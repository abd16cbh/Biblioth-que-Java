-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 21, 2024 at 10:09 PM
-- Server version: 5.7.24
-- PHP Version: 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `librairie-java-almabouada-abdennour`
--

-- --------------------------------------------------------

--
-- Table structure for table `adherent`
--

CREATE TABLE `adherent` (
  `adhnum` int(11) NOT NULL,
  `nom` varchar(42) DEFAULT NULL,
  `prenom` varchar(42) DEFAULT NULL,
  `email` varchar(42) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `adherent`
--

INSERT INTO `adherent` (`adhnum`, `nom`, `prenom`, `email`) VALUES
(15, 'Almabouada', 'Abdennour', 'abdennour@sio.fr'),
(4, 'Bah', 'Djenaba', 'djenaba@bah.fr'),
(7, 'Ouakrim', 'Hanane', 'hanane@ouak.fr'),
(10, 'Osseni', 'Semiyou', 'semi@osen.fr');

-- --------------------------------------------------------

--
-- Table structure for table `auteur`
--

CREATE TABLE `auteur` (
  `autnum` int(11) NOT NULL,
  `nom` varchar(42) DEFAULT NULL,
  `prenom` varchar(42) DEFAULT NULL,
  `date_naissance` varchar(42) DEFAULT NULL,
  `description` varchar(42) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `auteur`
--

INSERT INTO `auteur` (`autnum`, `nom`, `prenom`, `date_naissance`, `description`) VALUES
(1, 'Bah', 'Ibrahima ', '15/04/2004', 'Auteur de la negritude'),
(2, 'Sadji', 'Abdoulaye', '01/01/1901', 'Auteur senegalais'),
(3, 'Pascal', 'Blaise', '19/06/1623', 'Scientifique français'),
(4, 'Camus', 'Albert ', '01/01/1800', 'Ecrivain français'),
(5, 'Zola', 'Emile', '10/01/1854', 'Ecrivain français'),
(6, 'Rowling', 'JK', '10/10/1960', 'Ecrivaine anglaise'),
(7, 'Toriyama', 'Akira', '10/10/1950', 'Mangaka japonais'),
(8, 'Almabouada', 'Abdennour', '18/05/2003', 'Auteur algérien');

-- --------------------------------------------------------

--
-- Table structure for table `emprunts`
--

CREATE TABLE `emprunts` (
  `id_emprunt` int(11) NOT NULL,
  `id_adherent` int(11) DEFAULT NULL,
  `id_livre` int(11) DEFAULT NULL,
  `date_emprunt` date DEFAULT NULL,
  `date_retour` date DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `emprunts`
--

INSERT INTO `emprunts` (`id_emprunt`, `id_adherent`, `id_livre`, `date_emprunt`, `date_retour`) VALUES
(23, 1, 1, '2024-03-19', '2024-04-02'),
(25, 15, 10, '2024-04-21', '2024-05-05');

-- --------------------------------------------------------

--
-- Table structure for table `livre`
--

CREATE TABLE `livre` (
  `ISBN` int(11) NOT NULL,
  `titre` varchar(42) DEFAULT NULL,
  `prix` varchar(42) DEFAULT NULL,
  `autnum_1` varchar(42) NOT NULL,
  `disponibilite` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `livre`
--

INSERT INTO `livre` (`ISBN`, `titre`, `prix`, `autnum_1`, `disponibilite`) VALUES
(1, 'Pensées', '9,99€', '3', 10),
(6, 'Maimouna', '9,99€', '2', 7),
(7, 'Le joueur d\'echec', '9,99', '4', 5),
(8, 'Germinal', '9,99€', '5', 2),
(9, 'DBZ', '6,99€', '7', 5),
(10, 'Road Trip en Algérie', '15€', '8', 24);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `adherent`
--
ALTER TABLE `adherent`
  ADD PRIMARY KEY (`adhnum`);

--
-- Indexes for table `auteur`
--
ALTER TABLE `auteur`
  ADD PRIMARY KEY (`autnum`);

--
-- Indexes for table `emprunts`
--
ALTER TABLE `emprunts`
  ADD PRIMARY KEY (`id_emprunt`),
  ADD KEY `id_adherent` (`id_adherent`),
  ADD KEY `fk_id_livre` (`id_livre`);

--
-- Indexes for table `livre`
--
ALTER TABLE `livre`
  ADD PRIMARY KEY (`ISBN`),
  ADD KEY `autnum_1` (`autnum_1`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `adherent`
--
ALTER TABLE `adherent`
  MODIFY `adhnum` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `auteur`
--
ALTER TABLE `auteur`
  MODIFY `autnum` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `emprunts`
--
ALTER TABLE `emprunts`
  MODIFY `id_emprunt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `livre`
--
ALTER TABLE `livre`
  MODIFY `ISBN` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
