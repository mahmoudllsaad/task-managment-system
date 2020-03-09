-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Mar 09, 2020 at 02:31 AM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `task_managment_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `rules`
--

CREATE TABLE IF NOT EXISTS `rules` (
  `RULES_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`RULES_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `rules`
--

INSERT INTO `rules` (`RULES_ID`, `NAME`) VALUES
(1, 'Admin'),
(2, 'Agent');

-- --------------------------------------------------------

--
-- Table structure for table `status`
--

CREATE TABLE IF NOT EXISTS `status` (
  `STATUS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`STATUS_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `status`
--

INSERT INTO `status` (`STATUS_ID`, `NAME`) VALUES
(5, 'Opened'),
(6, 'Closed'),
(7, 'In Progress');

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE IF NOT EXISTS `task` (
  `TASK_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `USER_NAME` varchar(100) DEFAULT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TASK_ID`),
  KEY `FK_USERNAME` (`USER_NAME`),
  KEY `FK_STATUSID` (`STATUS_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`TASK_ID`, `NAME`, `DESCRIPTION`, `USER_NAME`, `STATUS_ID`) VALUES
(1, 'Create New Registration Window', 'Using java fx framework', 'AgentUser1', 5);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `USER_NAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `USER_EMAIL` varchar(50) DEFAULT NULL,
  `USER_PHONE` int(11) DEFAULT NULL,
  `RULES_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`USER_NAME`),
  KEY `FK_RULESNAME` (`RULES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`USER_NAME`, `PASSWORD`, `USER_EMAIL`, `USER_PHONE`, `RULES_ID`) VALUES
('AdminUser1', 'wTv1FZ7jGVYnBoBI5sGd8jEMv2XEtwNgooBT9UWp4xE=-7Qfa8jJGvsteJw8fWmK4t0Ujf3hWOe', 'AdminUser@Admin.com', 123455873, 1),
('AgentUser1', 'wYY9t6GxsSrkW3BB5RBaWOTscPUbZJ2xmvEKtsIrNXs=-2l1aMs0w68SQbZzCHmusWj72wpq0TH', 'AgentUser@Agent.com', 123655874, 2);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `FK_STATUSID` FOREIGN KEY (`STATUS_ID`) REFERENCES `status` (`STATUS_ID`),
  ADD CONSTRAINT `FK_USERNAME` FOREIGN KEY (`USER_NAME`) REFERENCES `user` (`USER_NAME`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FK_RULESNAME` FOREIGN KEY (`RULES_ID`) REFERENCES `rules` (`RULES_ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
