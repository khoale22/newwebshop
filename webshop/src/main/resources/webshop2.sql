CREATE DATABASE  IF NOT EXISTS `webshop2` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `webshop2`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: webshop2
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `admin_id` varchar(45) NOT NULL,
  `admin_email` varchar(45) NOT NULL,
  `amin_pass` varchar(45) NOT NULL,
  `role_id` varchar(45) NOT NULL,
  PRIMARY KEY  (`admin_id`),
  KEY `fdsfsdf_idx` (`role_id`),
  CONSTRAINT `fdsfsdf` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('admin','admin@gmail.com','123','role1');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill` (
  `bill_id` varchar(45) NOT NULL,
  `payment` varchar(45) NOT NULL,
  `date` datetime default NULL,
  `total` double default NULL,
  `user_id` varchar(45) NOT NULL,
  `phone` int(11) default NULL,
  `address_payment` varchar(45) default NULL,
  PRIMARY KEY  (`bill_id`),
  KEY `fdsfds_idx` (`user_id`),
  CONSTRAINT `fdsfds` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES ('5stpvycub9nzlxxfqo4ymumx0eau8o','Bank Transfer','2018-08-29 19:01:44',20,'user1',905211322,'12 ham nghi'),('85ccy0xdju9xm9481qh426wkk0d0uu','Live','2018-08-29 23:38:25',40,'user1',905433333,'12B quang trung'),('bbvekdjrxe33j048os6w7s84klfox4','Bank Transfer','2018-08-29 19:18:06',20,'user1',905445324,'12 quang trung'),('gfl5nsz8qj8tmp9oljy2us2a26r067','Live','2018-08-30 00:47:50',60,'user1',905211222,'15 to ngoc van'),('ivkvewt51f2oygvar69wcijizi8ahv','Bank Transfer','2018-08-30 00:34:25',40,'user1',98764843,'12 ham nghi'),('kthxouf84le2464l69m1495qbj66s6','Live','2018-08-29 21:18:34',20,'user1',905686653,'12 ham nghi'),('liagtc67nksyslaa2w8y0va7u9wevv','Bank Transfer','2018-08-30 00:36:56',45,'user1',95673833,'15 to ngoc van'),('odnmsc5l73pg25kjmcyrbumyda8s0p','Bank Transfer','2018-08-30 02:03:36',20,'user1',5646,'12 ham nghi'),('swget2m50l0k1fj9sfamopbfmr54tg','Bank Transfer','2018-08-29 19:02:04',20,'user1',905443233,'12B quang trung'),('v9sp4r13bcr13m7fxxrzs1jyu2k2re','Bank Transfer','2018-08-29 18:52:16',40,'user1',767667,'12 ham nghi'),('xjb8z3gx3ne1b090bhvz60cwmcrezi','Bank Transfer','2018-08-30 01:20:19',20,'user1',5435435,'543543');
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billdetail`
--

