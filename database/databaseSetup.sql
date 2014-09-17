SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`users` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`users` (
  `userid` INT NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(254) NOT NULL COMMENT 'Maximum email length (as specified by RFC)' ,
  `password` VARCHAR(255) NOT NULL COMMENT 'password is a salted SHA512 hash.' ,
  `active` TINYINT(1) NOT NULL ,
  PRIMARY KEY (`userid`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`documents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`documents` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`documents` (
  `docguid` VARCHAR(38) NOT NULL ,
  `docname` VARCHAR(255) NOT NULL COMMENT 'NTFS file name max size limitation' ,
  `directoryid` INT NOT NULL ,
  `ownerid` INT NOT NULL ,
  `creationtime` DATETIME NOT NULL ,
  `lastaccesstime` DATETIME NOT NULL ,
  `modificationtime` DATETIME NOT NULL ,
  `checkedoutuser` INT NULL ,
  `lastaccessuser` INT NOT NULL ,
  `modificationuser` INT NOT NULL ,
  `version` INT NOT NULL ,
  `comment` VARCHAR(255) NOT NULL ,
  `filesize` INT NOT NULL ,
  `groupid` INT NOT NULL ,
  PRIMARY KEY (`docguid`) ,
  INDEX `ownerkey_idx` (`ownerid` ASC) ,
  INDEX `checkedoutkey_idx` (`checkedoutuser` ASC) ,
  INDEX `lasteditkey_idx` (`lastaccessuser` ASC) ,
  INDEX `modifiedkey_idx` (`modificationuser` ASC) ,
  CONSTRAINT `ownerkey`
    FOREIGN KEY (`ownerid` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `checkedoutkey`
    FOREIGN KEY (`checkedoutuser` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `lastaccesskey`
    FOREIGN KEY (`lastaccessuser` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `modifiedkey`
    FOREIGN KEY (`modificationuser` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`directories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`directories` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`directories` (
  `dirid` INT NOT NULL AUTO_INCREMENT ,
  `dirname` VARCHAR(255) NOT NULL ,
  `parentfolder` INT NULL ,
  `owner` INT NOT NULL ,
  PRIMARY KEY (`dirid`) ,
  INDEX `ownerkey_idx` (`owner` ASC) ,
  CONSTRAINT `dirownerkey`
    FOREIGN KEY (`owner` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`departments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`departments` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`departments` (
  `deptid` INT NOT NULL AUTO_INCREMENT ,
  `deptname` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`deptid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`deptresponsibilities`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`deptresponsibilities` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`deptresponsibilities` (
  `userid` INT NOT NULL ,
  `responsabledept` INT NOT NULL ,
  PRIMARY KEY (`userid`) ,
  INDEX `userkey_idx` (`userid` ASC) ,
  CONSTRAINT `userkey`
    FOREIGN KEY (`userid` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `responsable`
    FOREIGN KEY (`responsabledept` )
    REFERENCES `mydb`.`departments` (`deptid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`acl`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`acl` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`acl` (
  `userid` INT NOT NULL ,
  `groupid` INT NOT NULL ,
  `sharedread` TINYINT(1) NOT NULL ,
  `sharedupdate` TINYINT(1) NOT NULL ,
  `shareddelete` TINYINT(1) NOT NULL ,
  `sharedlock` TINYINT(1) NOT NULL ,
  PRIMARY KEY (`userid`, `groupid`) ,
  INDEX `userkey_idx` (`userid` ASC) ,
  CONSTRAINT `acluserkey`
    FOREIGN KEY (`userid` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`systemlog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`systemlog` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`systemlog` (
  `eventid` INT NOT NULL AUTO_INCREMENT ,
  `comment` TEXT NOT NULL ,
  `eventdate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `category` INT NOT NULL ,
  `userid` INT NULL ,
  PRIMARY KEY (`eventid`) ,
  INDEX `userkey_idx` (`userid` ASC) ,
  CONSTRAINT `sysuserkey`
    FOREIGN KEY (`userid` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`userrole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`userrole` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`userrole` (
  `userid` INT NOT NULL ,
  `usertype` INT NOT NULL ,
  `department` INT NULL ,
  PRIMARY KEY (`userid`) ,
  INDEX `userkey_idx` (`userid` ASC) ,
  INDEX `departmentkey_idx` (`usertype` ASC) ,
  CONSTRAINT `userrolekey`
    FOREIGN KEY (`userid` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `departmentkey`
    FOREIGN KEY (`usertype` )
    REFERENCES `mydb`.`departments` (`deptid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`rolerequests`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`rolerequests` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`rolerequests` (
  `requestinguser` INT NOT NULL ,
  `requestedrole` INT NOT NULL ,
  `requesteddept` INT NULL ,
  PRIMARY KEY (`requestinguser`) ,
  INDEX `userkey_idx` (`requestinguser` ASC) ,
  INDEX `requesteddeptkey_idx` (`requesteddept` ASC) ,
  CONSTRAINT `roleuserkey`
    FOREIGN KEY (`requestinguser` )
    REFERENCES `mydb`.`users` (`userid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `requesteddeptkey`
    FOREIGN KEY (`requesteddept` )
    REFERENCES `mydb`.`departments` (`deptid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`pages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`pages` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`pages` (
  `pageid` INT NOT NULL AUTO_INCREMENT ,
  `modelandviewname` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`pageid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`pagepermissions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`pagepermissions` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`pagepermissions` (
  `pageid` INT NOT NULL AUTO_INCREMENT ,
  `userrole` INT NOT NULL ,
  PRIMARY KEY (`pageid`, `userrole`) ,
  INDEX `pageidkey_idx` (`pageid` ASC) ,
  CONSTRAINT `pageidkey`
    FOREIGN KEY (`pageid` )
    REFERENCES `mydb`.`pages` (`pageid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`usertype`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`usertype` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`usertype` (
  `roleid` INT NOT NULL AUTO_INCREMENT ,
  `rolename` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`roleid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`whitelist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`whitelist` ;

CREATE  TABLE IF NOT EXISTS `mydb`.`whitelist` (
  `filetype` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`filetype`) )
ENGINE = InnoDB;


SET SQL_MODE = '';
GRANT USAGE ON *.* TO systemadmin;
 DROP USER systemadmin;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER `systemadmin` IDENTIFIED BY 'qabuT4uC';

grant DELETE on TABLE `mydb`.`systemlog` to systemadmin;
grant INSERT on TABLE `mydb`.`systemlog` to systemadmin;
grant SELECT on TABLE `mydb`.`systemlog` to systemadmin;
grant UPDATE on TABLE `mydb`.`systemlog` to systemadmin;
grant INSERT on TABLE `mydb`.`users` to systemadmin;
grant SELECT on TABLE `mydb`.`users` to systemadmin;
grant UPDATE on TABLE `mydb`.`users` to systemadmin;
grant SELECT on TABLE `mydb`.`userrole` to systemadmin;
grant DELETE on TABLE `mydb`.`userrole` to systemadmin;
grant UPDATE on TABLE `mydb`.`userrole` to systemadmin;
SET SQL_MODE = '';
GRANT USAGE ON *.* TO temporary;
 DROP USER temporary;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER `temporary` IDENTIFIED BY '9uvEhaPh';

grant INSERT on TABLE `mydb`.`rolerequests` to temporary;
grant SELECT on TABLE `mydb`.`userrole` to temporary;
SET SQL_MODE = '';
GRANT USAGE ON *.* TO regularguest;
 DROP USER regularguest;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER `regularguest` IDENTIFIED BY 'xubU7eTU';

grant SELECT on TABLE `mydb`.`users` to regularguest;
grant UPDATE on TABLE `mydb`.`users` to regularguest;
grant UPDATE on TABLE `mydb`.`documents` to regularguest;
grant SELECT on TABLE `mydb`.`documents` to regularguest;
grant DELETE on TABLE `mydb`.`documents` to regularguest;
grant INSERT on TABLE `mydb`.`documents` to regularguest;
grant DELETE on TABLE `mydb`.`directories` to regularguest;
grant UPDATE on TABLE `mydb`.`directories` to regularguest;
grant SELECT on TABLE `mydb`.`directories` to regularguest;
grant INSERT on TABLE `mydb`.`directories` to regularguest;
grant SELECT on TABLE `mydb`.`departments` to regularguest;
grant SELECT on TABLE `mydb`.`acl` to regularguest;
grant UPDATE on TABLE `mydb`.`acl` to regularguest;
grant INSERT on TABLE `mydb`.`acl` to regularguest;
grant DELETE on TABLE `mydb`.`acl` to regularguest;
grant SELECT on TABLE `mydb`.`userrole` to regularguest;
grant INSERT on TABLE `mydb`.`rolerequests` to regularguest;
grant SELECT on TABLE `mydb`.`userrole` to regularguest;
SET SQL_MODE = '';
GRANT USAGE ON *.* TO management;
 DROP USER management;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER `management` IDENTIFIED BY 'Zux9gafr';

grant SELECT on TABLE `mydb`.`users` to management;
grant UPDATE on TABLE `mydb`.`users` to management;
grant DELETE on TABLE `mydb`.`documents` to management;
grant SELECT on TABLE `mydb`.`documents` to management;
grant UPDATE on TABLE `mydb`.`documents` to management;
grant INSERT on TABLE `mydb`.`documents` to management;
grant DELETE on TABLE `mydb`.`directories` to management;
grant SELECT on TABLE `mydb`.`directories` to management;
grant UPDATE on TABLE `mydb`.`directories` to management;
grant INSERT on TABLE `mydb`.`directories` to management;
grant SELECT on TABLE `mydb`.`departments` to management;
grant UPDATE on TABLE `mydb`.`acl` to management;
grant SELECT on TABLE `mydb`.`acl` to management;
grant DELETE on TABLE `mydb`.`acl` to management;
grant INSERT on TABLE `mydb`.`acl` to management;
grant UPDATE on TABLE `mydb`.`deptresponsibilities` to management;
grant SELECT on TABLE `mydb`.`deptresponsibilities` to management;
grant DELETE on TABLE `mydb`.`deptresponsibilities` to management;
grant INSERT on TABLE `mydb`.`deptresponsibilities` to management;
grant SELECT on TABLE `mydb`.`userrole` to management;
grant INSERT on TABLE `mydb`.`rolerequests` to management;
grant SELECT on TABLE `mydb`.`userrole` to management;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
