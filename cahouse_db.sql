-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 06, 2018 at 04:08 PM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cahouse_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `ca_master`
--

CREATE TABLE `ca_master` (
  `id` int(10) NOT NULL,
  `fname` text NOT NULL,
  `lname` text NOT NULL,
  `email` text NOT NULL,
  `mobile` text NOT NULL,
  `password` text NOT NULL,
  `gcmtokenid` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ca_master`
--

INSERT INTO `ca_master` (`id`, `fname`, `lname`, `email`, `mobile`, `password`, `gcmtokenid`) VALUES
(1, 'Jahan', 'Gagan', 'jahangagan5@gmail.com', '8866204838', '123456', 'ekiTeV9qXhY:APA91bGmpQlOeKazEDfQd9kmgl6ZBI7wm6Bvn8XRDamCbyszCKde3P_U9fFJrQsX5SLfuX2Dlx9QjdtbIUjX7WyLKqhiFlC9oJCY4T9TkHYoeYIDsgfXRTgaIY8J48hz_LwCLaZ0EF_Z');

-- --------------------------------------------------------

--
-- Table structure for table `client_master`
--

CREATE TABLE `client_master` (
  `id` int(11) NOT NULL,
  `fname` text NOT NULL,
  `lname` text NOT NULL,
  `email` text NOT NULL,
  `mobile` text NOT NULL,
  `password` text NOT NULL,
  `gcmtokenid` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `client_master`
--

INSERT INTO `client_master` (`id`, `fname`, `lname`, `email`, `mobile`, `password`, `gcmtokenid`) VALUES
(11, 'fzn', 'gagan', 'fzn@gmail.com', '9537497106', '9d4d4ab0dfdb72a54b895d78b90b09c7', 'dA0LLadAmYE:APA91bHjmFDqVTf9t-2YDh-fiX_mE9ickN4sV7atPTs0Pal2gV3LBRnWhnwuo_tsE2XrRLSecLsVMnLk2hBa43W-AnwNFZGpVId0nl2lUSbVCwmrX91TC7InANw4SoP_X3ZMIqyoazB0');

-- --------------------------------------------------------

--
-- Table structure for table `document_master`
--

CREATE TABLE `document_master` (
  `document_id` int(10) NOT NULL,
  `document_name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `document_master`
--

INSERT INTO `document_master` (`document_id`, `document_name`) VALUES
(2, 'Aadhar Card.'),
(3, 'GST Number'),
(6, 'Driving Licence.'),
(7, 'Passport Size Photo (X2)'),
(8, 'Property Papers'),
(9, 'Bank Statements'),
(10, 'Bill Book'),
(12, 'Tax Payment Receipt'),
(13, 'PAN Card');

-- --------------------------------------------------------

--
-- Table structure for table `feedback_master`
--

CREATE TABLE `feedback_master` (
  `feedback_id` int(10) NOT NULL,
  `name` text NOT NULL,
  `email` text NOT NULL,
  `feedbackmsg` text NOT NULL,
  `feedbackdate` date NOT NULL,
  `feedbacktime` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `feedback_master`
--

INSERT INTO `feedback_master` (`feedback_id`, `name`, `email`, `feedbackmsg`, `feedbackdate`, `feedbacktime`) VALUES
(6, 'Stbs College(Diploma)', 'info@stbs.com', 'Awesome Application', '2018-04-22', '22:41:10');

-- --------------------------------------------------------

--
-- Table structure for table `forum_answer_master`
--

CREATE TABLE `forum_answer_master` (
  `id` int(10) NOT NULL,
  `questionid` int(10) NOT NULL,
  `cid` int(10) NOT NULL,
  `answer` text NOT NULL,
  `answerby` text NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `forum_answer_master`
--

INSERT INTO `forum_answer_master` (`id`, `questionid`, `cid`, `answer`, `answerby`, `date`) VALUES
(36, 7, 2, 'The GST replaces numerous different indirect taxes such as:\n1. Central Exice Duty\n2. Service Tax\n3. Central Sales Tax\nand many more', 'Stbs College(Diploma)', '2018-04-22'),
(37, 7, 1, 'Yes you are correct', 'Jahan Gagan', '2018-04-22');

-- --------------------------------------------------------

--
-- Table structure for table `forum_question_master`
--

CREATE TABLE `forum_question_master` (
  `id` int(10) NOT NULL,
  `cid` int(10) NOT NULL,
  `question` text NOT NULL,
  `questionby` text NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `forum_question_master`
--

INSERT INTO `forum_question_master` (`id`, `cid`, `question`, `questionby`, `date`) VALUES
(7, 2, 'What are the taxes that GST replaces?', 'Stbs College(Diploma)', '2018-04-22');

-- --------------------------------------------------------

--
-- Table structure for table `news_master`
--

CREATE TABLE `news_master` (
  `news_id` int(10) NOT NULL,
  `news_title` text NOT NULL,
  `news_description` text NOT NULL,
  `news_date` date NOT NULL,
  `news_time` time NOT NULL,
  `news_status` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `news_master`
--

INSERT INTO `news_master` (`news_id`, `news_title`, `news_description`, `news_date`, `news_time`, `news_status`) VALUES
(11, 'Manufacturers May Soon Find Themselves In A GST Tax Pickle', 'Manufacturers across the country may be soon be starting at credit reversal notices from the tax department', '2018-04-22', '22:32:38', 'Active');

-- --------------------------------------------------------

--
-- Table structure for table `request_admin_master`
--

CREATE TABLE `request_admin_master` (
  `ca_id` int(10) NOT NULL,
  `cid` int(10) NOT NULL,
  `request_id` int(10) NOT NULL,
  `document_id` int(10) NOT NULL,
  `date` date NOT NULL,
  `requestby` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `request_admin_master`
--

INSERT INTO `request_admin_master` (`ca_id`, `cid`, `request_id`, `document_id`, `date`, `requestby`, `status`) VALUES
(1, 2, 18, 7, '2018-04-22', 'Jahan Gagan', 0),
(1, 2, 19, 9, '2018-04-22', 'Jahan Gagan', 0),
(1, 2, 20, 10, '2018-04-22', 'Jahan Gagan', 1);

-- --------------------------------------------------------

--
-- Table structure for table `request_client_master`
--

CREATE TABLE `request_client_master` (
  `request_id` int(10) NOT NULL,
  `cid` int(10) NOT NULL,
  `document_id` int(10) NOT NULL,
  `date` date NOT NULL,
  `requestby` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `upload_admin_master`
--

CREATE TABLE `upload_admin_master` (
  `id` int(10) NOT NULL,
  `ca_id` int(10) NOT NULL,
  `cid` int(10) NOT NULL,
  `filename` text NOT NULL,
  `uploadedby` text NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `upload_admin_master`
--

INSERT INTO `upload_admin_master` (`id`, `ca_id`, `cid`, `filename`, `uploadedby`, `date`) VALUES
(3, 1, 5, 'IMG-20180420-WA0004.jpg', 'Jahan Gagan', '2018-04-20'),
(4, 1, 1, 'images-2.jpg', 'Jahan Gagan', '2018-04-20'),
(5, 1, 8, 'IMG-20180327-WA0000.jpg', 'Jahan Gagan', '2018-04-21'),
(6, 1, 8, 'IMG-20180214-WA0004.jpg', 'Jahan Gagan', '2018-04-21'),
(7, 1, 8, 'IMG-20180211-WA0015.jpg', 'Jahan Gagan', '2018-04-21'),
(8, 1, 1, 'IMG-20180421-WA0006.jpg', 'Jahan Gagan', '2018-04-21');

-- --------------------------------------------------------

--
-- Table structure for table `upload_client_master`
--

CREATE TABLE `upload_client_master` (
  `id` int(10) NOT NULL,
  `cid` int(10) NOT NULL,
  `filename` text NOT NULL,
  `uploadedby` text NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `upload_client_master`
--

INSERT INTO `upload_client_master` (`id`, `cid`, `filename`, `uploadedby`, `date`) VALUES
(19, 3, 'AJAVA_Unit-3_03052016_063146AM.pdf', 'Google College', '2018-04-20'),
(20, 1, 'images.jpg', 'Raksh Infotech', '2018-04-20'),
(21, 8, 'IMG-20180214-WA0007.jpg', 'TJG TJG', '2018-04-21'),
(22, 8, 'IMG-20180214-WA0007.jpg', 'TJG TJG', '2018-04-21'),
(23, 8, 'IMG-20180214-WA0002.jpg', 'TJG TJG', '2018-04-21'),
(24, 8, 'IMG-20180214-WA0002.jpg', 'TJG TJG', '2018-04-21'),
(25, 1, 'AJAVA_Unit-1_03052016_063014AM.pdf', 'Raksh Infotech', '2018-04-21');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ca_master`
--
ALTER TABLE `ca_master`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `client_master`
--
ALTER TABLE `client_master`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `document_master`
--
ALTER TABLE `document_master`
  ADD PRIMARY KEY (`document_id`);

--
-- Indexes for table `feedback_master`
--
ALTER TABLE `feedback_master`
  ADD PRIMARY KEY (`feedback_id`);

--
-- Indexes for table `forum_answer_master`
--
ALTER TABLE `forum_answer_master`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `forum_question_master`
--
ALTER TABLE `forum_question_master`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `news_master`
--
ALTER TABLE `news_master`
  ADD PRIMARY KEY (`news_id`);

--
-- Indexes for table `request_admin_master`
--
ALTER TABLE `request_admin_master`
  ADD PRIMARY KEY (`request_id`);

--
-- Indexes for table `request_client_master`
--
ALTER TABLE `request_client_master`
  ADD PRIMARY KEY (`request_id`);

--
-- Indexes for table `upload_admin_master`
--
ALTER TABLE `upload_admin_master`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `upload_client_master`
--
ALTER TABLE `upload_client_master`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ca_master`
--
ALTER TABLE `ca_master`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `client_master`
--
ALTER TABLE `client_master`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `document_master`
--
ALTER TABLE `document_master`
  MODIFY `document_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `feedback_master`
--
ALTER TABLE `feedback_master`
  MODIFY `feedback_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `forum_answer_master`
--
ALTER TABLE `forum_answer_master`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;
--
-- AUTO_INCREMENT for table `forum_question_master`
--
ALTER TABLE `forum_question_master`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `news_master`
--
ALTER TABLE `news_master`
  MODIFY `news_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `request_admin_master`
--
ALTER TABLE `request_admin_master`
  MODIFY `request_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
--
-- AUTO_INCREMENT for table `request_client_master`
--
ALTER TABLE `request_client_master`
  MODIFY `request_id` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `upload_admin_master`
--
ALTER TABLE `upload_admin_master`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `upload_client_master`
--
ALTER TABLE `upload_client_master`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