DROP TABLE IF EXISTS `billdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billdetail` (
  `billDetail_id` bigint(20) NOT NULL auto_increment,
  `price` double NOT NULL,
  `product_id` varchar(45) NOT NULL,
  `quantily` int(11) NOT NULL,
  `bill_id` varchar(45) NOT NULL,
  PRIMARY KEY  (`billDetail_id`),
  KEY `fdsfsdf_idx` (`bill_id`),
  KEY `fdsafasfd_idx` (`bill_id`),
  KEY `asddadsfsf_idx` (`product_id`),
  CONSTRAINT `asddadsfsf` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fdsafasfd` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billdetail`
--

LOCK TABLES `billdetail` WRITE;
/*!40000 ALTER TABLE `billdetail` DISABLE KEYS */;
INSERT INTO `billdetail` VALUES (120,20,'pro1',2,'v9sp4r13bcr13m7fxxrzs1jyu2k2re'),(121,20,'pro1',1,'5stpvycub9nzlxxfqo4ymumx0eau8o'),(122,20,'pro1',1,'swget2m50l0k1fj9sfamopbfmr54tg'),(123,20,'pro1',1,'bbvekdjrxe33j048os6w7s84klfox4'),(124,20,'pro1',1,'kthxouf84le2464l69m1495qbj66s6'),(125,40,'pro3',1,'85ccy0xdju9xm9481qh426wkk0d0uu'),(126,20,'pro1',2,'ivkvewt51f2oygvar69wcijizi8ahv'),(127,45,'pro9',1,'liagtc67nksyslaa2w8y0va7u9wevv'),(128,20,'pro1',1,'gfl5nsz8qj8tmp9oljy2us2a26r067'),(129,40,'pro3',1,'gfl5nsz8qj8tmp9oljy2us2a26r067'),(130,20,'pro1',1,'xjb8z3gx3ne1b090bhvz60cwmcrezi'),(131,20,'pro1',1,'odnmsc5l73pg25kjmcyrbumyda8s0p');
/*!40000 ALTER TABLE `billdetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_id` varchar(45) NOT NULL,
  `category_name` varchar(45) NOT NULL,
  PRIMARY KEY  (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('cate1','iphone'),('cate2','samsung'),('cate3','oppo'),('cate4','vetu'),('cate5','motorola'),('cate6','windowphone');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `product_id` varchar(45) NOT NULL,
  `product_name` varchar(45) NOT NULL,
  `product_image` varchar(2525) NOT NULL,
  `product_price` double NOT NULL,
  `category_id` varchar(45) NOT NULL,
  `quantity` int(11) NOT NULL,
  `screen` varchar(45) default NULL,
  `version` varchar(45) default NULL,
  `ram` varchar(45) default NULL,
  `description` varchar(2525) default NULL,
  PRIMARY KEY  (`product_id`),
  KEY `fdsfs_idx` (`category_id`),
  CONSTRAINT `fdsaf` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('pro1','Iphone1','images/iphone1.jpg',20,'cate1',58752,'400x300','IOS 8','2GB','Iphone 1 is a handheld personal computer. It possesses extensive computing capabilities'),('pro10','Samsung1','https://i.ebayimg.com/thumbs/images/g/8P0AAOSwduZbMGDc/s-l225.jpg',34,'cate2',0,'700x500','Adroid6','1GB','Samsung 1 is a handheld personal computer. It possesses extensive computing capabilities'),('pro11','Samsung2','https://i.ebayimg.com/thumbs/images/g/TvQAAOSwC81atJ1M/s-l225.jpg',23,'cate2',1,'400x300','Adroid5','3GB','Samsung 2 is a handheld personal computer. It possesses extensive computing capabilities'),('pro12','Samsung3','http://giahuemobile.com/wp-content/uploads/2016/03/vo-j5.jpg',23,'cate2',34,'400x400','Adroid7','4GB','Samsung 2 is a handheld personal computer. It possesses extensive computing capabilities'),('pro13','Samsung4','https://dienthoaidoc.com/wp-content/uploads/2013/06/samsung-galaxy-note-3-02.jpg',34,'cate2',4,'700x700','Adroid9','4Gb','Samsung 4 is a handheld personal computer. It possesses extensive computing capabilities'),('pro14','Samsung5','https://i.ebayimg.com/thumbs/images/g/0C4AAOSwMNxXXwfV/s-l225.jpg',34,'cate2',4,'600x600','Adroid5','5GB','Samsung 5 is a handheld personal computer. It possesses extensive computing capabilities'),('pro15','Samsung6','https://i.ebayimg.com/thumbs/images/g/cLoAAOSwXoxaU0Gu/s-l225.jpg',345,'cate2',2,'700x700','Adroid8','5Gb','Samsung 6 is a handheld personal computer. It possesses extensive computing capabilities'),('pro2','Iphone2','images/iphone2.jpeg',30,'cate1',330,'400x350','IOS 8.5','3GB','Iphone 2 is a handheld personal computer. It possesses extensive computing capabilities'),('pro3','Iphone3','images/iphone3.jpeg',40,'cate1',3376,'450x700','IOS 9','3GB','Iphone 3 is a handheld personal computer. It possesses extensive computing capabilities'),('pro4','Iphone4','images/iphone4.jpeg',24,'cate1',424,'500x560','IOS 9','4GB','Iphone 4 is a handheld personal computer. It possesses extensive computing capabilities'),('pro5','Iphone5','images/iphone5.jpeg',23,'cate1',3431,'400x560','IOS 9','3GB','Iphone 5 is a handheld personal computer. It possesses extensive computing capabilities'),('pro6','Iphone6','images/iphone6.jpeg',23,'cate1',343,'600x650','IOS 10','3GB','Iphone 6 is a handheld personal computer. It possesses extensive computing capabilities'),('pro7','Iphone7','images/iphone7.jpeg',23,'cate1',322,'670x700','IOS 10','3GB','Iphone 7 is a handheld personal computer. It possesses extensive computing capabilities'),('pro8','Iphone8','images/iphone8.jpeg',23,'cate1',227,'600x700','IOS 10','6GB','Iphone 8 is a handheld personal computer. It possesses extensive computing capabilities'),('pro9','IphoneX','images/iphoneX.jpeg',45,'cate1',3,'750x700','IOS 10','8GB','Iphone X is a handheld personal computer. It possesses extensive computing capabilities');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` varchar(45) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('role1','admin'),('role2','user');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` varchar(45) NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `user_email` varchar(45) NOT NULL,
  `user_pass` varchar(45) NOT NULL,
  `role_id` varchar(45) NOT NULL,
  PRIMARY KEY  (`user_id`),
  KEY `fdsafd_idx` (`role_id`),
  CONSTRAINT `fdsafaf` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('fdhsj','fdsaf','fdas','fsda','role2'),('fdsf','fdsf','fdsf','fds','role2'),('user1','khoa','hbkkhoa1@gmail.com','123','role2'),('user100','trung phan','trung@gmail.com','12323434','role2'),('user11','fdsf','fds','fdsf','role2'),('user2','trung','hbkkhoa2@gmail.com','123','role2'),('user3','fdaf','fdsaf','fdaf','role2'),('user34','khoale34','hbkkhoa34@gmail.com','123','role2'),('user4','fdf','fdsf','fdsf','role2'),('user5','fds','fds','fds','role2'),('user6','342','4324','24342','role2'),('user7','abc','fsfds','123','role2'),('user78','fdsf','fds','fds','role2'),('user8','fdsf','fds','fds','role2'),('user9','dsff','fds','fds','role2'),('user90','sdfs','sfd','sdf','role2');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-30  2:54:31
