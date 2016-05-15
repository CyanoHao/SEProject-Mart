CREATE TABLE `inventory` (
  `id` int(10) UNSIGNED NOT NULL,
  `prod_id` char(13) NOT NULL,
  `num` int(11) NOT NULL,
  `price` decimal(6,2) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `prod_info` (
  `id` char(13) NOT NULL,
  `name` tinytext NOT NULL,
  `price` decimal(6,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sale` (
  `id` char(11) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `discount` decimal(6,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sale_detail` (
  `id` int(10) UNSIGNED NOT NULL,
  `prod_id` char(13) NOT NULL,
  `price` decimal(6,2) NOT NULL,
  `sale_id` char(11) NOT NULL,
  `num` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user` (
  `name` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `priority` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `inventory`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `prod_info`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `sale`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `sale_detail`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`name`);

ALTER TABLE `inventory`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

-- ALTER TABLE `sale`
  -- MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `sale_detail`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
