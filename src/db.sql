CREATE TABLE IF NOT EXISTS `sr03`.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family_name` varchar(20) DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `login` varchar(45) UNIQUE NOT NULL,
  `is_admin` tinyint(4) DEFAULT '0',
  `gender` tinyint(4) DEFAULT '0',
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE IF NOT EXISTS `sr03`.`forum` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL DEFAULT 'new forum',
  `begin_date` DATETIME NOT NULL,
  `end_date` DATETIME NOT NULL,
  `owner_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`owner_id`) REFERENCES `sr03`.`user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION);

  --   CREATE TABLE `sr03`.`message` (
  -- `id` INT NOT NULL AUTO_INCREMENT,
  -- `content` MEDIUMTEXT NULL,
  -- `editor` INT NOT NULL,
  -- `destination` INT NOT NULL,
  -- INDEX `FK_editor_idx` (`editor` ASC) VISIBLE,
  -- INDEX `Fk_dest_idx` (`destination` ASC) VISIBLE,
  -- PRIMARY KEY (`id`),
  -- CONSTRAINT `FK_editor`
  --   FOREIGN KEY (`editor`)
  --   REFERENCES `sr03`.`user` (`id`)
  --   ON DELETE NO ACTION
  --   ON UPDATE NO ACTION,
  -- CONSTRAINT `Fk_dest`
  --   FOREIGN KEY (`destination`)
  --   REFERENCES `sr03`.`forum` (`id`)
  --   ON DELETE CASCADE
  --   ON UPDATE CASCADE);

  --   CREATE TABLE `sr03`.`subscriptions` (
  -- `id_user` INT NOT NULL,
  -- `id_forum` INT NOT NULL,
  -- PRIMARY KEY (`id_user`, `id_forum`),
  -- INDEX `FK_subs_forum_idx` (`id_forum` ASC) VISIBLE,
  -- CONSTRAINT `FK_subs_user`
  --   FOREIGN KEY (`id_user`)
  --   REFERENCES `sr03`.`user` (`id`)
  --   ON DELETE NO ACTION
  --   ON UPDATE NO ACTION,
  -- CONSTRAINT `FK_subs_forum`
  --   FOREIGN KEY (`id_forum`)
  --   REFERENCES `sr03`.`forum` (`id`)
  --   ON DELETE NO ACTION
  --   ON UPDATE NO ACTION);
