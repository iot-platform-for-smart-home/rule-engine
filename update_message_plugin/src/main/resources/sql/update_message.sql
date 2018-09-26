CREATE TABLE IF NOT EXISTS `update_message` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `message` varchar(255),
    `messageType` varchar(31),
	`ts` bigint,
	PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;