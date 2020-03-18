-- auto Generated on 2020-03-02 23:52:59 
-- DROP TABLE IF EXISTS `user`; 
CREATE TABLE user(
    `uid` INTEGER(12) NOT NULL DEFAULT -1 COMMENT 'uid',
    `username` VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'username',
    `password` VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'password',
    `salt` VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'salt',
    `email` VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'email',
    `avatar` VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'avatar',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'user';
