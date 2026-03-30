package com.aliev.mes.interaction.receiving_order.service.impl;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.receiving.order.OrderDto;
import com.aliev.mes.common.dto.receiving.order.OrderNewDto;
import com.aliev.mes.common.dto.receiving.order.OrderUpdateDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionNewDto;
import com.aliev.mes.common.exceptions.NoDataFoundException;
import com.aliev.mes.interaction.receiving_order.mapper.OrderMapper;
import com.aliev.mes.interaction.receiving_order.model.Order;
import com.aliev.mes.interaction.receiving_order.repository.OrderRepository;
import com.aliev.mes.interaction.receiving_order.service.OrderPositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для класса OrderServiceImpl.
 * <p>
 * Этот класс содержит тесты, проверяющие корректность работы сервиса заказов:
 * - Создание новых заказов (с проверкой на дубликаты)
 * - Получение заказов по ID и списку ID
 * - Обновление существующих заказов
 * - Удаление заказов
 * - Обработка ошибок при отсутствии данных
 * <p>
 * Тесты используют Mockito для мокирования зависимостей:
 * - OrderRepository - репозиторий для работы с базой данных
 * - OrderMapper - маппер для преобразования между DTO и моделью
 * - OrderPositionService - сервис для работы с позициями заказа
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderPositionService orderPositionService;

    private OrderServiceImpl orderService;

    // Тестовые данные для использования в тестах
    private final Long TEST_ORDER_ID = 1L;
    private final String TEST_EXTERNAL_ID = "EXT-001";
    private final String TEST_ORDER_NO = "ORD-2024-001";
    private final LocalDate TEST_ORDER_DATE = LocalDate.of(2024, 1, 15);
    private final String TEST_CUSTOMER_NAME = "ООО Ромашка";

    @BeforeEach
    void setUp() {
        // Инициализация сервиса с моковыми зависимостями перед каждым тестом
        orderService = new OrderServiceImpl(orderRepository, orderMapper, orderPositionService);
    }

    /**
     * Тесты для метода create(OrderNewDto dto)
     * <p>
     * Проверяют создание новых заказов и обработку дубликатов.
     */
    @Nested
    @DisplayName("Тесты создания заказа (create)")
    class CreateTests {

        /**
         * Тест успешного создания нового заказа без позиций.
         * <p>
         * Сценарий: Пользователь создаёт новый заказ с уникальным внешним идентификатором.
         * Ожидаемый результат: Заказ сохраняется в БД и возвращается DTO с присвоенным ID.
         */
        @Test
        @DisplayName("Успешное создание нового заказа без позиций")
        void createNewOrderWithoutPositions_Success() {
            // Подготовка тестовых данных
            OrderNewDto orderNewDto = new OrderNewDto();
            orderNewDto.setExternalId(TEST_EXTERNAL_ID);
            orderNewDto.setOrderNo(TEST_ORDER_NO);
            orderNewDto.setOrderDate(TEST_ORDER_DATE);
            orderNewDto.setCustomerName(TEST_CUSTOMER_NAME);
            orderNewDto.setPositions(new ArrayList<>());

            Order orderModel = new Order();
            orderModel.setId(TEST_ORDER_ID);
            orderModel.setExternalId(TEST_EXTERNAL_ID);
            orderModel.setOrderNo(TEST_ORDER_NO);
            orderModel.setOrderDate(TEST_ORDER_DATE);
            orderModel.setCustomerName(TEST_CUSTOMER_NAME);
            orderModel.setStatus(ProcessingStatus.RECEIVED);

            OrderDto orderDto = new OrderDto();
            orderDto.setId(TEST_ORDER_ID);
            orderDto.setExternalId(TEST_EXTERNAL_ID);
            orderDto.setOrderNo(TEST_ORDER_NO);
            orderDto.setOrderDate(TEST_ORDER_DATE);
            orderDto.setCustomerName(TEST_CUSTOMER_NAME);

            // Настройка поведения моков
            when(orderMapper.toModel(any(OrderNewDto.class))).thenReturn(orderModel);
            when(orderRepository.getByExternalId(TEST_EXTERNAL_ID)).thenReturn(null); // Заказ не существует
            when(orderRepository.save(any(Order.class))).thenReturn(orderModel);
            when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

            // Вызов тестируемого метода
            OrderDto result = orderService.create(orderNewDto);

            // Проверка результатов
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(TEST_ORDER_ID);
            assertThat(result.getExternalId()).isEqualTo(TEST_EXTERNAL_ID);

            // Верификация вызовов репозитория и маппера
            verify(orderRepository, times(1)).getByExternalId(TEST_EXTERNAL_ID);
            verify(orderRepository, times(1)).save(any(Order.class));
            verify(orderMapper, times(2)).toModel(any(OrderNewDto.class)); // toModel вызывается дважды
        }

        /**
         * Тест создания заказа с позициями.
         * <p>
         * Сценарий: Пользователь создаёт заказ со списком позиций.
         * Ожидаемый результат: Заказ сохраняется, позиции привязываются к заказу и сохраняются.
         */
        @Test
        @DisplayName("Успешное создание заказа с позициями")
        void createOrderWithPositions_Success() {
            // Подготовка тестовых данных
            OrderNewDto orderNewDto = new OrderNewDto();
            orderNewDto.setExternalId(TEST_EXTERNAL_ID);
            orderNewDto.setOrderNo(TEST_ORDER_NO);
            orderNewDto.setOrderDate(TEST_ORDER_DATE);
            orderNewDto.setCustomerName(TEST_CUSTOMER_NAME);

            // Создаём тестовые позиции
            OrderPositionNewDto position1 = new OrderPositionNewDto();
            position1.setExternalId("POS-001");
            position1.setPositionNo(1L);
            position1.setProduct("Товар А");
            position1.setQuantity(10.0);

            List<OrderPositionNewDto> positions = List.of(position1);
            orderNewDto.setPositions(positions);

            Order orderModel = new Order();
            orderModel.setId(TEST_ORDER_ID);
            orderModel.setExternalId(TEST_EXTERNAL_ID);

            OrderDto orderDto = new OrderDto();
            orderDto.setId(TEST_ORDER_ID);
            orderDto.setExternalId(TEST_EXTERNAL_ID);

            OrderPositionDto savedPosition = new OrderPositionDto();
            savedPosition.setId(1L);
            savedPosition.setReceivedOrder(TEST_ORDER_ID);

            // Настройка поведения моков
            when(orderMapper.toModel(any(OrderNewDto.class))).thenReturn(orderModel);
            when(orderRepository.getByExternalId(TEST_EXTERNAL_ID)).thenReturn(null);
            when(orderRepository.save(any(Order.class))).thenReturn(orderModel);
            when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);
            when(orderPositionService.createMany(anyList())).thenReturn(List.of(savedPosition));

            // Вызов тестируемого метода
            OrderDto result = orderService.create(orderNewDto);

            // Проверка результатов
            assertThat(result).isNotNull();
            assertThat(result.getPositions()).hasSize(1);
            assertThat(result.getPositions().getFirst().getId()).isEqualTo(1L);

            // Верификация: позиции должны получить ID заказа перед сохранением
            ArgumentCaptor<List<OrderPositionNewDto>> positionsCaptor = ArgumentCaptor.forClass(List.class);
            verify(orderPositionService).createMany(positionsCaptor.capture());
            List<OrderPositionNewDto> capturedPositions = positionsCaptor.getValue();
            assertThat(capturedPositions.getFirst().getReceivedOrder()).isEqualTo(TEST_ORDER_ID);
        }

        /**
         * Тест обработки дубликата заказа.
         * <p>
         * Сценарий: Пользователь пытается создать заказ с уже существующим внешним ID,
         * но с новыми данными для обновления.
         * Ожидаемый результат: Существующий заказ обновляется, создаётся новый не должен быть создан.
         */
        @Test
        @DisplayName("Обработка дубликата заказа - обновление существующего")
        void createDuplicateOrder_UpdatesExisting() {
            // Подготовка тестовых данных
            OrderNewDto orderNewDto = new OrderNewDto();
            orderNewDto.setExternalId(TEST_EXTERNAL_ID);
            orderNewDto.setOrderNo("NEW-ORDER-NUMBER");
            orderNewDto.setOrderDate(LocalDate.of(2024, 12, 31));
            orderNewDto.setCustomerName("Новый Заказчик");

            // Существующий заказ в БД
            Order existingOrder = new Order();
            existingOrder.setId(TEST_ORDER_ID);
            existingOrder.setExternalId(TEST_EXTERNAL_ID);
            existingOrder.setOrderNo("OLD-ORDER-NUMBER");
            existingOrder.setCustomerName("Старый Заказчик");

            // Настройка поведения моков
            when(orderMapper.toModel(any(OrderNewDto.class))).thenReturn(existingOrder);
            when(orderRepository.getByExternalId(TEST_EXTERNAL_ID)).thenReturn(existingOrder);
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

            // Вызов тестируемого метода
            orderService.create(orderNewDto);

            // Верификация: заказ должен быть сохранён (обновлён)
            verify(orderRepository, times(1)).save(any(Order.class));
        }
    }

    /**
     * Тесты для метода getById(Long id)
     * <p>
     * Проверяют получение заказа по его внутреннему идентификатору.
     */
    @Nested
    @DisplayName("Тесты получения заказа по ID (getById)")
    class GetByIdTests {

        /**
         * Тест успешного получения заказа по ID.
         * <p>
         * Сценарий: Пользователь запрашивает заказ, который существует в БД.
         * Ожидаемый результат: Возвращается DTO заказа с позициями.
         */
        @Test
        @DisplayName("Успешное получение существующего заказа по ID")
        void getById_ExistingOrder_Success() {
            // Подготовка тестовых данных
            Order order = new Order();
            order.setId(TEST_ORDER_ID);
            order.setExternalId(TEST_EXTERNAL_ID);

            OrderDto orderDto = new OrderDto();
            orderDto.setId(TEST_ORDER_ID);

            List<OrderPositionDto> positions = new ArrayList<>();
            positions.add(new OrderPositionDto());

            // Настройка поведения моков
            when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
            when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);
            when(orderPositionService.getByOrderId(TEST_ORDER_ID)).thenReturn(positions);

            // Вызов тестируемого метода
            OrderDto result = orderService.getById(TEST_ORDER_ID);

            // Проверка результатов
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(TEST_ORDER_ID);
            assertThat(result.getPositions()).hasSize(1);

            verify(orderRepository, times(1)).findById(TEST_ORDER_ID);
        }

        /**
         * Тест получения несуществующего заказа.
         * <p>
         * Сценарий: Пользователь запрашивает заказ с ID, которого нет в БД.
         * Ожидаемый результат: Бросается исключение NoDataFoundException.
         */
        @Test
        @DisplayName("Получение несуществующего заказа - бросает NoDataFoundException")
        void getById_NonExistingOrder_ThrowsException() {
            // Настройка поведения моков
            when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());

            // Проверка исключения
            assertThatThrownBy(() -> orderService.getById(TEST_ORDER_ID))
                    .isInstanceOf(NoDataFoundException.class)
                    .hasMessageContaining(String.format("Заказ по уникальному идентификатору %s не найден", TEST_ORDER_ID));
        }
    }

    /**
     * Тесты для метода getAll(List<Long> ids, ProcessingStatus status, int from, int size)
     * <p>
     * Проверяют получение списка заказов с различными фильтрами.
     */
    @Nested
    @DisplayName("Тесты получения списка заказов (getAll)")
    class GetAllTests {

        /**
         * Тест получения всех заказов без фильтров.
         * <p>
         * Сценарий: Пользователь запрашивает все заказы с пагинацией.
         * Ожидаемый результат: Возвращается страница заказов.
         */
        @Test
        @DisplayName("Получение всех заказов без фильтров")
        void getAll_WithoutFilters_Success() {
            // Подготовка тестовых данных
            Order order1 = new Order();
            order1.setId(1L);
            Order order2 = new Order();
            order2.setId(2L);

            Page<Order> page = new PageImpl<>(List.of(order1, order2));

            OrderDto dto1 = new OrderDto();
            dto1.setId(1L);
            OrderDto dto2 = new OrderDto();
            dto2.setId(2L);

            // Настройка поведения моков
            PageRequest pageRequest = PageRequest.of(0, 10);
            when(orderRepository.findAll(pageRequest)).thenReturn(page);
            when(orderMapper.toDto(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                OrderDto dto = new OrderDto();
                dto.setId(order.getId());
                return dto;
            });
            when(orderPositionService.getByOrderId(anyLong())).thenReturn(new ArrayList<>());

            // Вызов тестируемого метода
            List<OrderDto> result = orderService.getAll(null, ProcessingStatus.ALL, 0, 10);

            // Проверка результатов
            assertThat(result).hasSize(2);
            verify(orderRepository, times(1)).findAll(pageRequest);
        }

        /**
         * Тест получения заказов по списку ID.
         * <p>
         * Сценарий: Пользователь запрашивает конкретные заказы по их ID.
         * Ожидаемый результат: Возвращаются только запрошенные заказы.
         */
        @Test
        @DisplayName("Получение заказов по списку ID")
        void getAll_ByIds_Success() {
            // Подготовка тестовых данных
            List<Long> ids = List.of(1L, 2L, 3L);
            Order order1 = new Order();
            order1.setId(1L);
            Order order2 = new Order();
            order2.setId(2L);

            // Настройка поведения моков
            when(orderRepository.findAllById(ids)).thenReturn(List.of(order1, order2));
            when(orderMapper.toDto(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                OrderDto dto = new OrderDto();
                dto.setId(order.getId());
                return dto;
            });
            when(orderPositionService.getByOrderId(anyLong())).thenReturn(new ArrayList<>());

            // Вызов тестируемого метода
            List<OrderDto> result = orderService.getAll(ids, ProcessingStatus.ALL, 0, 10);

            // Проверка результатов
            assertThat(result).hasSize(2);
            verify(orderRepository, times(1)).findAllById(ids);
        }

        /**
         * Тест получения заказов по статусу.
         * <p>
         * Сценарий: Пользователь запрашивает заказы с определённым статусом обработки.
         * Ожидаемый результат: Возвращаются только заказы с указанным статусом.
         */
        @Test
        @DisplayName("Получение заказов по статусу")
        void getAll_ByStatus_Success() {
            // Подготовка тестовых данных
            Order order = new Order();
            order.setId(1L);
            Page<Order> page = new PageImpl<>(List.of(order));

            // Настройка поведения моков
            when(orderRepository.findAllByStatus(eq(ProcessingStatus.IN_PROGRESS), any(PageRequest.class)))
                    .thenReturn(page);
            when(orderMapper.toDto(any(Order.class))).thenAnswer(invocation -> {
                Order order1 = invocation.getArgument(0);
                OrderDto dto = new OrderDto();
                dto.setId(order1.getId());
                return dto;
            });
            when(orderPositionService.getByOrderId(anyLong())).thenReturn(new ArrayList<>());

            // Вызов тестируемого метода
            List<OrderDto> result = orderService.getAll(null, ProcessingStatus.IN_PROGRESS, 0, 10);

            // Проверка результатов
            assertThat(result).hasSize(1);
            verify(orderRepository, times(1)).findAllByStatus(eq(ProcessingStatus.IN_PROGRESS), any(PageRequest.class));
        }
    }

    /**
     * Тесты для метода update(OrderUpdateDto dto)
     * <p>
     * Проверяют обновление существующих заказов.
     */
    @Nested
    @DisplayName("Тесты обновления заказа (update)")
    class UpdateTests {

        /**
         * Тест успешного обновления заказа.
         * <p>
         * Сценарий: Пользователь обновляет данные существующего заказа.
         * Ожидаемый результат: Заказ обновляется, возвращается актуальный DTO.
         */
        @Test
        @DisplayName("Успешное обновление заказа")
        void update_ExistingOrder_Success() {
            // Подготовка тестовых данных
            OrderUpdateDto dto = new OrderUpdateDto();
            dto.setId(TEST_ORDER_ID);
            dto.setOrderNo("UPDATED-ORDER-NUMBER");

            Order existingOrder = new Order();
            existingOrder.setId(TEST_ORDER_ID);
            existingOrder.setOrderNo("OLD-ORDER-NUMBER");
            existingOrder.setCustomerName("Old Customer");

            // Настройка поведения моков
            when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(existingOrder));
            when(orderMapper.toModel(any(OrderUpdateDto.class))).thenReturn(new Order());
            when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());
            when(orderPositionService.updateMany(anyList())).thenReturn(new ArrayList<>());

            // Вызов тестируемого метода
            orderService.update(dto);

            // Верификация: заказ должен быть сохранён
            verify(orderRepository, times(1)).save(any(Order.class));
        }

        /**
         * Тест обновления несуществующего заказа.
         * <p>
         * Сценарий: Пользователь пытается обновить заказ с несуществующим ID.
         * Ожидаемый результат: Бросается исключение NoDataFoundException.
         */
        @Test
        @DisplayName("Обновление несуществующего заказа - бросает NoDataFoundException")
        void update_NonExistingOrder_ThrowsException() {
            // Подготовка тестовых данных
            OrderUpdateDto dto = new OrderUpdateDto();
            dto.setId(TEST_ORDER_ID);

            // Настройка поведения моков
            when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());

            // Проверка исключения
            assertThatThrownBy(() -> orderService.update(dto))
                    .isInstanceOf(NoDataFoundException.class)
                    .hasMessageContaining(String.format("Заказ по уникальному идентификатору %s не найден", TEST_ORDER_ID));
        }
    }

    /**
     * Тесты для метода delete(Long id)
     * <p>
     * Проверяют удаление заказов.
     */
    @Nested
    @DisplayName("Тесты удаления заказа (delete)")
    class DeleteTests {

        /**
         * Тест успешного удаления заказа.
         * <p>
         * Сценарий: Пользователь удаляет существующий заказ.
         * Ожидаемый результат: Позиции заказа удаляются, затем сам заказ.
         */
        @Test
        @DisplayName("Успешное удаление заказа")
        void delete_ExistingOrder_Success() {
            // Подготовка тестовых данных
            Order order = new Order();
            order.setId(TEST_ORDER_ID);

            // Настройка поведения моков
            when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));

            // Вызов тестируемого метода
            orderService.delete(TEST_ORDER_ID);

            // Верификация: сначала удаляются позиции, затем заказ
            verify(orderPositionService, times(1)).deleteByOrderId(TEST_ORDER_ID);
            verify(orderRepository, times(1)).delete(any(Order.class));
        }

        /**
         * Тест удаления несуществующего заказа.
         * <p>
         * Сценарий: Пользователь пытается удалить заказ с несуществующим ID.
         * Ожидаемый результат: Бросается исключение NoDataFoundException.
         */
        @Test
        @DisplayName("Удаление несуществующего заказа - бросает NoDataFoundException")
        void delete_NonExistingOrder_ThrowsException() {
            // Настройка поведения моков
            when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());

            // Проверка исключения
            assertThatThrownBy(() -> orderService.delete(TEST_ORDER_ID))
                    .isInstanceOf(NoDataFoundException.class)
                    .hasMessageContaining(String.format("Заказ по уникальному идентификатору %s не найден", TEST_ORDER_ID));
        }
    }
}
