
CREATE TABLE `stage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$

CREATE TABLE `priv` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$


CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$


CREATE TABLE `role_priv` (
  `role_id` bigint(20) NOT NULL,
  `priv_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`priv_id`),
  KEY `FK8fybdps9ommh46xtq2we6r5wu` (`priv_id`),
  CONSTRAINT `FK8fybdps9ommh46xtq2we6r5wu` FOREIGN KEY (`priv_id`) REFERENCES `priv` (`id`),
  CONSTRAINT `FKbrkt9wy3wi314sii2asw7adqt` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$


CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$


CREATE TABLE `service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `project_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnwpqsts1xrsew7slgo1ewror7` (`project_id`),
  CONSTRAINT `FKnwpqsts1xrsew7slgo1ewror7` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$

CREATE TABLE `service_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `stage_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `service_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsw7sk23tgp5q5jfvcy8wn3sy2` (`stage_id`),
  KEY `FKjnitwq6k722t952brhir6l47n` (`role_id`),
  KEY `FKafffub47uh7i8566emjwqlb49` (`service_id`),
  CONSTRAINT `FKafffub47uh7i8566emjwqlb49` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKjnitwq6k722t952brhir6l47n` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKsw7sk23tgp5q5jfvcy8wn3sy2` FOREIGN KEY (`stage_id`) REFERENCES `stage` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$


CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$


CREATE TABLE `user_project` (
  `user_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`project_id`),
  KEY `FKocfkr6u2yh3w1qmybs8vxuv1c` (`project_id`),
  CONSTRAINT `FKocfkr6u2yh3w1qmybs8vxuv1c` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `FKpw81exe7fsdl7mddqujvu91kx` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$


CREATE TABLE `user_service_role` (
  `user_id` bigint(20) NOT NULL,
  `service_role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`service_role_id`),
  KEY `FKp4g7iyi33vmrsyiayb5eatxhb` (`service_role_id`),
  CONSTRAINT `FKe80014gnug6h70w7gfg20btxh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKp4g7iyi33vmrsyiayb5eatxhb` FOREIGN KEY (`service_role_id`) REFERENCES `service_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci$$






