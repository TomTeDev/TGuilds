CREATE TABLE IF NOT EXISTS `guilds` (
  `id` INT AUTO_INCREMENT,
  `guild_uuid` BINARY(16) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `tag` VARCHAR(32) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `owner_uuid` BINARY(16) NOT NULL,
  `activity_points` BIGINT DEFAULT 0,
  `description` TEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`guild_uuid`),
  UNIQUE KEY (`name`)
);
CREATE TABLE IF NOT EXISTS `guild_members` (
  `id` INT AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `player_uuid` BINARY(16) NOT NULL,
  `rank` VARCHAR(255) NOT NULL,
  `guild_id` INT,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`),
  FOREIGN KEY (`guild_id`) REFERENCES `guilds`(`id`) ON DELETE CASCADE
);
