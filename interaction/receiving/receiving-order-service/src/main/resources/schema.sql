-- ReceivedOrder
CREATE TABLE received_order
(
    id            UUID PRIMARY KEY,
    external_id   VARCHAR(255) NOT NULL,
    order_no      VARCHAR(255) NOT NULL,
    order_date    DATE         NOT NULL,
    customer_name VARCHAR(255),
    status        VARCHAR(50)  NOT NULL DEFAULT 'RECEIVED'
);

COMMENT
ON TABLE received_order IS 'Таблица заказов, полученных из внешних систем';
COMMENT
ON COLUMN received_order.id IS 'Идентификатор заказа';
COMMENT
ON COLUMN received_order.external_id IS 'Внешний идентификатор заказа';
COMMENT
ON COLUMN received_order.order_no IS 'Номер заказа';
COMMENT
ON COLUMN received_order.order_date IS 'Дата заказа';
COMMENT
ON COLUMN received_order.customer_name IS 'Наименование заказчика';
COMMENT
ON COLUMN received_order.status IS 'Статус обработки заказа';

-- ReceivedOrderPosition
CREATE TABLE received_order_position
(
    id                UUID PRIMARY KEY,
    external_id       VARCHAR(255) NOT NULL,
    received_order_id UUID         NOT NULL,
    position_no       BIGINT,
    product           VARCHAR(255),
    quantity          DOUBLE PRECISION,
    status            VARCHAR(50)  NOT NULL DEFAULT 'RECEIVED'
);

COMMENT
ON TABLE received_order_position IS 'Таблица полученных позиций заказов';
COMMENT
ON COLUMN received_order_position.id IS 'Идентификатор полученной позиции заказа';
COMMENT
ON COLUMN received_order_position.external_id IS 'Внешний идентификатор позиции заказа';
COMMENT
ON COLUMN received_order_position.received_order_id IS 'Внешний ключ на полученный заказ';
COMMENT
ON COLUMN received_order_position.position_no IS 'Номер позиции';
COMMENT
ON COLUMN received_order_position.product IS 'Наименование продукта';
COMMENT
ON COLUMN received_order_position.quantity IS 'Количество продукта';
COMMENT
ON COLUMN received_order_position.status IS 'Статус обработки заказа';