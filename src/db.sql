CREATE TABLE `sr03`.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family_name` varchar(20) DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `login` varchar(45) NOT NULL,
  `is_admin` tinyint(4) DEFAULT '0',
  `gender` tinyint(4) DEFAULT '0',
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
