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
INSERT INTO `bill` VALUES ('25dutpoh30zpuo5xcwoi5a99iiw0rp','Bank Transfer','2018-08-27 20:56:29',93,'user1',44345345,'77 thang trung'),('4ee2foeyv6pwmg5r3trde7v03ofc7x','Live','2018-08-28 03:34:32',80,'user3',543525423,'4353543543'),('gffzqbgcwu94b40dfwcgwgb7i212yz','Live','2018-08-27 23:49:55',80,'user1',905323454,'15 pasta'),('i2ns919a7mie1jxn08cnei2bs688zh','Bank Transfer','2018-08-28 02:57:25',20,'user2',90543233,'12 quang trung'),('sia3b3rv9ojeoe5nwdxv6vu7s90eac','Bank Transfer','2018-08-27 20:55:31',20,'user1',940324,'13ham nghi'),('tay7lph3efa2teogbhqxxitycp11cb','Live','2018-08-28 03:32:42',80,'user7',4535,'5435gfd'),('tbnwjij3d805n919w3zhuu9kctbh0f','Live','2018-08-27 21:38:13',40,'user1',906564435,'656tongocvan'),('wf5a6m5l0b91cdeatrxqxz97tb09xk','Bank Transfer','2018-08-28 03:30:32',20,'user1',543535,'12 ham nghi');
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
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billdetail`
--

LOCK TABLES `billdetail` WRITE;
/*!40000 ALTER TABLE `billdetail` DISABLE KEYS */;
INSERT INTO `billdetail` VALUES (99,20,'pro1',1,'sia3b3rv9ojeoe5nwdxv6vu7s90eac'),(100,20,'pro1',2,'25dutpoh30zpuo5xcwoi5a99iiw0rp'),(101,30,'pro2',1,'25dutpoh30zpuo5xcwoi5a99iiw0rp'),(102,23,'pro5',1,'25dutpoh30zpuo5xcwoi5a99iiw0rp'),(103,40,'pro3',1,'tbnwjij3d805n919w3zhuu9kctbh0f'),(104,20,'pro1',1,'gffzqbgcwu94b40dfwcgwgb7i212yz'),(105,30,'pro2',2,'gffzqbgcwu94b40dfwcgwgb7i212yz'),(106,20,'pro1',1,'i2ns919a7mie1jxn08cnei2bs688zh'),(107,20,'pro1',1,'wf5a6m5l0b91cdeatrxqxz97tb09xk'),(108,20,'pro1',1,'tay7lph3efa2teogbhqxxitycp11cb'),(109,30,'pro2',2,'tay7lph3efa2teogbhqxxitycp11cb'),(110,20,'pro1',1,'4ee2foeyv6pwmg5r3trde7v03ofc7x'),(111,30,'pro2',2,'4ee2foeyv6pwmg5r3trde7v03ofc7x');
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
  `product_image` varchar(45) NOT NULL,
  `product_price` double NOT NULL,
  `category_id` varchar(45) NOT NULL,
  `quantity` int(11) NOT NULL,
  `screen` varchar(45) default NULL,
  `version` varchar(45) default NULL,
  `ram` varchar(45) default NULL,
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
INSERT INTO `product` VALUES ('pro1','Iphone1','images/iphone1.jpg',20,'cate1',58779,NULL,NULL,NULL),('pro2','Iphone2','images/iphone2.jpeg',30,'cate1',332,NULL,NULL,NULL),('pro3','Iphone3','images/iphone3.jpeg',40,'cate1',3379,NULL,NULL,NULL),('pro4','Iphone4','images/iphone4.jpeg',24,'cate1',424,NULL,NULL,NULL),('pro5','Iphone5','images/iphone5.jpeg',23,'cate1',3431,NULL,NULL,NULL),('pro6','Iphone6','images/iphone6.jpeg',23,'cate1',343,NULL,NULL,NULL),('pro7','Iphone7','images/iphone7.jpeg',23,'cate1',322,NULL,NULL,NULL),('pro8','Iphone8','images/iphone8.jpeg',23,'cate1',228,NULL,NULL,NULL),('pro9','IphoneX','images/iphoneX.jpeg',45,'cate1',4,NULL,NULL,NULL);
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
INSERT INTO `user` VALUES ('fdsf','fdsf','fdsf','fds','role2'),('user1','khoa','hbkkhoa1@gmail.com','123','role2'),('user2','trung','hbkkhoa2@gmail.com','123','role2'),('user3','fdaf','fdsaf','fdaf','role2'),('user4','fdf','fdsf','fdsf','role2'),('user5','fds','fds','fds','role2'),('user6','342','4324','24342','role2'),('user7','abc','fsfds','123','role2');
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

-- Dump completed on 2018-08-28  3:43:56
