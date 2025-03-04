-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 04-03-2025 a las 08:54:59
-- Versión del servidor: 10.3.13-MariaDB
-- Versión de PHP: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `rbppaperlesshalfpr`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `Aaruth 1` (IN `mg` VARCHAR(20), OUT `id_r` INT)  NO SQL BEGIN
DECLARE id_m int;

SET id_m=(SELECT mog.id_mog FROM mog WHERE mog.mog=CONCAT('MOG0','',mg));

set id_r=(select registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.mog_id_mog=id_m and registro_rbp.proceso='PLATINADO');

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `abrirSupervisor` (IN `code_emp_rev` VARCHAR(20), IN `code_emp_apro` VARCHAR(20), IN `orden` VARCHAR(20), OUT `val` INT)  NO SQL BEGIN

DECLARE idMog int;
DECLARE idEmpRev int;
DECLARE idEmpApro int;

set idMog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

set idEmpRev=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo=code_emp_rev and empleado.tipo_usuario='Aduana');

set idEmpApro=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo_alea like code_emp_apro and empleado.tipo_usuario='Aduana');

if(idEmpApro is null)THEN
set val=0;
ELSEIF (idEmpRev is null) THEN
set val=0;
ELSE
UPDATE `registro_rbp` SET `estado`=1 ,`aduana`=1 WHERE registro_rbp.orden_manufactura=orden;

INSERT INTO `ordenes_abiertas_asupervisor`( `empleado_revision`, `empleado_aprobacion`, `orden_abierta`, `mog_id_mog`, `fecha_apertura`, `hora_apertura`, `activo`) VALUES (idEmpRev,idEmpApro,orden,idMog,CURDATE(),CURTIME(),1);

set val=1;
end if;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaDasBGAssy_S` (IN `idpiezas` INT, IN `ordenMa` VARCHAR(20), IN `piezaG` INT, IN `piezaPro` INT, IN `scrap` INT, IN `scrapRG` INT)  NO SQL BEGIN

DECLARE idmog int;
DECLARE idas int;
DECLARE proceRG int;

SET idmog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=ordenMa);

set idas=(SELECT piezas_procesadas.dasiddas from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpiezas);

if(idas is not null) THEN

SET proceRG=(piezaG+scrapRG);

UPDATE `das_prod_bgproceso` SET `pcs_pro_bush`=piezaPro,`pcs_buen_bush`=piezaG,`scrap_bush`=scrap, 
`pcs_pro_rg`=proceRG,`pcs_buen_rg`=piezaG,`scrp_rg`=scrapRG WHERE `das_prod_bgproceso`.`das_iddas`=idas and `das_prod_bgproceso`.`mog_idmog`=idmog; 

END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaDasBGPCK_S` (IN `ordenMa` VARCHAR(20), IN `idpiezas` INT, IN `piezaG` INT, IN `piezaPro` INT, IN `scrap` INT, IN `sobF` INT)  NO SQL BEGIN

DECLARE idmog int;
DECLARE idas int;

SET idmog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=ordenMa);

set idas=(SELECT piezas_procesadas.dasiddas from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpiezas);

if (idas is not null) THEN

UPDATE `das_produ_empamesas` SET `cant_proce`=piezaPro,`pza_buenas`=piezaG,`pza_Sort`=scrap,`Sobrante_Final`=sobF
WHERE `das_produ_empamesas`.`das_iddas`=idas and `das_produ_empamesas`.`mog_idmog`=idmog; 
END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaDasBGPrensa_S` (IN `idpiezas` INT, IN `ordenMa` VARCHAR(20), IN `piezaG` INT, IN `piezaPro` INT, IN `scrapBM` INT, IN `scrapOtros` INT, IN `idDas2` INT)  NO SQL BEGIN

DECLARE idmog int;
DECLARE idas int;

SET idmog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=ordenMa);

set idas=(SELECT piezas_procesadas.dasiddas from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpiezas);

IF (idas is not null) THEN

UPDATE `das_prod_bgprensa` SET `pcs_pro`=piezaPro,`pcs_buenas`=piezaG, `pcs_scrap`= scrapOtros,`pcs_bm`=scrapBM
WHERE `das_prod_bgprensa`.`das_id_das`=idas and `das_prod_bgprensa`.`mog_idmog`=idmog and `das_prod_bgprensa`.`id_dasprodbgp`=idDas2; 

    end if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaDasBGProce_S` (IN `idpiezas` INT, IN `ordenMa` VARCHAR(20), IN `piezaG` INT, IN `piezaPro` INT, IN `scrap` INT)  NO SQL BEGIN

DECLARE idmog int;
DECLARE idas int;

SET idmog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=ordenMa);

set idas=(SELECT piezas_procesadas.dasiddas from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpiezas);

UPDATE `das_prod_bgproceso` SET `pcs_pro_bush`=piezaPro,`pcs_buen_bush`=piezaG,`scrap_bush`=scrap 
WHERE `das_prod_bgproceso`.`das_iddas`=idas and `das_prod_bgproceso`.`mog_idmog`=idmog; 

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaDasBGSR_S` (IN `idpiezas` INT, IN `ordenMa` VARCHAR(30), IN `piezaG` INT, IN `piezaPro` INT, IN `scrap` INT, IN `scrapRG` INT, IN `scrapSR` INT)  NO SQL BEGIN

DECLARE idmog int;
DECLARE idas int;
DECLARE proceRG int;
DECLARE proceSR int;

SET idmog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=ordenMa);

set idas=(SELECT piezas_procesadas.dasiddas from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpiezas);

if(idas is not null) THEN

SET proceRG=(piezaG+scrapRG);
SET proceSR=(piezaG+scrapSR);

UPDATE `das_prod_bgproceso` SET `pcs_pro_bush`=piezaPro,`pcs_buen_bush`=piezaG,`scrap_bush`=scrap, 
`pcs_pro_rg`=proceRG,`pcs_buen_rg`=piezaG,`scrp_rg`=scrapRG, `pcs_pro_sr`=proceSR,`pcs_buen_sr`=piezaG,`scrap_sr`= scrapSR WHERE `das_prod_bgproceso`.`das_iddas`=idas and `das_prod_bgproceso`.`mog_idmog`=idmog; 

END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaDasPrensa_S` (IN `idpiezas` INT, IN `ordenMa` VARCHAR(20), IN `piezaG` INT, IN `idDas2` INT)  NO SQL BEGIN
DECLARE idmog int;
DECLARE idas int;
DECLARE total int DEFAULT 0;
DECLARE pcsBM int;
DECLARE pcsNG int;

SET idmog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=ordenMa);

set idas=(SELECT piezas_procesadas.dasiddas from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpiezas);

IF (idas is not null) THEN

set pcsBM=(SELECT das_prod_pren.pcs_bm from das_prod_pren WHERE das_prod_pren.das_iddas=idas and `das_prod_pren`.`mog_idmog`=idmog and `das_prod_pren`.`id_daspropren`=idDas2);

set pcsNG=(SELECT das_prod_pren.pcs_ng from das_prod_pren WHERE das_prod_pren.das_iddas=idas and `das_prod_pren`.`mog_idmog`=idmog and `das_prod_pren`.`id_daspropren`=idDas2);

SET total=(pcsBM+pcsNG+piezaG);

UPDATE `das_prod_pren` SET `pzasTotales`=total,`pza_ok`=piezaG WHERE  das_prod_pren.das_iddas=idas and `das_prod_pren`.`mog_idmog`=idmog and `das_prod_pren`.`id_daspropren`=idDas2; 

 end if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaEstadoAduana` (IN `orden_manu` VARCHAR(30))  NO SQL BEGIN
UPDATE `registro_rbp` SET `aduana`=0 WHERE orden_manufactura = orden_manu;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaEstadoRBP` (IN `orden_manu` VARCHAR(30))   BEGIN
UPDATE `registro_rbp` SET `estado`=0 WHERE orden_manufactura = orden_manu;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaEstadoS` (IN `orden_manu` VARCHAR(20))  NO SQL BEGIN
UPDATE `registro_rbp` SET  registro_rbp.estado=0 WHERE orden_manufactura = orden_manu;

UPDATE `ordenes_abiertas_asupervisor` SET `activo`=0 WHERE ordenes_abiertas_asupervisor.orden_abierta=orden_manu and ordenes_abiertas_asupervisor.activo=1;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaMetrosMaterial` (IN `id` INT, IN `lot` VARCHAR(20), IN `mts` INT, IN `orden1` VARCHAR(20), IN `id2` INT)  NO SQL BEGIN

DECLARE idMOG int;

SET idMOG=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=orden1);


UPDATE `das_prod_bgprensa` SET `num_lot_mat`=lot,`metros`=mts WHERE `das_id_das`=id and das_prod_bgprensa.mog_idmog=idMOG and das_prod_bgprensa.id_dasprodbgp=id2;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaPiezasPro` (IN `orden_manu` VARCHAR(30), IN `piezasCanastaCom` INT, IN `NCanastasCompl` INT, IN `totalPiezApro` INT, IN `verificacion` INT)   BEGIN 
DECLARE id_registro int DEFAULT 0;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);
UPDATE `piezas_procesadas_fg` SET `total_piezas_aprobadas`=totalPiezApro,`verificacion`=verificacion,`num_canasta_completa`=NCanastasCompl,`pza_canasta_completa`=piezasCanastaCom WHERE registro_rbp_id_registro_rbp = id_registro;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaPiezasProTab_S` (IN `orden_manu` VARCHAR(30), IN `cantPiezasPro` INT, IN `canast` INT)   BEGIN
DECLARE id_registro int DEFAULT 0;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);
UPDATE `piezas_procesadas` SET `cantidad_piezas_procesadas`=cantPiezasPro WHERE registro_rbp_id_registro_rbp =id_registro and rango_canasta_1 = canast;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarCausasParo` (IN `idCausa` INT, IN `detalle2` VARCHAR(255), IN `tiempo2` INT, IN `causa` INT)  NO SQL UPDATE `registrocausasparo` SET `causas_paro_idcausas_paro`=causa,`tiempo`=tiempo2,`detalle`=detalle2 WHERE idregistrocausasparo=idCausa$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `actualizarDas` (IN `id` INT)  NO SQL BEGIN

UPDATE `das` SET activaOperador=0 WHERE id_das=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarEstado` (IN `idppfg` INT)  NO SQL BEGIN

DECLARE id int;

SET id=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp INNER JOIN piezas_procesadas_fg ON 
piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE 
piezas_procesadas_fg.id_piezas_procesadas_fg=idppfg);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarMetrosTotalesDas` (IN `id` INT, IN `mtr` INT, IN `lot` VARCHAR(50))  NO SQL UPDATE `das_prod_bgprensa` SET `metros`=mtr, das_prod_bgprensa.num_lot_mat=lot WHERE das_prod_bgprensa.id_dasprodbgp=id$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarMetrosTotalesDasPrensa` (IN `id` INT, IN `mtr` INT, IN `lot` VARCHAR(50))  NO SQL UPDATE `das_prod_pren` SET `lot_material`=lot,`metros`=mtr WHERE das_prod_pren.id_daspropren=id$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarScrapPorNivel` (IN `orden_manu` VARCHAR(20), IN `id_razon` INT, IN `cantidad` INT, IN `colum` INT)  NO SQL BEGIN 
DECLARE id_registro int DEFAULT 0;
DECLARE exist int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SET exist=(SELECT defecto.razon_rechazo_id_razon_rechazo FROM defecto WHERE defecto.registro_rbp_id_registro_rbp=id_registro AND defecto.razon_rechazo_id_razon_rechazo=id_razon 
and columna = colum);

if(exist > 0) THEN
UPDATE `defecto` SET `cantidad_defecto`=cantidad
WHERE registro_rbp_id_registro_rbp = id_registro AND razon_rechazo_id_razon_rechazo = id_razon and columna=colum;
ELSE
INSERT INTO `defecto`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`) VALUES (id_registro,id_razon,cantidad,colum);
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarScrapPorNivel1` (IN `orden_manu` VARCHAR(20), IN `id_razon` INT, IN `cantidad` INT, IN `colum` INT)  NO SQL BEGIN 
DECLARE id_registro int DEFAULT 0;
DECLARE exist int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SET exist=(SELECT defecto1.razon_rechazo_id_razon_rechazo FROM defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=id_registro AND defecto1.razon_rechazo_id_razon_rechazo=id_razon 
and columna = colum);

if(exist > 0) THEN
UPDATE `defecto1` SET `cantidad_defecto`=cantidad
WHERE registro_rbp_id_registro_rbp = id_registro AND razon_rechazo_id_razon_rechazo = id_razon and columna=colum;
ELSE
INSERT INTO `defecto1`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`) VALUES (id_registro,id_razon,cantidad,colum);
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarStatusSuper` (IN `orden` VARCHAR(20))  NO SQL BEGIN
UPDATE registro_rbp set registro_rbp.estado=0 WHERE registro_rbp.orden_manufactura=orden;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarTiempo` (IN `orden` VARCHAR(20), IN `hora1` VARCHAR(20), IN `tur` INT, IN `hora2` VARCHAR(20), IN `horaT` VARCHAR(20), IN `fec` DATE)  NO SQL BEGIN
DECLARE id int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

UPDATE `tiempo` SET `turno`=tur,`hora_fin`=hora2,`horas_trabajadas`=horaT WHERE tiempo.hora_inicio=hora1 and tiempo.fecha=fec and tiempo.registro_rbp_id_registro_rbp=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizarTM` (IN `orden` VARCHAR(20), IN `loteT` VARCHAR(50))  NO SQL UPDATE `registro_rbp` SET `loteTM`= loteT WHERE registro_rbp.orden_manufactura=orden$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizar_estado_sorting` (IN `orden` VARCHAR(20))  NO SQL UPDATE `registro_rbp` SET `sortingSupervisor`=0 WHERE orden_manufactura=orden$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizar_por_turno_std` (IN `std1` VARCHAR(10), IN `canastas` INT, IN `sobra_ini` INT, IN `pzafila` INT, IN `filas1` INT, IN `nivel1` INT, IN `nivel_comp` INT, IN `filas_comp` INT, IN `sobrante` INT, IN `cant_pzabuena` INT, IN `id_pro` INT)  NO SQL BEGIN 

DECLARE id_registro int DEFAULT 0;

UPDATE `piezas_procesadas_grading` SET `canastas`=canastas, `filas_completas`=filas_comp, `piezasxfila`= pzafila, `filas`=filas1, `nivel`=nivel1, `niveles_completos`=nivel_comp, `sobrante`=sobrante, `cant_piezas_buenas`=cant_pzabuena, `sobrante_inicial`=sobra_ini
WHERE piezas_procesadas_grading.stdE=std1 AND 
piezas_procesadas_grading.id_piezasProcesadas = id_pro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizar_rangos_bandeja` (IN `orden_manu` VARCHAR(30), IN `id_pie_pro` INT, IN `rang_1` INT, IN `rang_2` INT)   BEGIN

DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

UPDATE `piezas_procesadas` SET `rango_canasta_1`=rang_1,`rango_canasta_2`=rang_2 WHERE registro_rbp_id_registro_rbp = id_registro AND idpiezas_procesadas = id_pie_pro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizar_sobra_inicial_std` (IN `std1` VARCHAR(10), IN `id_pipro` INT, IN `sobrante_ini` INT)  NO SQL BEGIN

UPDATE `piezas_procesadas_grading` SET `sobrante_inicial`=sobrante_ini WHERE piezas_procesadas_grading.stdE=std1 AND 
piezas_procesadas_grading.id_piezasProcesadas = id_pipro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualizaVeri2` (IN `totalV` INT, IN `orden` VARCHAR(20))  NO SQL BEGIN

DECLARE id_or int;

SET id_or=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);


UPDATE  `piezas_procesadas_fg` SET `verificacion2`=totalV WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=id_or;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_lotel_coil` (IN `orden_manu` VARCHAR(30), IN `id_lote` INT, IN `lote` VARCHAR(30), IN `mtrs` INT, IN `ft` INT)   BEGIN
DECLARE id_registro int;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

UPDATE `lote_coil` SET `lote_coil`=lote,`metros`=mtrs,`f_terminado`=ft WHERE registro_rbp_id_registro_rbp = id_registro AND id_lote_coil = id_lote;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_piezas_pro_fg` (IN `orden_manu` VARCHAR(30), IN `piezas_p_bandeja_com` INT, IN `no_bandeja_completas` INT, IN `cantidad_piezas_bandeja_incom` INT, IN `total_piezas_aprobadas` INT, IN `total_piezas_prensadas` INT, IN `cantidad_nueva_mog` INT, IN `veri` INT)   BEGIN 

DECLARE id_registro int DEFAULT 0;
DECLARE scrap int;
declare totalpro int;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);


set scrap=(SELECT SUM(cantidad_defecto) FROM `defecto1` WHERE registro_rbp_id_registro_rbp=id_registro);

if(scrap is not null)THEN
SET totalpro=(scrap+total_piezas_aprobadas);
ELSE
SET totalpro=(total_piezas_aprobadas);
END IF;

UPDATE `piezas_procesadas_fg` SET `total_piezas_aprobadas`=total_piezas_aprobadas,`cambio_mog`=cantidad_nueva_mog,`sobrante_final`=cantidad_piezas_bandeja_incom,
`num_canasta_completa`=no_bandeja_completas,`pza_canasta_completa`=piezas_p_bandeja_com,`totalpiezas_procesadas`=totalpro, `verificacion`=veri
WHERE registro_rbp_id_registro_rbp = id_registro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_piezas_pro_fg_empaque` (IN `orden_manu` VARCHAR(30), IN `piezas_p_bandeja_com` INT, IN `no_bandeja_completas` INT, IN `cantidad_piezas_bandeja_incom` INT, IN `total_piezas_aprobadas` INT, IN `total_piezas_prensadas` INT, IN `cantidad_nueva_mog` INT, IN `veri` INT)   BEGIN 

DECLARE id_registro int DEFAULT 0;
DECLARE scrap int;
declare totalpro int;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);


set scrap=(SELECT SUM(cantidad_defecto) FROM `defecto1` WHERE registro_rbp_id_registro_rbp=id_registro);

if(scrap is not null)THEN
SET totalpro=(scrap+total_piezas_aprobadas);
ELSE
SET totalpro=(total_piezas_aprobadas);
END IF;

UPDATE `piezas_procesadas_fg` SET `total_piezas_aprobadas`=total_piezas_aprobadas,`cambio_mog`=cantidad_nueva_mog,`sobrante_final`=cantidad_piezas_bandeja_incom,
`num_canasta_completa`=no_bandeja_completas,`pza_canasta_completa`=piezas_p_bandeja_com,`totalpiezas_procesadas`=total_piezas_prensadas, `verificacion`=veri
WHERE registro_rbp_id_registro_rbp = id_registro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_piezas_pro_fg_maq` (IN `orden_manu` VARCHAR(30), IN `piezas_p_bandeja_com` INT, IN `no_bandeja_completas` INT, IN `cantidad_piezas_bandeja_incom` INT, IN `total_piezas_aprobadas` INT, IN `total_piezas_prensadas` INT, IN `cantidad_nueva_mog` INT, IN `veri` INT, IN `veri2` INT)   BEGIN 

DECLARE id_registro int DEFAULT 0;
DECLARE scrap int;
declare totalpro int;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);


set scrap=(SELECT SUM(cantidad_defecto) FROM `defecto1` WHERE registro_rbp_id_registro_rbp=id_registro);

if(scrap is not null)THEN
SET totalpro=(scrap+total_piezas_aprobadas);
ELSE
SET totalpro=(total_piezas_aprobadas);
END IF;

UPDATE `piezas_procesadas_fg` SET `total_piezas_aprobadas`=total_piezas_aprobadas,`cambio_mog`=cantidad_nueva_mog,`sobrante_final`=cantidad_piezas_bandeja_incom,
`num_canasta_completa`=no_bandeja_completas,`pza_canasta_completa`=piezas_p_bandeja_com,`totalpiezas_procesadas`=totalpro, `verificacion`=veri, verificacion2=veri2
WHERE registro_rbp_id_registro_rbp = id_registro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_piezas_recibidas` (IN `manu` VARCHAR(20), IN `qty` INT)  NO SQL UPDATE piezas_procesadas_fg SET piezas_procesadas_fg.total_piezas_recibidas = qty WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=manu)$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_por_turno_op` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT, IN `canastas` INT, IN `niveles_com` INT, IN `filas_com` INT, IN `sobrante` INT, IN `cambio_mog` INT, IN `cantidad_piezas_p` INT, IN `cant_piezas_good` INT, IN `totS` INT, IN `piexfil` INT, IN `fil` INT, IN `niv` INT)   BEGIN 
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

UPDATE `piezas_procesadas` SET `canastas`=canastas,`niveles_completos`=niveles_com,`filas_completas`=filas_com,
`cambioMOG`=cambio_mog, piezas_procesadas.sobra_fin=totS,
`sobrante`= sobrante, piezas_procesadas.piezasxfila=piexfil, piezas_procesadas.filas=fil, piezas_procesadas.niveles=niv, cantidad_piezas_procesadas=cantidad_piezas_p, cantPzaGood=cant_piezas_good 
WHERE registro_rbp_id_registro_rbp = id_registro AND 
idpiezas_procesadas = id_pipro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_por_turno_s` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT, IN `canastas` INT, IN `niveles_com` INT, IN `filas_com` INT, IN `sobrante` INT, IN `cambio_mog` INT, IN `cantidad_piezas_p` INT, IN `cant_piezas_good` INT, IN `totS` INT)   BEGIN 
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

UPDATE `piezas_procesadas` SET `canastas`=canastas,`niveles_completos`=niveles_com,`filas_completas`=filas_com,
`cambioMOG`=cambio_mog, piezas_procesadas.sobra_fin=totS,
`sobrante`= sobrante, cantidad_piezas_procesadas=cantidad_piezas_p, cantPzaGood=cant_piezas_good 
WHERE registro_rbp_id_registro_rbp = id_registro AND 
idpiezas_procesadas = id_pipro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_pza_fila_s` (IN `cant` INT, IN `orden` VARCHAR(20), IN `id` INT)  NO SQL BEGIN

DECLARE id_orden int;

SET id_orden=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

UPDATE `piezas_procesadas` SET `piezasxfila`=cant WHERE piezas_procesadas.idpiezas_procesadas=id and piezas_procesadas.registro_rbp_id_registro_rbp=id_orden;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_scrap_por_nivel_s` (IN `orden_manu` VARCHAR(30), IN `id_razon` INT, IN `cantidad` INT, IN `colum` INT)   BEGIN 
DECLARE id_registro int DEFAULT 0;
DECLARE exist int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SET exist=(SELECT defecto1.razon_rechazo_id_razon_rechazo FROM defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=id_registro AND defecto1.razon_rechazo_id_razon_rechazo=id_razon 
and columna = colum and defecto1.columna_sorting is null);

if(exist > 0) THEN
UPDATE `defecto1` SET `cantidad_defecto`=cantidad
WHERE registro_rbp_id_registro_rbp = id_registro AND razon_rechazo_id_razon_rechazo = id_razon and columna=colum;
ELSE
if (cantidad!=0) then
INSERT INTO `defecto1`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`) VALUES (id_registro,id_razon,cantidad,colum);
END IF;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_scrap_por_nivel_s1` (IN `orden_manu` VARCHAR(30), IN `id_razon` INT, IN `cantidad` INT, IN `colum` INT)   BEGIN 
DECLARE id_registro int DEFAULT 0;
DECLARE exist int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SET exist=(SELECT defecto1.razon_rechazo_id_razon_rechazo FROM defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=id_registro AND defecto1.razon_rechazo_id_razon_rechazo=id_razon 
and columna = colum and defecto1.columna_sorting is null);

if(exist > 0) THEN
UPDATE `defecto1` SET `cantidad_defecto`=cantidad
WHERE registro_rbp_id_registro_rbp = id_registro AND razon_rechazo_id_razon_rechazo = id_razon and columna=colum;
ELSE
if (cantidad!=0) then
INSERT INTO `defecto1`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`) VALUES (id_registro,id_razon,cantidad,colum);
END IF;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_sobrante_inicial_s` (IN `orden_manu` VARCHAR(30), IN `sobrante_ini` INT, IN `id_pipro` INT)   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

UPDATE `piezas_procesadas` SET `sobrante_inicial`=sobrante_ini WHERE registro_rbp_id_registro_rbp = id_registro AND 
idpiezas_procesadas = id_pipro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `actualiza_total_scrap_s` (IN `orden_manu` VARCHAR(30), IN `cantidad` INT)   BEGIN
DECLARE id_registro int DEFAULT 0;
DECLARE sumadefec int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SET sumadefec=(SELECT SUM(defecto1.cantidad_defecto) FROM defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=id_registro and defecto1.columna_sorting is null);

if(sumadefec is null) THEN
UPDATE `piezas_procesadas_fg` SET `total_scrap` = 0 WHERE registro_rbp_id_registro_rbp = id_registro;
ELSE
UPDATE `piezas_procesadas_fg` SET `total_scrap` = sumadefec WHERE registro_rbp_id_registro_rbp = id_registro;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `act_das_prod_Prensa` (IN `material` VARCHAR(20), IN `anc` DOUBLE, IN `num_lote` VARCHAR(20), IN `mts` DOUBLE, IN `lot1` VARCHAR(20), IN `in1` VARCHAR(20), IN `fi1` VARCHAR(20), IN `ini2` VARCHAR(20), IN `fin2` VARCHAR(20), IN `pcpro` INT, IN `pcgood` INT, IN `pcscrap` INT, IN `pcbm` INT, IN `idas` INT, IN `centro` VARCHAR(10), IN `extre` VARCHAR(10), IN `sell` VARCHAR(10), IN `ngkg` DOUBLE, IN `bmkg` DOUBLE, IN `cod` VARCHAR(30))  NO SQL BEGIN
DECLARE id int;
DECLARE idMaxbgpre int;

SET id=(SELECT mog.id_mog from mog where mog.mog=cod);

set idMaxbgpre=(SELECT MAX(das_prod_pren.id_daspropren) from das_prod_pren WHERE das_prod_pren.mog_idmog=id and das_prod_pren.das_iddas=idas);


UPDATE `das_prod_pren` SET `material`=material,`corte_coiling`=anc, `lot_material`=num_lote, `metros`=mts, `inicio_cm`=in1, `fin_cm`=fi1, `inicio_tp`=fi1, `fin_tp`=fin2, `centroEstampado`=centro,`extremo`=extre, `sello`=sell, `pzasTotales`=pcpro, `bm_kg`=bmkg, `pcs_bm`=pcbm, `pza_ok`=pcgood, `ng_kg`=ngkg, `pcs_ng`=pcscrap WHERE das_prod_pren.id_daspropren=idMaxbgpre;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarIdMog` (IN `orden` VARCHAR(20))  NO SQL SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarIdsDasBgGeneral` (IN `id` INT, IN `mogg` VARCHAR(20))  NO SQL BEGIN

DECLARE line varchar(20);
DECLARE m int;

SET line=(SELECT piezas_procesadas.linea FROM piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=id);

SET m=(SELECT mog.id_mog FROM mog WHERE mog.mog=mogg);

SELECT das_prod_bgproceso.* FROM `das_prod_bgproceso` INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas WHERE das.linea = line AND mog_idmog=m;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarIdsDasBgPrensa` (IN `idm` INT)  NO SQL SELECT das_prod_bgprensa.id_dasprodbgp FROM das_prod_bgprensa WHERE das_prod_bgprensa.mog_idmog=idm$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarIdsDasPrensa` (IN `idm` INT)  NO SQL SELECT das_prod_pren.id_daspropren FROM das_prod_pren WHERE das_prod_pren.mog_idmog=idm$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarMOG` (IN `mogg` VARCHAR(50))   BEGIN
select mog, mog.cantidad_planeada from mog where mog.mog=mogg;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarMOG0` (IN `proce` VARCHAR(20))  NO SQL SELECT piezas_procesadas.linea, piezas_procesadas.registro_rbp_id_registro_rbp, piezas_procesadas.dasiddas FROM `piezas_procesadas` INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas.registro_rbp_id_registro_rbp WHERE registro_rbp.orden_manufactura LIKE '%000000%' AND registro_rbp.proceso = proce AND piezas_procesadas.activo = 1$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarMOG0Das` (IN `idda` INT, IN `idreg` INT, IN `proce` VARCHAR(30), IN `mognueva` VARCHAR(20))  NO SQL BEGIN

DECLARE mogid int;
DECLARE mognueva1 int;

DECLARE mogid1 int;

SET mognueva1=(SELECT mog.id_mog FROM mog WHERE mog.mog=mognueva);

SET mogid=(SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.id_registro_rbp=idreg);

UPDATE das_prod_bgproceso SET das_prod_bgproceso.mog_idmog=mognueva1 WHERE das_prod_bgproceso.das_iddas=idda and das_prod_bgproceso.mog_idmog=mogid;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarMOG2` (IN `mogg` VARCHAR(50))   BEGIN
select mog.mog ,mog.cantidad_planeada from mog where mog.mog=mogg;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `buscarOrder` (IN `mg` VARCHAR(20))  NO SQL BEGIN
DECLARE r varchar(20);

SET r=(SELECT registro_rbp.orden_manufactura FROM registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog WHERE mog=mg AND registro_rbp.orden_manufactura LIKE '%PCK%');

IF (r is NOT null) THEN

SELECT registro_rbp.orden_manufactura FROM registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog WHERE mog=mg AND registro_rbp.orden_manufactura LIKE '%PCK%';

END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `busquedaRegistrosDasBgPrensa` (IN `mogg` VARCHAR(20))  NO SQL BEGIN

DECLARE id int;

SET id = (SELECT mog.id_mog FROM mog WHERE mog.mog=mogg);

SELECT * from das_prod_bgprensa WHERE das_prod_bgprensa.mog_idmog=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `busquedaRegistrosDasPrensa` (IN `mogg` VARCHAR(30))  NO SQL BEGIN

DECLARE id int;

SET id = (SELECT mog.id_mog FROM mog WHERE mog.mog=mogg);

SELECT * from das_prod_pren WHERE das_prod_pren.mog_idmog=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `calc_sobrante_inicial` (IN `piezas_por_fila` INT, IN `filas` INT, IN `niveles_completos` INT, IN `filas_completas` INT, IN `sobrante` INT, OUT `total` INT)   BEGIN 
SET total = (piezas_por_fila*filas*niveles_completos)+(piezas_por_fila*filas_completas+sobrante);
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `cantidadCamog` (IN `orden` VARCHAR(20))  NO SQL BEGIN

DECLARE idreg int;

set idreg=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SELECT sum(piezas_procesadas.cambioMOG) as totalCamog from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=idreg;


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `cantidadPlaneada` (IN `mg` VARCHAR(50))   BEGIN
select cantidad_planeada from mog where mog=mg;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `causasParoDAS` (IN `proc` VARCHAR(20), IN `cat` VARCHAR(30))  NO SQL BEGIN
DECLARE id int;
SET id=(SELECT procesos.id_proceso from procesos WHERE procesos.descripcion=proc);

SELECT CONCAT(causas_paro.numero_causas_paro," ",causas_paro.descripcion) as descripcion FROM `causas_paro` WHERE `causas_paro`.`procesos_idproceso`=id and causas_paro.categoria=cat ORDER BY causas_paro.numero_causas_paro;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `cerrarCorriendo` (IN `orde` VARCHAR(50))  NO SQL UPDATE `corriendoactualmente` SET corriendoactualmente.activo= 0 WHERE corriendoactualmente.ordenActual=orde$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `cerrarO` (IN `orde` INT)  NO SQL UPDATE registro_rbp SET registro_rbp.estado= 0 WHERE registro_rbp.orden_manufactura=orde$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `checkMog` (IN `orden` VARCHAR(20))  NO SQL BEGIN
SELECT piezas_procesadas.cantPzaGood, piezas_procesadas.registro_rbp_id_registro_rbp, piezas_procesadas.sobrante, registro_rbp.orden_manufactura from piezas_procesadas INNER JOIN registro_rbp ON piezas_procesadas.registro_rbp_id_registro_rbp = registro_rbp.id_registro_rbp where registro_rbp.orden_manufactura=orden and registro_rbp.estado=1 and registro_rbp.activo_op=1 and registro_rbp.aduana=1;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `cierreOrden` (IN `linea` VARCHAR(40), IN `codigo` VARCHAR(20), OUT `ret` INT)  NO SQL BEGIN
DECLARE cadena varchar(20);
if(codigo = '') THEN
set ret=2;
else
SET cadena=(SELECT empleado.codigo_alea from empleado WHERE empleado.codigo_alea=codigo AND empleado.cerrar_orden=1);
if(cadena IS null) then
SET ret=0;
ELSE
SET ret=1;
END IF;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `cierreTemporalSup` (IN `orden` VARCHAR(30))  NO SQL BEGIN

DECLARE idOr int;

set idOr=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);


UPDATE `registro_rbp` SET `estado`=0 WHERE registro_rbp.id_registro_rbp=idOr;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `comboTiempo_S` ()  NO SQL BEGIN
SELECT MAX(tiempo.fecha) as fechas from tiempo INNER JOIN registro_rbp on registro_rbp.id_registro_rbp=tiempo.registro_rbp_id_registro_rbp WHERE registro_rbp.estado=1 AND registro_rbp.activo_op=0 GROUP by tiempo.registro_rbp_id_registro_rbp;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `consultaCerradoOrdenes` (IN `pro` VARCHAR(20))  NO SQL SELECT mog.mog, registro_rbp.orden_manufactura, registro_rbp.proceso, empleado.nombre_empleado, empleado.apellido, cerradoordenes.hora_liberacion, cerradoordenes.fecha, cerradoordenes.tipo_liberacion FROM cerradoordenes INNER JOIN mog on mog.id_mog=cerradoordenes.id_mog INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=cerradoordenes.id_registro_rbp INNER JOIN empleado ON empleado.id_empleado=cerradoordenes.id_empleado WHERE cerradoordenes.tipo_liberacion='Aduana' AND mog.mog LIKE pro$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `consultaCerradoOrdenes2` (IN `pro` VARCHAR(20))  NO SQL SELECT mog.mog, registro_rbp.orden_manufactura, registro_rbp.proceso, empleado.nombre_empleado, empleado.apellido, cerradoordenes.hora_liberacion, cerradoordenes.fecha, cerradoordenes.tipo_liberacion FROM cerradoordenes INNER JOIN mog on mog.id_mog=cerradoordenes.id_mog INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=cerradoordenes.id_registro_rbp INNER JOIN empleado ON empleado.id_empleado=cerradoordenes.id_empleado WHERE cerradoordenes.tipo_liberacion='Supervisor' AND mog.mog LIKE pro$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `consultarDasTerminado` (IN `pro` VARCHAR(20))  NO SQL SELECT * FROM das WHERE das.activaOperador=0 AND das.activaSupervisor=1 AND proceso=pro$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `consultarMOG` (IN `orden` VARCHAR(20), OUT `sino` INT)  NO SQL BEGIN

DECLARE m varchar (20);

SET m=(SELECT mog from mog WHERE mog.mog=orden);

if(m is null) THEN
	SET sino=0;
ELSE
	SET sino=1;    
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `consultarRangos` (IN `rbp` VARCHAR(30), OUT `res` INT)  NO SQL BEGIN

declare id int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=rbp);

SET res=(SELECT rango_canasta_2 from piezas_procesadas where registro_rbp_id_registro_rbp=id ORDER BY idpiezas_procesadas DESC LIMIT 1);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `contarLotes` (IN `orden_man` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden_man);

SELECT COUNT(lote_coil.lote_coil) from lote_coil WHERE lote_coil.registro_rbp_id_registro_rbp=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `contarLotes2` (IN `orden_man` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;
DECLARE idMg int;

set idMg=(SELECT mog.id_mog from mog WHERE mog.mog=orden_man);

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.mog_id_mog=idMg and registro_rbp.orden_manufactura like '%BHL%');

SELECT COUNT(lote_coil.lote_coil) from lote_coil WHERE lote_coil.registro_rbp_id_registro_rbp=id GROUP BY lote_coil.lote_coil;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `DasCausasParo2` (IN `iddas` INT)  NO SQL BEGIN

SELECT registrocausasparo.idregistrocausasparo,registrocausasparo.causas_paro_idcausas_paro, causas_paro.numero_causas_paro, causas_paro.descripcion,  registrocausasparo.tiempo, registrocausasparo.detalle, registrocausasparo.hora_inicio FROM `registrocausasparo` INNER JOIN causas_paro on causas_paro.idcausas_paro=registrocausasparo.causas_paro_idcausas_paro
WHERE registrocausasparo.das_id_das = iddas;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `DasCausasParoBush` (IN `mo` VARCHAR(20), IN `orde` VARCHAR(20))  NO SQL BEGIN
DECLARE idmo int;
DECLARE idr int;

SET idmo=(SELECT mog.id_mog FROM mog WHERE mog.mog=mo);
SET idr=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orde);

SELECT registrocausasparo.idregistrocausasparo,registrocausasparo.causas_paro_idcausas_paro, causas_paro.numero_causas_paro, causas_paro.descripcion,  registrocausasparo.tiempo, registrocausasparo.detalle, registrocausasparo.hora_inicio FROM `registrocausasparo` INNER JOIN causas_paro on causas_paro.idcausas_paro=registrocausasparo.causas_paro_idcausas_paro INNER JOIN piezas_procesadas ON piezas_procesadas.dasiddas=registrocausasparo.das_id_das INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas.registro_rbp_id_registro_rbp
WHERE registrocausasparo.mog_idmog=idmo AND registro_rbp.id_registro_rbp=idr;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `dasEmMaqMedio` (IN `id` INT)  NO SQL BEGIN
DECLARE idrbp int;



END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `DasMaquinadoCausasParo` (IN `iddas` INT)  NO SQL BEGIN
SELECT * FROM `das_registrer` WHERE das_registrer.das_id_das=iddas;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `dasRegisPrensaS` (IN `id` INT)  NO SQL BEGIN
SELECT * FROM `das_reg_prensa` WHERE das_reg_prensa.das_iddas=id;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `dataCoiling` (IN `ordenMan` VARCHAR(50), OUT `exis` INT)  NO SQL BEGIN

SET exis=(SELECT orden_coil.id_orden_coil FROM orden_coil WHERE orden_coil.orden_coil=ordenMan and orden_coil.activo_operador=1);

IF (exis is not null) THEN 

SELECT orden_coil.material as parte, orden_coil.ancho as ancho, orden_coil.no_lote as lote from orden_coil where orden_coil.orden_coil=ordenMan;

ELSE
set exis=0;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `dataSlitter` (IN `ordenMan` VARCHAR(50), OUT `exis` INT, IN `pro` VARCHAR(20))  NO SQL BEGIN

SET exis=(SELECT ordenes_slitter.id_ordenSlitter FROM ordenes_slitter WHERE ordenes_slitter.orden_Slitter=ordenMan and ordenes_slitter.activo_op=1);

IF (exis is not null) THEN 

SELECT ordenes_slitter.no_Parte_Material as parte, ordenes_slitter.ancho as ancho, ordenes_slitter.numero_Lote as lote from ordenes_slitter where ordenes_slitter.orden_Slitter=ordenMan;

ELSE
set exis=0;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `DatosDasBGProcesos` (IN `id` INT)  NO SQL BEGIN
SELECT  mog.mog, mog.no_parte, das_prod_bgproceso.*,  das.linea, das.empleado_id_empleado, 
das.id_keeper, das.id_inspector, das.fecha, das.turno FROM das 
inner JOIN das_prod_bgproceso on das_prod_bgproceso.das_iddas=das.id_das 
INNER JOIN mog on mog.id_mog=das_prod_bgproceso.mog_idmog  
INNER JOIN piezas_procesadas on piezas_procesadas.dasiddas=das_prod_bgproceso.das_iddas
WHERE das.id_das=id AND das.activaOperador=0 GROUP BY das_prod_bgproceso.id_dasprodbgproc;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `DatosDasGenerico` (IN `id` INT, IN `pro` VARCHAR(20))  NO SQL BEGIN

SELECT das.linea as line, das.turno as tur, das.id_keeper as veri, das.empleado_id_empleado as opera, das.fecha from das WHERE das.id_das=id and proceso = pro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `DatosDasMaquinado` (IN `iddas` INT)  NO SQL BEGIN
SELECT totalpiezas_procesadas, mog.mog, total_piezas_aprobadas, total_scrap, das_produccion.*, mog.mog, das.linea, das.empleado_id_empleado, 
das.id_keeper, das.id_inspector, das.fecha, das.turno FROM das inner JOIN das_produccion on das_produccion.das_ida_das=das.id_das 
INNER JOIN mog on mog.id_mog=das_produccion.mog_idmog INNER JOIN registro_rbp ON registro_rbp.mog_id_mog = mog.id_mog INNER JOIN 
piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE das.id_das=iddas and 
das.proceso='MAQUINADO';
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `datosEmpaMesas` (IN `id` INT)  NO SQL BEGIN

SELECT * from das_produ_empamesas WHERE das_produ_empamesas.das_iddas=id;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `datosPrensa` (IN `id` INT)  NO SQL BEGIN

SELECT * from das_prod_pren WHERE das_prod_pren.das_iddas=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `defectosReporte` ()  NO SQL SELECT registro_rbp.orden_manufactura, CONCAT(razon_rechazo1.id_razon_rechazo,"-",razon_rechazo1.nombre_rechazo) as nume, sum(defecto1.cantidad_defecto) AS cant FROM defecto1 INNER JOIN razon_rechazo1 on 
razon_rechazo1.id_razon_rechazo=defecto1.razon_rechazo_id_razon_rechazo INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=defecto1.registro_rbp_id_registro_rbp WHERE defecto1.columna_sorting is null GROUP BY defecto1.razon_rechazo_id_razon_rechazo,
defecto1.registro_rbp_id_registro_rbp$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `estatusOrdenes` (IN `mogC` VARCHAR(50), IN `proce` VARCHAR(50))  NO SQL BEGIN

DECLARE id int;

SET id=(SELECT mog.id_mog from mog WHERE mog=mogC);

SELECT registro_rbp.estado, registro_rbp.activo_op, registro_rbp.aduana from registro_rbp where registro_rbp.mog_id_mog=id and  registro_rbp.proceso=proce;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `fillFecha` (IN `f1` VARCHAR(20))  NO SQL BEGIN
SELECT registro_rbp.mog, registro_rbp.orden_manufactura from registro_rbp INNER JOIN tiempo on registro_rbp.id_registro_rbp=tiempo.registro_rbp_id_registro_rbp WHERE tiempo.fecha=f1 and registro_rbp.estado=1 and registro_rbp.activo_op=0;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `fillNoPart` (IN `parte` VARCHAR(20))  NO SQL BEGIN
SELECT registro_rbp.mog, registro_rbp.orden_manufactura from registro_rbp WHERE registro_rbp.no_parte=parte and registro_rbp.estado=1 and registro_rbp.activo_op=0;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `filtrarOperador` (IN `nom` VARCHAR(30))  NO SQL BEGIN
DECLARE id int;
SET id=(SELECT empleado.id_empleado from empleado WHERE CONCAT(empleado.nombre_empleado,' ', empleado.apellido)=nom);

SELECT registro_rbp.mog,registro_rbp.orden_manufactura from registro_rbp INNER JOIN piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE piezas_procesadas_fg.id_empleado=id and registro_rbp.estado=1 and registro_rbp.activo_op=0 GROUP by registro_rbp.orden_manufactura;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `finishgood` (IN `parte` VARCHAR(20), OUT `sino` INT, OUT `total` INT, OUT `total_mas` INT, OUT `total_menos` INT)  NO SQL BEGIN
DECLARE rp int;

SET rp=(SELECT MAX(registro_rbp.id_registro_rbp) FROM mog INNER JOIN registro_rbp ON registro_rbp.mog_id_mog=mog.id_mog 
WHERE mog.no_parte=parte and registro_rbp.activo_op=0 and registro_rbp.estado=0 and registro_rbp.aduana=0 AND mog.no_parte=parte AND 
registro_rbp.orden_manufactura LIKE 'PCK%');

SET sino=0;
if(rp is not null)THEN

set total=(SELECT sobrante_final as finish FROM piezas_procesadas_fg WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=rp);

set total_mas=(SELECT sobrante_final_grading_menos as finish FROM piezas_procesadas_fg 
WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=rp);

set total_menos=(SELECT sobrante_final_grading_mas as finish FROM piezas_procesadas_fg 
WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=rp);

SET sino=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getAjustes` (IN `cat` VARCHAR(30))  NO SQL BEGIN
SELECT descripcion FROM `causas_paro` WHERE `categoria`='Ajustes';
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getCamogQty` (IN `ordenMan` VARCHAR(20))  NO SQL BEGIN

DECLARE idRBP int ;

SET idRBP=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=ordenMan);

SELECT SUM(piezas_procesadas.cambioMOG) as totalcamog from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=idRBP;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getCamogQty1` (IN `ordenMan` VARCHAR(20), OUT `totalcamog` INT)  NO SQL BEGIN

DECLARE idRBP int ;
declare cantidad int;

SET idRBP=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=ordenMan);

set cantidad=(SELECT SUM(piezas_procesadas.cambioMOG) as total from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=idRBP);

if (cantidad is null) THEN 
SET totalcamog=0;
ELSE
SET totalcamog=cantidad;
end if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getCountRbp_S` (IN `nombre_proceso` VARCHAR(30))  NO SQL BEGIN 
SELECT COUNT(*) AS regrbp FROM registro_rbp WHERE estado = 1 and activo_op = 0 and registro_rbp.aduana=1 AND proceso = nombre_proceso;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getDailyReport` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT registro_rbp.orden_manufactura, mog.mog, piezas_procesadas.cantidad_piezas_procesadas, piezas_procesadas.cantPzaGood, registro_rbp.estado, registro_rbp.activo_op, tiempo.fecha, tiempo.turno, tiempo.hora_fin from piezas_procesadas INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp = piezas_procesadas.registro_rbp_id_registro_rbp INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp = registro_rbp.id_registro_rbp INNER JOIN mog ON mog.id_mog = registro_rbp.mog_id_mog WHERE tiempo.fecha BETWEEN fecha1 and fecha2 GROUP by piezas_procesadas.idpiezas_procesadas$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getDailyReport2` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT registro_rbp.orden_manufactura, mog.mog, piezas_procesadas.cantidad_piezas_procesadas as 'Total Piezas Procesadas', piezas_procesadas.cantPzaGood as 'Cantidad piezas buenas',
piezas_procesadas.sobrante_inicial as 'Sobrante Inicial', piezas_procesadas.sobrante as 'Sofrante final',
registro_rbp.activo_op as 'Cerrado por operador', registro_rbp.estado as 'Validado por Supervisor',  tiempo.fecha, tiempo.turno, tiempo.hora_fin from piezas_procesadas INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp = piezas_procesadas.registro_rbp_id_registro_rbp INNER JOIN tiempo on tiempo.pza_pro_id = piezas_procesadas.idpiezas_procesadas INNER JOIN mog ON mog.id_mog = registro_rbp.mog_id_mog WHERE tiempo.fecha BETWEEN fecha1 and fecha2 GROUP by piezas_procesadas.idpiezas_procesadas$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getDandori` (IN `cat` VARCHAR(30))  NO SQL BEGIN
SELECT descripcion FROM `causas_paro` WHERE `categoria`='Dandori';
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getdatosPiezPro_S` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT)  NO SQL BEGIN 
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT sobrante,piezasxfila,filas,niveles,canastas,niveles_completos,filas_completas,cambioMOG,idpiezas_procesadas 
FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro
AND idpiezas_procesadas = id_pipro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getEmpleadoNombre` (IN `id_em` INT)   BEGIN
SELECT nombre_empleado,apellido FROM empleado WHERE id_empleado = id_em;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getEmpleadoSuper` (IN `idsuper` INT)   BEGIN
SELECT nombre_empleado, apellido FROM empleado inner join empleado_supervisor on
empleado_supervisor.empleado_id_empleado = empleado.id_empleado WHERE empleado_supervisor.id_empleado_supervisor=idsuper;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getEmpleado_S` (IN `orden_manu` VARCHAR(40))   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT empleado_id_empleado FROM empleado_has_registro_rbp WHERE registro_rbp_id_registro_rbp = id_registro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getFill_S` (IN `ord_manu` VARCHAR(30))   BEGIN

declare id_mg int DEFAULT 0;

SET id_mg=(SELECT mog_id_mog FROM registro_rbp WHERE orden_manufactura = ord_manu);

SELECT cantidad_planeada,num_dibujo,descripcion FROM mog WHERE id_mog=id_mg;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getMaquinado_S` ()  NO SQL BEGIN
SELECT work_center_maquina.codigo_maquina from work_center_maquina WHERE work_center_maquina.nombre_work_center='MAQUINADO';
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getModelSTD` (IN `orden` VARCHAR(15), OUT `si` INT)  NO SQL BEGIN
DECLARE exis int;
SET exis=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden and registro_rbp.activo_op=1);
if(exis is null) THEN
set si=0;
ELSE 
SET si=1;
SELECT registro_rbp.modelo, registro_rbp.STD from registro_rbp WHERE registro_rbp.orden_manufactura=orden;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getParo` (IN `cat` VARCHAR(30))  NO SQL BEGIN
SELECT descripcion FROM `causas_paro` WHERE `categoria`='Paro Planeado';
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getParosLinea` (IN `id` INT)  NO SQL BEGIN

SELECT registrocausasparo.hora_inicio, registrocausasparo.tiempo, causas_paro.numero_causas_paro, registrocausasparo.detalle from registrocausasparo INNER JOIN causas_paro on causas_paro.idcausas_paro=registrocausasparo.causas_paro_idcausas_paro
WHERE registrocausasparo.das_id_das=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getPiezasCanasIncom_S` (IN `orden_manu` VARCHAR(30))   BEGIN 
DECLARE id_registro,id_piepro int DEFAULT 0;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);
SET id_piepro=(SELECT MAX(idpiezas_procesadas) FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp=id_registro);
SELECT piezasxfila,filas,niveles_completos,filas_completas,sobrante FROM piezas_procesadas WHERE idpiezas_procesadas=id_piepro;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getProblema` (IN `cat` VARCHAR(30))  NO SQL BEGIN
SELECT descripcion FROM `causas_paro` WHERE `categoria`='Problema de Mantenimiento';
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getProceso` (IN `orden` VARCHAR(25))  NO SQL SELECT registro_rbp.proceso FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRBPcount_S` (IN `nombre_proceso` VARCHAR(30))  NO SQL BEGIN 
SELECT COUNT(*) AS regrbp FROM registro_rbp WHERE estado = 1 and activo_op = 0 and registro_rbp.aduana=1 AND proceso = nombre_proceso;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRBPGeneral` (IN `filtro` VARCHAR(50), IN `process` VARCHAR(50))  NO SQL BEGIN 

SELECT registro_rbp.orden_manufactura,mog.mog FROM registro_rbp INNER JOIN mog ON registro_rbp.mog_id_mog=mog.id_mog
WHERE mog.mog LIKE filtro  and registro_rbp.activo_op=0 and registro_rbp.estado=1 AND proceso = process;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRBP_Count_Aduana` (IN `nombre_proceso` VARCHAR(30))  NO SQL BEGIN 
SELECT COUNT(*) AS regrbp FROM registro_rbp WHERE estado = 0 and activo_op = 0  and registro_rbp.aduana= 1 AND proceso = nombre_proceso;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRbp_S` (IN `filtro` VARCHAR(30), IN `process` VARCHAR(30))   BEGIN 

SELECT registro_rbp.orden_manufactura,mog.mog FROM registro_rbp INNER JOIN mog ON registro_rbp.mog_id_mog=mog.id_mog
WHERE mog.mog LIKE filtro and registro_rbp.estado=0 and registro_rbp.activo_op=0 AND aduana=1 AND proceso = process;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRbp_S_Aduana` (IN `filtro` VARCHAR(30), IN `process` VARCHAR(30))  NO SQL BEGIN 

SELECT registro_rbp.orden_manufactura,mog.mog FROM registro_rbp INNER JOIN mog ON registro_rbp.mog_id_mog=mog.id_mog
WHERE mog.mog LIKE filtro  and registro_rbp.activo_op=0 and registro_rbp.estado=0 AND registro_rbp.aduana=1
AND proceso = process;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRevisarSorting` ()  NO SQL SELECT mog.mog, registro_rbp.orden_manufactura from registro_rbp INNER JOIN mog on 
mog.id_mog=registro_rbp.mog_id_mog WHERE registro_rbp.sortingSupervisor=1 and registro_rbp.orden_manufactura like '%PCK%'$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRevisarSorting1` ()  NO SQL SELECT mog.mog, registro_rbp.orden_manufactura from registro_rbp INNER JOIN mog on 
mog.id_mog=registro_rbp.mog_id_mog WHERE registro_rbp.sortingSupervisor=0 and registro_rbp.orden_manufactura like '%PCK%'$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getRevisarSortingAduana` (IN `ord` VARCHAR(20))  NO SQL SELECT mog.mog, registro_rbp.orden_manufactura from registro_rbp INNER JOIN mog on 
mog.id_mog=registro_rbp.mog_id_mog WHERE registro_rbp.sortingSupervisor=0 and mog.mog like ord AND registro_rbp.orden_manufactura LIKE '%PCK%'$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getScrapTotal` (IN `orden` VARCHAR(50), OUT `scr` INT)  NO SQL BEGIN

declare idr int;

set idr=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET scr=(SELECT sum(defecto1.cantidad_defecto) from defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=idr and defecto1.columna_sorting is null);

if(scr IS null) THEN

SET scr=0;

END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getSupervisor_S` (IN `codsp` INT, IN `work_center` VARCHAR(20), OUT `sies` INT, OUT `worker_name` VARCHAR(40), OUT `worker_name2` VARCHAR(40))   BEGIN 
DECLARE id_em,id_proc int DEFAULT 0;
DECLARE proceso_nm varchar(20);

SET id_em = (SELECT id_empleado FROM empleado WHERE codigo = codsp);

SET id_proc = (SELECT id_proceso FROM procesos WHERE descripcion = work_center);

SET proceso_nm = (SELECT procesos_idproceso FROM empleado_supervisor WHERE empleado_id_empleado = id_em AND procesos_idproceso = id_proc);

IF(id_em is null OR proceso_nm IS null)THEN 
SET sies = 0;
ELSE
SET sies = 1;
SET worker_name = (SELECT nombre_empleado FROM empleado WHERE codigo = codsp);
SET worker_name2 = (SELECT apellido FROM empleado WHERE codigo = codsp);
END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getTabRazonR_S` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT razon_rechazo_id_razon_rechazo AS ID, cantidad_defecto AS Cantidad, columna AS Colum FROM defecto WHERE registro_rbp_id_registro_rbp = id_registro AND defecto.columna_sorting is null;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getTabRazonR_S1` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT razon_rechazo_id_razon_rechazo AS ID, cantidad_defecto AS Cantidad, columna AS Colum FROM defecto1 WHERE registro_rbp_id_registro_rbp = id_registro AND defecto1.columna_sorting is null;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getTabRazonR_SMesaSort` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT razon_rechazo_id_razon_rechazo AS ID, cantidad_defecto AS Cantidad, columna_sorting AS Colum FROM defecto WHERE registro_rbp_id_registro_rbp = id_registro AND defecto.columna_sorting is not null;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getTabRazonR_SMesaSort1` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT razon_rechazo_id_razon_rechazo AS ID, cantidad_defecto AS Cantidad, columna_sorting AS Colum FROM defecto1 WHERE registro_rbp_id_registro_rbp = id_registro AND defecto1.columna_sorting is not null;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getTiempo_S` (IN `orden_manu` VARCHAR(40))   BEGIN 
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT fecha,horas_trabajadas FROM tiempo WHERE registro_rbp_id_registro_rbp =id_registro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `getTotalRecibido` (IN `orden` VARCHAR(20))  NO SQL BEGIN

DECLARE idOrden int;

SET idOrden=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SELECT piezas_procesadas_fg.total_piezas_recibidas from piezas_procesadas_fg WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=idOrden;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_canastas_siguiente_s` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT)   BEGIN

DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT canastas FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro AND idpiezas_procesadas = id_pipro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_coil_s` (IN `orden_manu` VARCHAR(30), OUT `rows_c` INT)   BEGIN
DECLARE id_rbp int DEFAULT 0;

SET id_rbp = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SET rows_c = (SELECT COUNT(id_lote_coil) FROM lote_coil WHERE registro_rbp_id_registro_rbp = id_rbp);

SELECT id_lote_coil,lote_coil,metros,f_terminado FROM lote_coil WHERE registro_rbp_id_registro_rbp = id_rbp;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_datos_piezas_pro_s` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT)   begin
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT sobrante,piezasxfila,filas,niveles,canastas,niveles_completos,filas_completas,cambioMOG,idpiezas_procesadas, piezas_procesadas.cantidad_piezas_procesadas, piezas_procesadas.cantPzaGood FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro
AND idpiezas_procesadas = id_pipro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_datos_total_piezas_p_S` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT total_piezas_recibidas AS TotalPiezasR,total_piezas_aprobadas AS TotalPiezasApro,verificacion,cambio_mog,pza_canasta_completa AS PiezasCanasCom,num_canasta_completa AS NCanastaCom,sobrante_final AS PiezasCanInc,totalpiezas_procesadas as piezas_prensadas,piezas_procesadas_fg.verificacion, verificacion2  FROM piezas_procesadas_fg WHERE registro_rbp_id_registro_rbp = id_registro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_fill_Piezas_Procesadas_S` (IN `orden_manu` VARCHAR(40))   BEGIN
DECLARE  id_reg_rbp int DEFAULT 0;

SET id_reg_rbp=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT idpiezas_procesadas, registro_rbp_id_registro_rbp, linea, rango_canasta_1, rango_canasta_2, cantPzaGood,cantidad_piezas_procesadas, piezas_procesadas.dasiddas FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_reg_rbp;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_fill_Piezas_Procesadas_SPre` (IN `orden_manu` VARCHAR(40))   BEGIN
DECLARE  id_reg_rbp int DEFAULT 0;

SET id_reg_rbp=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT idpiezas_procesadas,linea, rango_canasta_1, rango_canasta_2, cantPzaGood FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_reg_rbp;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_no_parte_s` (IN `orden_manu` VARCHAR(30))   BEGIN 

DECLARE id_mog_id int DEFAULT 0;

SET id_mog_id = (SELECT mog_id_mog FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT no_parte FROM mog WHERE id_mog = id_mog_id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_N_Registros_S` (IN `orden_manu` VARCHAR(30))   BEGIN

DECLARE id_manu int DEFAULT 0;

set id_manu = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);

SELECT COUNT(registro_rbp_id_registro_rbp) FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_manu; 

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_razonr_s` (IN `p1` INT, IN `p2` INT)   BEGIN
SELECT id_razon_rechazo,nombre_rechazo FROM razon_rechazo WHERE id_razon_rechazo BETWEEN p1 AND p2;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_razonr_s1` (IN `p1` INT, IN `p2` INT)   BEGIN
SELECT id_razon_rechazo,nombre_rechazo FROM razon_rechazo1 WHERE id_razon_rechazo BETWEEN p1 AND p2;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_registros_canastascom_incom` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT)   BEGIN
DECLARE id_registro int DEFAULT 0;
SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);
SELECT piezasxfila, filas, niveles,canastas,niveles_completos,filas_completas,sobrante,cambioMOG FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro AND idpiezas_procesadas = id_pipro;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_registros_canastascom_incom_stdmasok` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT)   BEGIN
DECLARE id_registro int DEFAULT 0;
SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu);
SELECT piezas_procesadas_grading.piezasxfila,piezas_procesadas_grading.filas,piezas_procesadas_grading.nivel,piezas_procesadas_grading.canastas,piezas_procesadas_grading.niveles_completos,
piezas_procesadas_grading.niveles_completos, piezas_procesadas_grading.filas_completas, piezas_procesadas_grading.sobrante,piezas_procesadas.cambioMOG FROM `piezas_procesadas_grading` INNER JOIN piezas_procesadas ON piezas_procesadas_grading.id_piezasProcesadas=piezas_procesadas.idpiezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp = id_registro AND piezas_procesadas_grading.stdE='+OK' AND piezas_procesadas_grading.id_piezasProcesadas = id_pipro;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_registros_canastascom_incom_stdmenosok` (IN `orden_manu` VARCHAR(30), IN `id_pipro` INT)   BEGIN DECLARE id_registro int DEFAULT 0; SET id_registro = (SELECT id_registro_rbp FROM registro_rbp 
WHERE orden_manufactura = orden_manu); SELECT piezas_procesadas_grading.piezasxfila,
piezas_procesadas_grading.filas,piezas_procesadas_grading.nivel,piezas_procesadas_grading.canastas,
piezas_procesadas_grading.niveles_completos, piezas_procesadas_grading.niveles_completos, 
piezas_procesadas_grading.filas_completas, piezas_procesadas_grading.sobrante,
piezas_procesadas.cambioMOG FROM `piezas_procesadas_grading` INNER JOIN piezas_procesadas ON piezas_procesadas_grading.id_piezasProcesadas=piezas_procesadas.idpiezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp = id_registro AND piezas_procesadas_grading.stdE='-OK' AND piezas_procesadas_grading.id_piezasProcesadas = id_pipro; END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_registros_qrtverificacion_s` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);
SELECT total_piezas_recibidas as piezasrecibidasplatinado, totalpiezas_procesadas AS totalpiezasprocesadas, cambio_mog AS cambiomog, verificacion AS veri FROM piezas_procesadas_fg WHERE registro_rbp_id_registro_rbp = id_registro;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_registros_std` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT piezasxfila*filas*niveles AS piezasxcajacompleta, SUM(canastas) AS nodecajascompletas, SUM(cantPzaGood)as totalpiezasaprobadas, sobrante_inicial as sobranteinicial from piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro LIMIT 1;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_registros_stdmasok` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);
SELECT piezas_procesadas_grading.piezasxfila*piezas_procesadas_grading.filas*piezas_procesadas_grading.nivel AS piezasporcajacompleta, SUM(piezas_procesadas_grading.canastas) as nocajacompletas,piezas_procesadas_grading.sobrante_inicial AS sobranteinicial, SUM(piezas_procesadas_grading.cant_piezas_buenas) AS totalpiezasaprobadas  FROM `piezas_procesadas_grading` INNER JOIN piezas_procesadas ON piezas_procesadas_grading.id_piezasProcesadas=piezas_procesadas.idpiezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp = id_registro AND piezas_procesadas_grading.stdE='+OK';
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_registros_stdmenosok` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);
SELECT piezas_procesadas_grading.piezasxfila*piezas_procesadas_grading.filas*piezas_procesadas_grading.nivel AS piezasporcajacompleta, SUM(piezas_procesadas_grading.canastas) as nocajacompletas,piezas_procesadas_grading.sobrante_inicial AS sobranteinicial, SUM(piezas_procesadas_grading.cant_piezas_buenas) AS totalpiezasaprobadas  FROM `piezas_procesadas_grading` INNER JOIN piezas_procesadas ON piezas_procesadas_grading.id_piezasProcesadas=piezas_procesadas.idpiezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp = id_registro AND piezas_procesadas_grading.stdE='-OK';
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_sobranteFinal_s` (IN `orden` VARCHAR(20))  NO SQL BEGIN

declare id_reg int;

SET id_reg=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SELECT sobra_fin as sobrantefinal FROM `piezas_procesadas` WHERE registro_rbp_id_registro_rbp=id_reg  order by idpiezas_procesadas desc
 limit 1;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_sobrantes_grading_s` (IN `orden_manu` VARCHAR(30))   BEGIN DECLARE id_registro int DEFAULT 0; SET id_registro = (SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura = orden_manu); SELECT sobrante_final as sobrantestd, sobrante_final_grading_mas AS sobrantemasok,sobrante_final_grading_menos AS sobrantemenosok FROM piezas_procesadas_fg WHERE registro_rbp_id_registro_rbp = id_registro; END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_sobrante_inicial_s` (IN `orden_manu` VARCHAR(30), IN `id_turno_anterior` INT)   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT piezas_procesadas.sobra_fin as sobrante FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro AND idpiezas_procesadas = id_turno_anterior;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_sobra_final` (IN `orden_manu` VARCHAR(20), IN `id_turno_anterior` INT)  NO SQL BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT sobra_fin FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro AND idpiezas_procesadas = id_turno_anterior;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_sobra_inicial_packing` (IN `orden_manu` VARCHAR(20), IN `id_turno_anterior` INT)   BEGIN
DECLARE id_registro int DEFAULT 0;

SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);

SELECT sobra_fin FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro AND idpiezas_procesadas = id_turno_anterior;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_Tab_RazonRsSort` (IN `orden_manu` VARCHAR(20))  NO SQL BEGIN
DECLARE idrbp int;

SET idrbp=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden_manu); 

select defecto.razon_rechazo_id_razon_rechazo as ID, SUM(defecto.cantidad_defecto) as Cantidad, defecto.columna as Colum from defecto INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp = defecto.registro_rbp_id_registro_rbp WHERE defecto.registro_rbp_id_registro_rbp=idrbp AND defecto.columna_sorting is not null GROUP by defecto.razon_rechazo_id_razon_rechazo;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_Tab_RazonRsSort1` (IN `orden_manu` VARCHAR(20))  NO SQL BEGIN
DECLARE idrbp int;

SET idrbp=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden_manu); 

select defecto1.razon_rechazo_id_razon_rechazo as ID, SUM(defecto1.cantidad_defecto) as Cantidad, defecto1.columna as Colum from defecto1 INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp = defecto1.registro_rbp_id_registro_rbp WHERE defecto1.registro_rbp_id_registro_rbp=idrbp AND defecto1.columna_sorting is not null GROUP by defecto1.razon_rechazo_id_razon_rechazo;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `get_Tab_RazonRsSortSupervisor` (IN `orden_manu` VARCHAR(20))  NO SQL BEGIN
DECLARE idrbp int;

SET idrbp=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden_manu); 

select defecto1.razon_rechazo_id_razon_rechazo as ID, SUM(defecto1.cantidad_defecto) as Cantidad, defecto1.columna as Colum from defecto1 INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp = defecto1.registro_rbp_id_registro_rbp WHERE defecto1.registro_rbp_id_registro_rbp=idrbp GROUP by defecto1.razon_rechazo_id_razon_rechazo;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `idDasPlatReport` (IN `fechaini` DATE, IN `fechafin` DATE, OUT `val` INT)   BEGIN
DECLARE prueba int;

set prueba=(SELECT das.id_das from das WHERE (das.linea='TG01' or das.linea='TG02' or das.linea='TG03' or das.linea='TGP01') and das.fecha BETWEEN fechaini and fechafin limit 1);

if(prueba is null) THEN
set val=0;

else

SELECT das.id_das from das WHERE (das.linea='TG01' or das.linea='TG02' or das.linea='TG03' or das.linea='TGP01') and das.fecha BETWEEN fechaini and fechafin;


set val=1;

end if;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `insertarCorriendo` (IN `manu` VARCHAR(20), IN `mog` VARCHAR(20), IN `hora` VARCHAR(10), IN `fecha` DATE, IN `line` VARCHAR(20))  NO SQL BEGIN
INSERT INTO `corriendoactualmente`(`linea`, `ordenActual`, `mogActual`, `hora_inicio`, `fecha`, `activo`) VALUES (line,manu,mog,hora,fecha,1);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarPiezasTotales` (IN `orden_manu` VARCHAR(20), IN `piezas_rec` INT, IN `pie_aprobadas` INT, IN `total_scra` INT, IN `verifi` INT, IN `sobra_fina` INT, IN `canas_comple` INT, IN `piezasxcanastaCom` INT, IN `piezasTotalesProcesadas` INT)  NO SQL BEGIN
DECLARE id_rbp int;

SET id_rbp=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura = orden_manu);

INSERT INTO `piezas_procesadas_fg`(`registro_rbp_id_registro_rbp`, `total_piezas_recibidas`, `total_piezas_aprobadas`, `cambio_mog`, `total_scrap`, `verificacion`, `sobrante_final`, `sobrante_final_grading_mas`, `sobrante_final_grading_menos`, `id_empleado`, `num_canasta_completa`, `pza_canasta_completa`, `totalpiezas_procesadas`, `idmogcambio`) VALUES (id_rbp,piezas_rec,pie_aprobadas,0,total_scra, verifi,sobra_fina,0,0,51,canas_comple,piezasxcanastaCom,piezasTotalesProcesadas,null);

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarPorTurnoMaquinado` (IN `orden_manu` VARCHAR(20), IN `rango1` INT, IN `rango2` INT, IN `piezas_pro` INT, IN `sobra_inicial` INT, IN `canas` INT, IN `piezas_buenas` INT, IN `turn` INT, IN `horas_traba` VARCHAR(10), IN `fech` DATE)  NO SQL BEGIN
DECLARE id_rbp int;

SET id_rbp=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura = orden_manu);


INSERT INTO `piezas_procesadas`(`registro_rbp_id_registro_rbp`, `linea`, `rango_canasta_1`, `rango_canasta_2`, `cantidad_piezas_procesadas`, `sobrante_inicial`, `piezasxfila`, `filas`, `niveles`, `canastas`, `niveles_completos`, `filas_completas`, `cambioMOG`, `sobrante`, `cantPzaGood`) VALUES (id_rbp,'TH10',rango1, rango2, piezas_pro, sobra_inicial, 22,5,4, canas,0,0,0,0,piezas_buenas);

INSERT INTO `empleado_has_registro_rbp`(`empleado_id_empleado`, `registro_rbp_id_registro_rbp`, `empleado_supervisor_id_empleado_supervisor`) VALUES (51,id_rbp,2);

INSERT INTO `tiempo`(`registro_rbp_id_registro_rbp`, `turno`, `hora_inicio`, `hora_fin`, `horas_trabajadas`, `fecha`) VALUES (id_rbp,turn,null,null,horas_traba,fech);

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarScrapManual` (IN `orden_manu` VARCHAR(20), IN `codigo_scrap` INT, IN `cantidad` INT, IN `colum` INT)  NO SQL BEGIN

DECLARE id_rbp int;

set colum = colum+1;

SET id_rbp=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden_manu);

INSERT INTO `defecto`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`) VALUES (id_rbp,codigo_scrap,cantidad,colum);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `insertar_coil` (IN `lote_coil` VARCHAR(20), IN `lote_new` VARCHAR(20), IN `orden` VARCHAR(20))  NO SQL BEGIN 

DECLARE nolot varchar(20);
DECLARE mate varchar(20);
DECLARE ancho float; 
DECLARE id int;

SET id = (SELECT orden_coil.id_orden_coil FROM orden_coil WHERE orden_coil.orden_coil=orden);

if (id IS null) THEN

SET nolot = (SELECT ordenes_slitter.numero_Lote FROM ordenes_slitter WHERE numero_lote=lote_new);

if (nolot is not null) THEN 

SET mate = (SELECT ordenes_slitter.no_Parte_Material FROM ordenes_slitter WHERE numero_lote=lote_new);

SET ancho = (SELECT ordenes_slitter.ancho FROM ordenes_slitter WHERE numero_lote=lote_new);

INSERT INTO `orden_coil`(`orden_coil`, `no_lote`, `material`, `ancho`,`activo_operador`,`activo_supervisor`,`final`) VALUES (orden,lote_coil,mate,ancho,1,0,0);

END IF;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `insertar_Registro_Slitter` (IN `orden` VARCHAR(20), IN `no_parte` VARCHAR(20), IN `ancho` FLOAT, IN `tiras` INT, IN `lote` VARCHAR(20), IN `ancho2` FLOAT)  NO SQL BEGIN
DECLARE existe int;

SET existe = (SELECT ordenes_slitter.id_ordenSlitter FROM ordenes_slitter WHERE ordenes_slitter.orden_Slitter=orden);
if(existe is null) then
INSERT INTO `ordenes_slitter`(`orden_Slitter`, `no_Parte_Material`, `ancho`, `ancho2`, `no_Tiras`, `numero_Lote`, `estado`, `activo_op`, `final`) VALUES (orden, no_parte,ancho,ancho2,tiras,lote,1,1,0);
END if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `insertCoil` (IN `orden` VARCHAR(20), IN `lote` VARCHAR(30), IN `mtr` DOUBLE, IN `termi` BOOLEAN)  NO SQL BEGIN
DECLARE reg int;

SET reg=(SELECT id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

INSERT INTO `lote_coil`(`registro_rbp_id_registro_rbp`, `lote_coil`, `metros`, `f_terminado`) VALUES (reg,lote,mtr,termi);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `insertCoil2` (IN `orden` VARCHAR(20), IN `lote` VARCHAR(30), IN `mtr` DOUBLE, IN `termi` BOOLEAN, IN `idPiez` INT)  NO SQL BEGIN
DECLARE reg int;
DECLARE idMgg int;
DECLARE idDBG int;

SET reg=(SELECT id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

set idDBG=(SELECT piezas_procesadas.dasiddas FROM piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idPiez);

Set idMgg=(SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.id_registro_rbp=reg);

INSERT INTO `lote_coil`(`registro_rbp_id_registro_rbp`, `lote_coil`, `metros`, `f_terminado`, `das_id_das`,`mog_id_mog`) VALUES (reg,lote,mtr,termi,idDBG, idMgg);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `insertOrderBush` (IN `m` VARCHAR(40), IN `pro` VARCHAR(40), IN `sec` INT, IN `orden` VARCHAR(40))  NO SQL BEGIN
DECLARE idmo int;
DECLARE exis int;
SET idmo=(SELECT mog.id_mog FROM mog WHERE mog.mog=m);

set exis=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

if(exis is null) THEN

INSERT INTO `registro_rbp`(`orden_manufactura`, `proceso`, `estado`,`activo_op`,`aduana`,`mog_id_mog`,`secuencia`) VALUES (orden,pro,1,1,1,idmo,sec);
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `insertTiempoSor` (IN `rbp` VARCHAR(20), IN `emple` VARCHAR(30), IN `tarj` INT, IN `hora_in` VARCHAR(20), IN `hora_fin` VARCHAR(20), IN `mogg` VARCHAR(20))  NO SQL BEGIN
DECLARE idrbp int;
DECLARE idmo int;
DECLARE idem int;

SET idrbp=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=rbp);

SET idem=(SELECT empleado.id_empleado FROM empleado WHERE empleado.codigo_alea=emple);

SET idmo=(SELECT mog.id_mog FROM mog WHERE mog.mog=mogg);

INSERT INTO `tiempo_sorteo`(`id_empleado`, `id_registro_rbp`, `tarjeta_sorteo`, `mog_id_mog`, `fecha`, `hora_inicio`, `hora_fin`) VALUES (idem,idrbp,tarj,idmo,CURDATE(),hora_in,hora_fin);
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `jaimeReport` (IN `idDas` INT)  NO SQL BEGIN

SELECT das.fecha, SUM(piezas_procesadas.cantidad_piezas_procesadas), sum(piezas_procesadas.cantPzaGood) from das 
inner JOIN piezas_procesadas on piezas_procesadas.dasiddas=das.id_das
WHERE piezas_procesadas.dasiddas GROUP BY das.id_das;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `largoPza` (IN `parte` VARCHAR(30), IN `orden` VARCHAR(30))  NO SQL BEGIN

select mog.largo from mog where mog.no_parte=parte and mog.mog=orden;


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarCatego` (IN `proc` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;
SET id=(SELECT procesos.id_proceso from procesos WHERE procesos.descripcion=proc);

SELECT causas_paro.categoria FROM `causas_paro` WHERE `causas_paro`.`procesos_idproceso`=id GROUP BY causas_paro.categoria ;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarCausasP` (IN `codEmp` VARCHAR(20), IN `defect` VARCHAR(45), IN `times` INT, IN `detal` VARCHAR(255), IN `hora` VARCHAR(20), IN `fec` DATE, IN `line` VARCHAR(20), IN `proce` VARCHAR(20), OUT `num` INT, IN `idas` INT, IN `orden` VARCHAR(20))  NO SQL BEGIN
DECLARE ide int;
DECLARE idDef int;
DECLARE idPro int;
DECLARE idmo int;

SET idPro=(SELECT procesos.id_proceso from procesos WHERE procesos.descripcion=proce);

set ide=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo_alea=codEmp);

SET idDef=(SELECT causas_paro.idcausas_paro FROM causas_paro WHERE Concat(causas_paro.numero_causas_paro, " ",causas_paro.descripcion)=defect and causas_paro.procesos_idproceso=idPro);

SET idmo=(SELECT registro_rbp.mog_id_mog from registro_rbp where registro_rbp.orden_manufactura=orden);

INSERT INTO `registrocausasparo`(`empleado_idempleado`, `causas_paro_idcausas_paro`, `tiempo`, `detalle`, `hora_inicio`, `fecha`, `linea`,`das_id_das`,`mog_idmog`) VALUES (ide,idDef,times,detal,hora,fec,line,idas,idmo);

SET num=(SELECT causas_paro.numero_causas_paro from causas_paro where Concat(causas_paro.numero_causas_paro, " ",causas_paro.descripcion)=defect and causas_paro.procesos_idproceso=idPro);

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenarCausasPC` (IN `codEmp` INT, IN `defect` VARCHAR(45), IN `times` INT, IN `detal` VARCHAR(255), IN `hora` VARCHAR(20), IN `fec` DATE, IN `line` VARCHAR(20), IN `proce` VARCHAR(20), OUT `num` INT, IN `idas` INT, IN `orden` VARCHAR(20))   BEGIN
DECLARE ide int;
DECLARE idDef int;
DECLARE idPro int;
DECLARE idmo int;

SET idPro=(SELECT procesos.id_proceso from procesos WHERE procesos.descripcion=proce);

set ide=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo=codEmp);

SET idDef=(SELECT causas_paro.idcausas_paro FROM causas_paro WHERE Concat(causas_paro.numero_causas_paro, " ",causas_paro.descripcion)=defect and causas_paro.procesos_idproceso=idPro);

SET idmo=(SELECT orden_coil.id_orden_coil from orden_coil where orden_coil.orden_coil=orden);

INSERT INTO `registrocausasparocoiling`(`empleado_idempleado`, `causas_paro_idcausas_paro`, `tiempo`, `detalle`, `hora_inicio`, `fecha`, `linea`,`das_id_das`,`ordencoil_idordenc`) VALUES (ide,idDef,times,detal,hora,fec,line,idas,idmo);

SET num=(SELECT causas_paro.numero_causas_paro from causas_paro where Concat(causas_paro.numero_causas_paro, " ",causas_paro.descripcion)=defect and causas_paro.procesos_idproceso=idPro);

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenarCausasPS` (IN `codEmp` INT, IN `defect` VARCHAR(45), IN `times` INT, IN `detal` VARCHAR(255), IN `hora` VARCHAR(20), IN `fec` DATE, IN `line` VARCHAR(20), IN `proce` VARCHAR(20), OUT `num` INT, IN `idas` INT, IN `orden` VARCHAR(20))   BEGIN
DECLARE ide int;
DECLARE idDef int;
DECLARE idPro int;
DECLARE idmo int;

SET idPro=(SELECT procesos.id_proceso from procesos WHERE procesos.descripcion=proce);

set ide=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo=codEmp);

SET idDef=(SELECT causas_paro.idcausas_paro FROM causas_paro WHERE Concat(causas_paro.numero_causas_paro, " ",causas_paro.descripcion)=defect and causas_paro.procesos_idproceso=idPro);

SET idmo=(SELECT ordenes_slitter.id_ordenSlitter from ordenes_slitter where ordenes_slitter.orden_Slitter=orden);

INSERT INTO `registrocausasparoslitter`(`empleado_idempleado`, `causas_paro_idcausas_paro`, `tiempo`, `detalle`, `hora_inicio`, `fecha`, `linea`,`das_id_das`,`ordenesslitter_idordenes`) VALUES (ide,idDef,times,detal,hora,fec,line,idas,idmo);

SET num=(SELECT causas_paro.numero_causas_paro from causas_paro where Concat(causas_paro.numero_causas_paro, " ",causas_paro.descripcion)=defect and causas_paro.procesos_idproceso=idPro);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarCoil` (IN `op` VARCHAR(20), IN `lote` VARCHAR(20), IN `metros` INT, IN `terminado` BOOLEAN, IN `das` INT)  NO SQL BEGIN
DECLARE id int;
DECLARE id_mog int;

SET id_mog=(SELECT registro_rbp.mog_id_mog from registro_rbp WHERE registro_rbp.orden_manufactura=op);

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=op);

INSERT INTO `lote_coil`(`registro_rbp_id_registro_rbp`, `lote_coil`, `metros`, `f_terminado`, `das_id_das`,`mog_id_mog`,`tiempo_insercion`) VALUES (id,lote,metros,terminado,das, id_mog,CURTIME());

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarDas` (OUT `idDas` INT, IN `linea` VARCHAR(10), IN `codEmp` VARCHAR(20), IN `codKeep` VARCHAR(20), IN `codIns` VARCHAR(20), IN `fecha` DATE, IN `tur` INT, IN `proce` VARCHAR(20))  NO SQL BEGIN
DECLARE idE,idke,idIn int;

set idE=(SELECT empleado.id_empleado FROM empleado where empleado.codigo_alea=codEmp);

SET idKe=(SELECT empleado.id_empleado from empleado where empleado.codigo_alea=codKeep);

SET idIn=(SELECT empleado.id_empleado FROM empleado where empleado.codigo_alea=codIns);

INSERT INTO `das`(`linea`, `empleado_id_empleado`, `id_keeper`, `id_inspector`, `fecha`,`activaSupervisor`,`activaOperador`,`turno`, `procesO`) VALUES(linea,idE,idKe,idIn,fecha,1,1,tur,proce);

set idDas=(SELECT max(id_das) from das);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenarDasGrading` (IN `lot` VARCHAR(20), IN `in_c` VARCHAR(50), IN `fin_c` VARCHAR(50), IN `inP` VARCHAR(50), IN `fiP` VARCHAR(50), IN `sor` INT, IN `maq` INT, IN `orden` VARCHAR(20), IN `idas` INT)   BEGIN

DECLARE mog1 int;

set mog1 =(select registro_rbp.mog_id_mog from registro_rbp where registro_rbp.orden_manufactura=orden);

INSERT INTO das_prod_grading (lote,in_cm,fin_cm,in_tp,fin_tp,sorting,maquina,mog_idmog,das_idas) values(lot,in_c,fin_c,inP,fiP, sor,maq,mog1,idas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarDasProPlat` (IN `lot` VARCHAR(10), IN `r1` INT, IN `r2` INT, IN `r3` INT, IN `r4` INT, IN `r5` INT, IN `r6` INT, IN `r7` INT, IN `r8` INT, IN `r9` INT, IN `iddas` INT, IN `smog` VARCHAR(20), IN `hora_inicio` VARCHAR(20), IN `hora_fin` VARCHAR(20))  NO SQL BEGIN
DECLARE idmog int;
DECLARE idP int;
DEclare idReg int;
set idmog=(SELECT mog.id_mog FROM mog WHERE mog.mog=smog);

set idReg=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.mog_id_mog=idmog and registro_rbp.orden_manufactura like '%PLT%');

set idP=(SELECT MAX(piezas_procesadas.idpiezas_procesadas) from piezas_procesadas WHERE piezas_procesadas.dasiddas=iddas AND piezas_procesadas.registro_rbp_id_registro_rbp=idReg);

INSERT INTO `das_prod_plat`(`mog_idmog`, `lote`, `ini_tur`, `fin_tur`, `razon1`, `razon2`, `razon3`, `razon4`, `razon5`, `razon6`, `razon7`, `razon8`, `razon9`, `das_id_das`, piezasProcesadas_id_piezadprocesada) VALUES (idmog,lot,hora_inicio,hora_fin,r1,r2,r3,r4,r5,r6,r7,r8,r9,iddas, idP);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarDas_has_registro` (IN `iddas` INT, IN `orden` VARCHAR(30))  NO SQL BEGIN

DECLARE id int;

SET id=(SELECT id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura = orden);

INSERT INTO das_has_registro_rbp(`id_registro_rbp`, `das_iddas`) VALUES(id,iddas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarDefecto` (IN `orden` VARCHAR(20), IN `cant` INT, IN `colum` INT, IN `id_ra` INT, IN `razon` VARCHAR(30))   BEGIN 
DECLARE id int; 
DECLARE razon_id int; 
SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden); 
SET razon_id=(SELECT razon_rechazo.id_razon_rechazo FROM razon_rechazo WHERE razon_rechazo.id_razon_rechazo=id_ra and razon_rechazo.nombre_rechazo=razon); 
INSERT INTO `defecto`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`, columna_sorting) VALUES (id,razon_id,cant,colum,NULL); 
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarDefecto1` (IN `orden` VARCHAR(20), IN `cant` INT, IN `colum` INT, IN `id_ra` INT, IN `razon` VARCHAR(60))   BEGIN 
DECLARE id int; 
DECLARE razon_id int; 
SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden); 
SET razon_id=(SELECT razon_rechazo1.id_razon_rechazo FROM razon_rechazo1 WHERE razon_rechazo1.id_razon_rechazo=id_ra and razon_rechazo1.nombre_rechazo=razon); 
INSERT INTO `defecto1`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`, columna_sorting) VALUES (id,razon_id,cant,colum,NULL); 
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarDefectoSor` (IN `orden` VARCHAR(20), IN `cant` INT, IN `colum` INT, IN `id_ra` INT, IN `razon` VARCHAR(30), IN `column_sort` INT)   BEGIN

DECLARE id int; 
DECLARE razon_id int;
DECLARE exist int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET razon_id=(SELECT razon_rechazo.id_razon_rechazo FROM razon_rechazo WHERE razon_rechazo.id_razon_rechazo=id_ra and razon_rechazo.nombre_rechazo=razon);

SET exist=(SELECT defecto.razon_rechazo_id_razon_rechazo FROM defecto WHERE defecto.registro_rbp_id_registro_rbp=id AND defecto.razon_rechazo_id_razon_rechazo=razon_id 
and columna_sorting = column_sort);

if(exist > 0) THEN
UPDATE `defecto` SET `cantidad_defecto`=cantidad
WHERE registro_rbp_id_registro_rbp = id AND razon_rechazo_id_razon_rechazo = razon_id and columna_sorting=column_sort;
ELSE
if (cant !=0) then
INSERT INTO `defecto`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`, columna_sorting) VALUES (id,razon_id,cant,2,column_sort);
END IF;
END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarDefectoSor1` (IN `orden` VARCHAR(20), IN `cant` INT, IN `colum` INT, IN `id_ra` INT, IN `razon` VARCHAR(30), IN `column_sort` INT)   BEGIN

DECLARE id int; 
DECLARE razon_id int;
DECLARE exist int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET razon_id=(SELECT razon_rechazo1.id_razon_rechazo FROM razon_rechazo1 WHERE razon_rechazo1.id_razon_rechazo=id_ra and razon_rechazo1.nombre_rechazo=razon);

SET exist=(SELECT defecto1.razon_rechazo_id_razon_rechazo FROM defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=id AND defecto1.razon_rechazo_id_razon_rechazo=razon_id 
and columna_sorting = column_sort);

if(exist > 0) THEN
UPDATE `defecto1` SET `cantidad_defecto`=cantidad
WHERE registro_rbp_id_registro_rbp = id AND razon_rechazo_id_razon_rechazo = razon_id and columna_sorting=column_sort;
ELSE
if (cant !=0) then
INSERT INTO `defecto1`(`registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`, columna_sorting) VALUES (id,razon_id,cant,6,column_sort);
END IF;
END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarEmpleado_has_res_RBP` (IN `orden` VARCHAR(20), IN `cod` VARCHAR(20), IN `maq` VARCHAR(20))   BEGIN 
DECLARE id_em int; 
DECLARE id int; 
DECLARE id_sup int; 

SET id_em=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo_alea=cod); 
SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden); 

IF(maq='TB91' or maq='TB92' or maq='TB03B' or maq='TB03F' or maq='TB05' or maq='TB01' or maq='TB06' or maq='TB32') THEN

SET id_sup=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo=519); 

ELSE
SET id_sup=(SELECT work_center_maquina.empleado_supervisor_id_empleado_supervisor from work_center_maquina where 
work_center_maquina.codigo_maquina=maq); 
END IF;

INSERT INTO `empleado_has_registro_rbp`(`empleado_id_empleado`, `registro_rbp_id_registro_rbp`, 
`empleado_supervisor_id_empleado_supervisor`) VALUES (id_em,id,id_sup);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarEmpleado_has_res_RBP2` (IN `maq` VARCHAR(20), OUT `id_sup` INT)   BEGIN 

IF(maq='TB91' or maq='TB92' or maq='TB03B' or maq='TB03F' or maq='TB05' or maq='TB01' or maq='TB06' or maq='TB32') THEN

SET id_sup=(SELECT empleado.id_empleado from empleado WHERE empleado.codigo=519); 

ELSE

SET id_sup=(SELECT work_center_maquina.empleado_supervisor_id_empleado_supervisor from work_center_maquina where 
work_center_maquina.codigo_maquina=maq); 


END if;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenarMatA` (IN `no_ti` INT, IN `mts` DOUBLE, IN `id` INT)  NO SQL BEGIN

INSERT INTO `coiling_material_a`(`no_tira`, `metros`, `dasprodcoi_iddasprodcoi`) VALUES (no_ti,mts,id);

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenarMatS` (IN `no_ti` INT, IN `mts` DOUBLE, IN `id` INT)   BEGIN

INSERT INTO `coiling_material_s`(`no_tira`, `metros`, `dasprodcoi_iddasprodcoi`) VALUES (no_ti,mts,id);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarMog` (IN `mogo` VARCHAR(20), IN `descr` VARCHAR(25), IN `num` VARCHAR(20), IN `part` VARCHAR(20), IN `model` VARCHAR(20), IN `stdo` VARCHAR(10), IN `cant` INT, OUT `idmo` INT, IN `weigth` DOUBLE, IN `large` DOUBLE)   BEGIN
DECLARE om varchar(20);
SELECT COUNT(*) FROM empleado FOR UPDATE;
SET om=(SELECT mog FROM mog WHERE mog.mog=mogo);
if(om is null) THEN

INSERT INTO `mog`(`mog`, `descripcion`, `num_dibujo`, `no_parte`, `modelo`, `STD`, `cantidad_planeada`, `peso`, `largo`) VALUES (mogo,descr,num,part,model,stdo,cant,weigth,large);
SET idmo=(SELECT MAX(mog.id_mog) FROM mog);
ELSE
SET idmo=(SELECT mog.id_mog FROM mog WHERE mog.mog=mogo);
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarPiezasP_fg` (IN `orden` VARCHAR(20), IN `pzaRec` INT, IN `pzaApro` INT, IN `cha_mog` INT, IN `scrap` INT, IN `verif` INT, IN `so_F` INT, IN `cod_emp` VARCHAR(20), IN `num_can` INT, IN `p_can_com` INT, IN `totalpro` INT, IN `mogg` VARCHAR(50), IN `so_F_mas` INT, IN `so_F_menos` INT, IN `verf2` INT)   BEGIN 

DECLARE id int; 
DECLARE id_em int; 
DECLARE idmo int;

if (mogg is not null) THEN

SET idmo=(SELECT id_mog from mog where mog.mog=mogg);

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden); 

SET id_em=(SELECT empleado.id_empleado from empleado where empleado.codigo_alea=cod_emp); 

INSERT INTO `piezas_procesadas_fg`(`registro_rbp_id_registro_rbp`, `total_piezas_recibidas`, `total_piezas_aprobadas`,
`cambio_mog`, `total_scrap`, `verificacion`,`verificacion2`, `sobrante_final`,`sobrante_final_grading_mas`,`sobrante_final_grading_menos`,`id_empleado`,
`num_canasta_completa`, `pza_canasta_completa`,`totalpiezas_procesadas`,`idmogcambio`) 
VALUES (id,pzaRec,pzaApro,cha_mog,scrap,verif,verf2,so_F,so_F_mas,so_F_menos,id_em,num_can,p_can_com,totalpro,idmo); 

UPDATE registro_rbp SET activo_op=0 where registro_rbp.id_registro_rbp=id;

SET @p0=orden; CALL `cerrarCorriendo`(@p0);

ELSE 

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden); 

SET id_em=(SELECT empleado.id_empleado from empleado where empleado.codigo_alea=cod_emp); 

INSERT INTO `piezas_procesadas_fg`(`registro_rbp_id_registro_rbp`, `total_piezas_recibidas`, `total_piezas_aprobadas`,
`cambio_mog`, `total_scrap`, `verificacion`,`verificacion2`, `sobrante_final`, `sobrante_final_grading_mas`,`sobrante_final_grading_menos`,
`id_empleado`,`num_canasta_completa`, `pza_canasta_completa`,`totalpiezas_procesadas`,`idmogcambio`) 
VALUES (id,pzaRec,pzaApro,cha_mog,scrap,verif,verf2,so_F,so_F_mas,so_F_menos,id_em,num_can,p_can_com,totalpro,null); 

UPDATE registro_rbp SET activo_op=0, estado=0  where registro_rbp.id_registro_rbp=id;

SET @p0=orden; 
CALL `cerrarCorriendo`(@p0);
CALL `cerrarO`(@p0);


END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarRBP` (IN `m` VARCHAR(20), IN `pro` VARCHAR(25), IN `idmo` INT, IN `sec` INT, OUT `id_rbp` INT)   BEGIN
DECLARE om varchar(25);
SELECT COUNT(*) FROM empleado FOR UPDATE;
SET om=(SELECT registro_rbp.orden_manufactura FROM registro_rbp WHERE registro_rbp.orden_manufactura=m);

if(om is null) THEN
INSERT INTO `registro_rbp`(`orden_manufactura`, `proceso`, `estado`,`activo_op`,`aduana`,`mog_id_mog`,`secuencia`) VALUES (m,pro,1,1,1,idmo,sec);
END IF;

set id_rbp = (select r_rbp.id_registro_rbp from registro_rbp as r_rbp where r_rbp.mog_id_mog = idmo and r_rbp.proceso = pro);
             

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarRBP1` (IN `m` VARCHAR(20), IN `pro` VARCHAR(25), IN `idmo` INT, IN `sec` INT)   BEGIN
DECLARE om varchar(25);
SELECT COUNT(*) FROM empleado FOR UPDATE;
SET om=(SELECT registro_rbp.orden_manufactura FROM registro_rbp WHERE registro_rbp.orden_manufactura=m);

if(om is null) THEN
INSERT INTO `registro_rbp`(`orden_manufactura`, `proceso`, `estado`,`activo_op`,`aduana`,`mog_id_mog`,`secuencia`, `sortingSupervisor`) VALUES (m,pro,1,1,1,idmo,sec,1);
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenarRegCoiling` (IN `codEmp` VARCHAR(50), IN `hora` VARCHAR(20), IN `ok1` BOOLEAN, IN `idDas` INT)  NO SQL BEGIN
DECLARE idE int;
SET idE=(SELECT empleado.id_empleado FROM empleado WHERE CONCAT(empleado.nombre_empleado,' ',empleado.apellido)=codEmp);

INSERT INTO `das_reg_coiling` (`empleado_idempleado`, `hora`, `ok`, `das_iddas`) VALUES (idE,hora,ok1,idDas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenarTiempo` (IN `orden` VARCHAR(20), IN `tur` INT, IN `hora1` VARCHAR(20), IN `hora2` VARCHAR(20), IN `horaT` VARCHAR(20), IN `fecha` DATE)   BEGIN 
DECLARE id int;
DECLARE idpro int;
SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden); 

set idpro=(SELECT MAX(piezas_procesadas.idpiezas_procesadas) from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=id);

INSERT INTO `tiempo`(`registro_rbp_id_registro_rbp`, `turno`, `hora_inicio`, `hora_fin`, `horas_trabajadas`, `fecha`,`pza_pro_id`) VALUES (id,tur,hora1,hora2,horaT,fecha, idpro); 
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenarTotalesCoi` (IN `mtsAcep` DOUBLE, IN `mtsPro` DOUBLE, IN `bobinas` INT, IN `scrap` DOUBLE, IN `idas` INT)  NO SQL BEGIN

INSERT INTO `das_coiling_totales`(`total_mts_aceptados`, `total_mts_procesados`, `total_bobina`, `totalScrap`,`das_iddas`) VALUES(mtsAcep,mtsPro,scrap,idas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_dasEmpaquePrd` (IN `mog1` VARCHAR(20), IN `lot` VARCHAR(20), IN `scraps` INT, IN `scrapm` INT, IN `idas` INT, IN `incm` VARCHAR(50), IN `ficm` VARCHAR(50), IN `intp` VARCHAR(50), IN `fitp` VARCHAR(50))  NO SQL BEGIN
DECLARE idmog int;

SET idmog=(SELECT mog.id_mog FROM mog WHERE mog.mog=mog1);

INSERT INTO `das_prod_empmaq`(`lote`,`ini_cm`, `fin_cm`, `ini_tp` , `fin_tp`, `scrap_sorting`, `scrap_maquina`, `das_iddas`, `mog_idmog`) 
VALUES (lot,incm,fIcm,intp,fitp,scraps,scrapm,idas,idmog);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_dasEmpMes` (IN `inicio` VARCHAR(20), IN `fin` VARCHAR(20), IN `can_pro` INT, IN `pzaBuen` INT, IN `sort` INT, IN `sobra` INT, IN `cod` VARCHAR(20), IN `idas` INT, IN `lot` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;

SET id=(SELECT `registro_rbp`.`mog_id_mog` from registro_rbp where registro_rbp.orden_manufactura=cod);

INSERT INTO `das_produ_empamesas`(`lote`,`ini_tu`, `fin_tu`, `cant_proce`, `pza_buenas`, `pza_Sort`, `Sobrante_Final`, `mog_idmog`, `das_iddas`) 
VALUES (lot,inicio,fin,can_pro,pzaBuen,sort,sobra,id,idas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_dasProdBGPro` (IN `idas` INT, IN `cod` VARCHAR(20), IN `partFin` VARCHAR(30), IN `matbu` VARCHAR(20), IN `matrg` VARCHAR(20), IN `matsr` VARCHAR(20), IN `lotb` VARCHAR(20), IN `lotr` VARCHAR(20), IN `lotsr` VARCHAR(20), IN `ini_cm` VARCHAR(20), IN `fin_cm` VARCHAR(20), IN `ini_tp` VARCHAR(20), IN `fin_tp` VARCHAR(20), IN `pcsprob` INT, IN `pcsbb` INT, IN `scrapb` INT, IN `pcspro` INT, IN `pcsbr` INT, IN `scraprg` INT, IN `pcsprosr` INT, IN `pcsbsr` INT, IN `scrapsr` INT)  NO SQL BEGIN
DECLARE id int;

SET id=(SELECT mog.id_mog from mog WHERE mog.mog=cod);


INSERT INTO `das_prod_bgproceso`( `num_parteFin`, `material_bush`, `material_rg`, `material_sr`, `lote_bush`, `lote_rg`, `lote_sr`, `ini_cm`, `fin_cm`, `ini_tp`, `fin_tp`, `pcs_pro_bush`, `pcs_buen_bush`, `scrap_bush`, `pcs_pro_rg`, `pcs_buen_rg`, `scrp_rg`, `pcs_pro_sr`, `pcs_buen_sr`, `scrap_sr`, `mog_idmog`, `das_iddas`) VALUES (partFin,matbu,matrg,matsr,lotb,lotr,lotsr,ini_cm,fin_cm,ini_tp,fin_tp,pcsprob,pcsbb,scrapb,pcspro,pcsbr,scraprg,pcsprosr,pcsbsr,scrapsr,id,idas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_dasPro_Prensa` (IN `mog1` VARCHAR(20), IN `mate` VARCHAR(20), IN `cortco` DOUBLE, IN `lote` VARCHAR(30), IN `mts` DOUBLE, IN `ini_cm` VARCHAR(20), IN `fi_cm` VARCHAR(20), IN `in_tp` VARCHAR(20), IN `fi_tp` VARCHAR(20), IN `pzaT` INT, IN `bmk` DOUBLE, IN `pcbm` INT, IN `pzao` INT, IN `ngkg` DOUBLE, IN `pcsng` INT, IN `idas` INT, IN `centro` VARCHAR(10), IN `extremo` VARCHAR(10), IN `sello` VARCHAR(10))  NO SQL BEGIN
DECLARE id_mog int;
SET id_mog=(SELECT mog.id_mog from mog WHERE mog.mog=mog1);

INSERT INTO `das_prod_pren`(`material`, `corte_coiling`, `lot_material`, `metros`, `inicio_cm`, `fin_cm`, `inicio_tp`, `fin_tp`, `centroEstampado`, `extremo`, `sello`, `pzasTotales`, `bm_kg`, `pcs_bm`, `pza_ok`, `ng_kg`, `pcs_ng`, `das_iddas`, `mog_idmog`) VALUES (mate,cortco,lote,mts,ini_cm,fi_cm,in_tp,fi_tp,centro,extremo, sello, pzaT, bmk, pcbm, pzao, ngkg, pcsng, idas, id_mog);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_dasRegisPren` (IN `codEmp` VARCHAR(50), IN `hor` VARCHAR(20), IN `cal` BOOLEAN, IN `idDas` INT)  NO SQL BEGIN
DECLARE idE int;

SET idE=(SELECT empleado.id_empleado FROM empleado WHERE CONCAT(empleado.nombre_empleado,' ',empleado.apellido)=codEmp);

INSERT INTO `das_reg_prensa`( `hora`, `ok`, `empleado_idempleado`, `das_iddas`) VALUES (hor,cal,idE,idDas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_dasSlitter` (IN `orden` VARCHAR(20), IN `tiras` INT, IN `in_Ajuste` VARCHAR(20), IN `fin_Ajuste` VARCHAR(20), IN `in_Pro` VARCHAR(20), IN `fin_Pro` VARCHAR(20), IN `in_Ama` VARCHAR(20), IN `fin_Ama` VARCHAR(20), IN `mts_pro` DOUBLE, IN `mts_ng` DOUBLE, IN `cant_bm` DOUBLE, IN `scrapkg` DOUBLE, IN `iddas` INT)  NO SQL BEGIN

DECLARE idS int;

SET idS=(SELECT ordenes_slitter.id_ordenSlitter from ordenes_slitter WHERE ordenes_slitter.orden_Slitter=orden);

INSERT INTO `das_slitter`( `no_tiras`,`ajuste_inicio`, `ajuste_final`, `proces_inicio`, `proces_final`, `amarre_inicio`, `amarre_fin`, `mtr_procesados`, `mtrs_ng`, `cantidad_mb`, `scrap`, `das_id_das`,`ordenSlitter_idordenS`) VALUES (tiras,in_Ajuste,fin_Ajuste,in_Pro, fin_Pro,in_Ama,fin_Ama,mts_pro,mts_ng,cant_bm,scrapkg,iddas,idS);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_das_produccion` (IN `mog1` VARCHAR(15), IN `modelo` VARCHAR(30), IN `STD1` VARCHAR(10), IN `lote1` VARCHAR(10), IN `in_cm` VARCHAR(30), IN `fin_cm1` VARCHAR(30), IN `in_tp` VARCHAR(30), IN `fin_tp1` VARCHAR(30), IN `id_das` INT(11), IN `idP` INT)  NO SQL BEGIN
DECLARE idm int;

SET idm=(SELECT mog.id_mog FROM mog WHERE mog.mog=mog1);

INSERT INTO `das_produccion`(`modelo`, `estandar`, `lote`, `inicio_cm`, `fin_cm`, `inicio_tp`, `fin_tp`, `das_ida_das`, `mog_idmog`,piezasProcesadas_id_piezadprocesada) 
VALUES (modelo,STD1,lote1,in_cm,fin_cm1,in_tp,fin_tp1,id_das,idm, idP);
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_das_prod_BPrensa` (IN `material` VARCHAR(20), IN `anc` DOUBLE, IN `num_lote` VARCHAR(20), IN `mts` DOUBLE, IN `lot1` VARCHAR(20), IN `in1` VARCHAR(20), IN `fi1` VARCHAR(20), IN `ini2` VARCHAR(20), IN `fin2` VARCHAR(20), IN `pcpro` INT, IN `pcgood` INT, IN `pcscrap` INT, IN `pcbm` INT, IN `idas` INT, IN `cod` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;

SET id=(SELECT mog.id_mog from mog where mog.mog=cod);

INSERT INTO `das_prod_bgprensa`(`no_mat`, `anch_mat`, `num_lot_mat`, `metros`, `lote`, `ini_cm`, `fin_cm`, `ini_tp`, `fin_tp`, `pcs_pro`, `pcs_buenas`, `pcs_scrap`, `pcs_bm`, `das_id_das`, `mog_idmog`) VALUES (material,anc,num_lote,mts,lot1,in1,fi1,ini2,CURTIME(),pcpro,pcgood,pcscrap,
pcbm,idas,id);


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_das_registro` (IN `codEmp` VARCHAR(50), IN `hora` VARCHAR(20), IN `acomulado` INT, IN `pzxhr` INT, IN `ok` BOOLEAN, IN `idDas` INT)  NO SQL BEGIN
DECLARE idE int;

SET idE=(SELECT empleado.id_empleado FROM empleado WHERE CONCAT(empleado.nombre_empleado,' ',empleado.apellido)=codEmp);

INSERT INTO `das_registrer`(`empleado_id_empleado`, `hora`, `acumulado`, `piezasxhora`, `ok`, `das_id_das`) VALUES (idE,hora,acomulado,pzxhr,ok,idDas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_das_SlitterReg` (IN `codEmp` VARCHAR(50), IN `hora` VARCHAR(20), IN `ok1` BOOLEAN, IN `idDas` INT)   BEGIN
DECLARE idE int;
SET idE=(SELECT empleado.id_empleado FROM empleado WHERE CONCAT(empleado.nombre_empleado,' ',empleado.apellido)=codEmp);

INSERT INTO `das_reg_slitter`(`empleado_idempleado`, `hora`, `ok`, `das_iddas`) VALUES (idE,hora,ok1,idDas);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `llenar_piezas_pro` (IN `line` VARCHAR(10), IN `s_anterior` INT, IN `orden` VARCHAR(20), IN `totalpc` INT, IN `sobrante` INT, IN `tProce` INT, OUT `r1` INT, OUT `r2` INT, IN `piexfila` INT, IN `filas` INT, IN `niveles` INT, IN `canastas` INT, IN `nivelcompleto` INT, IN `filacom` INT, IN `mog` INT, IN `sobrante_f` INT, OUT `idpiepro` INT, IN `das` INT)  NO SQL BEGIN
DECLARE id int;
DECLARE res int;
DECLARE rango1 int;
DECLARE rango2 int;
DECLARE res2 int;
DECLARE a1 int;
DECLARE a2 int;
DECLARE a3 int;
DECLARE tt int;
DECLARE pzab int ;
DECLARE sobF int;

SET a1=(piexfila*filas*niveles*canastas);


if(orden LIKE '%PRS%') THEN
SET a2=(piexfila*filacom);
ELSE
SET a2=(piexfila*filas*nivelcompleto);
END IF;



if(orden LIKE '%PRS%') THEN
SET a3=sobrante_f;
ELSE
SET a3=((piexfila*filacom)+sobrante_f);
END IF;

SET tt=(a1+a2+a3);
SET sobF=(a2+a3);
SET pzab=(tt-s_anterior);
SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET res=(SELECT rango_canasta_2 from piezas_procesadas where registro_rbp_id_registro_rbp=id ORDER BY idpiezas_procesadas DESC LIMIT 1);

IF (res is null) then
SET rango1=1;
SET rango2=((tProce-1)+rango1);
ELSE
if(s_anterior=0) then
SET rango1=res+1;
SET rango2=((tProce-1)+rango1);
ELSE
SET rango1=res;
SET rango2=((tProce-1)+rango1);
END IF;
END IF;

INSERT INTO `piezas_procesadas`(`registro_rbp_id_registro_rbp`, `linea`, `rango_canasta_1`, `rango_canasta_2`, 
`cantidad_piezas_procesadas`, `sobrante_inicial`, `piezasxfila`, `filas`, `niveles`, `canastas`, `niveles_completos`,
`filas_completas`,`cambioMOG`,`sobrante`,`cantPzaGood`,`sobra_fin`,`activo`,`dasiddas`) VALUES(id,line,rango1,rango2,totalpc,s_anterior,piexfila,filas,
niveles,canastas,nivelcompleto,filacom,mog,sobrante_f,pzab,sobF,1,das);

SET idpiepro=(SELECT MAX(idpiezas_procesadas) FROM piezas_procesadas);


SET r1=(SELECT piezas_procesadas.rango_canasta_1 from piezas_procesadas where 
registro_rbp_id_registro_rbp=id ORDER BY idpiezas_procesadas DESC LIMIT 1);

SET r2=(SELECT piezas_procesadas.rango_canasta_2 from piezas_procesadas where 
registro_rbp_id_registro_rbp=id ORDER BY idpiezas_procesadas DESC LIMIT 1);

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenar_ProdCoi` (IN `orden` VARCHAR(20), IN `totalP` DOUBLE, IN `sc` DOUBLE, IN `a_in` VARCHAR(20), IN `a_f` VARCHAR(20), IN `p_in` VARCHAR(20), IN `p_f` VARCHAR(20), IN `doble` BOOLEAN, IN `cant1` INT, IN `cant2` INT, IN `idas` INT, OUT `id` INT)  NO SQL BEGIN
DECLARE id_coi int;

SET id_coi=(SELECT orden_coil.id_orden_coil from orden_coil WHERE orden_coil.orden_coil=orden);

INSERT INTO `das_prod_coi`(`ordencoil_idordencoil`, `totalProd`, `scrap`, `aj_in`, `aj_fin`, `pro_in`, `pro_fin`, `doble_bobina`, `cant_tira1`, `cant_tira2`, `das_idas`) VALUES (id_coi,totalP,sc,a_in,a_f,p_in,p_f,doble,cant1,cant2,idas);

SET id=(SELECT max(das_prod_coi.id_dasprodcoi) from das_prod_coi);
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `login` (IN `codsur` VARCHAR(20), OUT `val` INT, OUT `process` VARCHAR(20), OUT `supervisor_name` VARCHAR(80))   BEGIN    
DECLARE cadena varchar(50);                      
SET cadena= ( SELECT codigo from empleado WHERE empleado.codigo_alea=codsur and (empleado.tipo_usuario='Supervisor' or empleado.tipo_usuario='Lider'));
SET process=(SELECT descripcion from procesos INNER JOIN empleado_supervisor on empleado_supervisor.procesos_idproceso=procesos.id_proceso INNER JOIN
empleado on empleado_supervisor.empleado_id_empleado=empleado.id_empleado where empleado.codigo_alea=codsur);
SET supervisor_name = (SELECT CONCAT(nombre_empleado," ",apellido) AS nombre_supv FROM empleado WHERE codigo_alea = codsur);
IF (cadena is null) THEN
SET val=0;
ELSE
SET val=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `loginAduana` (IN `codsp` INT, IN `work_center` VARCHAR(20), OUT `sies` INT, OUT `worker_name` VARCHAR(40), OUT `worker_name2` VARCHAR(40))  NO SQL BEGIN 
DECLARE id_em,id_proc int DEFAULT 0;
DECLARE proceso_nm varchar(20);
DECLARE tipo varchar(20);

SET id_em = (SELECT id_empleado FROM empleado WHERE codigo = codsp );

SET id_proc = (SELECT id_proceso FROM procesos WHERE descripcion = work_center);

SET tipo = (SELECT empleado.tipo_usuario from empleado WHERE empleado.id_empleado=id_em);

SET proceso_nm = (SELECT empleado.id_proceso from empleado WHERE empleado.id_empleado=id_em);

IF(id_em is null OR proceso_nm IS null)THEN 
SET sies = 0;
ELSE
if (tipo='Aduana') THEN 
SET sies = 1;
SET worker_name = (SELECT nombre_empleado FROM empleado WHERE codigo = codsp);
SET worker_name2 = (SELECT apellido FROM empleado WHERE codigo = codsp);
ELSE
SET sies = 0;  
END IF;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `loginSupervisor` (IN `codsp` VARCHAR(20), IN `work_center` VARCHAR(20), OUT `sies` INT, OUT `worker_name` VARCHAR(40), OUT `worker_name2` VARCHAR(40))  NO SQL BEGIN 
DECLARE id_em,id_proc int DEFAULT 0;
DECLARE proceso_nm varchar(20);

if(work_center='B/G PRENSA' || work_center='B/G FORMING' || work_center='B/G COINING' || work_center='B/G CHAMFER' || work_center='B/G GRINDING' || work_center='B/G ASSY' || work_center='B/G ASSY/SEAL RING')THEN

SET id_em = (SELECT id_empleado FROM empleado WHERE codigo_alea = codsp and (empleado.tipo_usuario='Supervisor' or empleado.tipo_usuario='Lider') and empleado.activo=1);

ELSE

SET id_em = (SELECT id_empleado FROM empleado WHERE (codigo_alea = codsp or codigo=codsp) and (empleado.tipo_usuario='Supervisor' or empleado.tipo_usuario='Lider') and empleado.activo=1);

end if;

SET id_proc = (SELECT id_proceso FROM procesos WHERE descripcion = work_center);

SET proceso_nm = (SELECT empleado.id_proceso from empleado WHERE empleado.id_empleado=id_em);

IF(id_em is null OR proceso_nm IS null)THEN 
SET sies = 0;
ELSE
SET sies = 1;
SET worker_name = (SELECT nombre_empleado FROM empleado WHERE (codigo_alea = codsp or codigo=codsp));
SET worker_name2 = (SELECT apellido FROM empleado WHERE (codigo_alea = codsp or codigo=codsp));
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ManufOrdFill` (IN `ordenMan` VARCHAR(50), OUT `exis` INT, IN `pro` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;

SET exis=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE orden_manufactura=ordenMan and registro_rbp.activo_op=1);

IF (exis is not null) THEN 
SET id=(SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.orden_manufactura=ordenMan);

SELECT mog.mog, mog.descripcion, mog.num_dibujo, registro_rbp.proceso, mog.no_parte, mog.modelo, mog.STD from mog INNER JOIN registro_rbp ON registro_rbp.mog_id_mog=mog.id_mog WHERE mog.id_mog=id and registro_rbp.proceso=pro;

ELSE
set exis=0;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `metros` (IN `ord` VARCHAR(20), IN `id2` INT)  NO SQL BEGIN

DECLARE id int;
DECLARE ti1 varchar(20);
DECLARE ti2 varchar(20);

SET ti1=(SELECT das_prod_bgprensa.ini_tp from das_prod_bgprensa WHERE das_prod_bgprensa.id_dasprodbgp=id2);

SET ti2=(SELECT das_prod_bgprensa.fin_tp from das_prod_bgprensa WHERE das_prod_bgprensa.id_dasprodbgp=id2);


SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=ord);

SELECT `lote_coil`.`lote_coil`, sum(metros),`lote_coil`.das_id_das FROM `lote_coil` WHERE `lote_coil`.`registro_rbp_id_registro_rbp`=id 
and lote_coil.tiempo_insercion BETWEEN ti1 and ti2 GROUP BY `lote_coil`.das_id_das;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `minutosParosLinea` (IN `line` VARCHAR(20), IN `mogg` VARCHAR(20), OUT `tot` INT)  NO SQL BEGIN
DECLARE idmo int;
DECLARE total int;

SET idmo=(SELECT mog.id_mog FROM mog WHERE mog.mog=mogg);

SET total=(SELECT SUM(registrocausasparo.tiempo) AS totaltiempo FROM `registrocausasparo` WHERE registrocausasparo.mog_idmog=idmo and registrocausasparo.linea=line GROUP BY registrocausasparo.linea);

if(total > 0) THEN

SET tot=total;
else
SET tot=0;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `mogEmpaMes` (IN `id` INT)  NO SQL BEGIN

SELECT mog, no_parte, STD from mog WHERE mog.id_mog=id;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `mogPrensa` (IN `id` INT)  NO SQL BEGIN

SELECT mog, modelo, STD from mog WHERE mog.id_mog=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `nombreCierreOrden` (IN `codsur` VARCHAR(20))  NO SQL BEGIN
SELECT empleado.nombre_empleado, empleado.apellido from empleado WHERE empleado.codigo_alea=codsur;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ObtenerCausasParoPorProceso` (IN `proceso_id` INT)   BEGIN
  SELECT *
  FROM causas_paro
  WHERE procesos_idproceso = proceso_id;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ObtenerEmpleadoPorCodigo` (IN `codigo_empleado` INT)   BEGIN
    SELECT
        e.nombre_empleado, 
        e.apellido, 
        e.codigo, 
        e.codigo_alea, 
        e.tipo_usuario 
    FROM 
        empleado AS e 
    WHERE 
        e.codigo = codigo_empleado;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ObtenerFechaActual` ()   BEGIN
  SELECT NOW();
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ObtenerFechaActualFecha` ()   BEGIN
  SELECT CURDATE();
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ObtenerFechaActualHora` ()   BEGIN
  SELECT CURTIME();
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `obtenerIdDasPCK` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL BEGIN

SELECT das.id_das from das WHERE 
(das.linea='TI30' or das.linea='TI29' or das.linea='TI28') and das.fecha BETWEEN fecha1 and fecha2;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `obtenerSecuencia` (IN `mogg` VARCHAR(20))  NO SQL BEGIN
SELECT registro_rbp.proceso from registro_rbp INNER JOIN mog on mog.id_mog=registro_rbp.mog_id_mog WHERE mog=mogg;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `obtenersobraFin` (IN `orden` VARCHAR(20))  NO SQL BEGIN

DECLARE idrg int;
DECLARE idpi int;

set idrg=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

set idpi=(SELECT MAX(piezas_procesadas.idpiezas_procesadas) from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=idrg);

SELECT piezas_procesadas.sobra_fin from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=idrg and piezas_procesadas.idpiezas_procesadas=idpi;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `obtenerSobranteInicial` (IN `orden` VARCHAR(20), IN `proceso` VARCHAR(20), OUT `pieGraMas` INT, OUT `pieGraMenos` INT)  NO SQL BEGIN
DECLARE id,sies,idpie int;
set sies = (select strcmp(proceso,'GRADING'));

if (sies = 0) then 

SET id=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SELECT piezas_procesadas.sobrante from piezas_procesadas where registro_rbp_id_registro_rbp=id 
ORDER BY idpiezas_procesadas DESC LIMIT 1;

SET idpie=(SELECT piezas_procesadas.idpiezas_procesadas from piezas_procesadas where piezas_procesadas.registro_rbp_id_registro_rbp= 
id order by idpiezas_procesadas DESC LIMIT 1);

SET pieGraMas=(SELECT piezas_procesadas_grading.sobrante from piezas_procesadas_grading where 
piezas_procesadas_grading.id_piezasProcesadas=idpie and piezas_procesadas_grading.stdE='+OK');

SET pieGraMenos=(SELECT piezas_procesadas_grading.sobrante from piezas_procesadas_grading where 
piezas_procesadas_grading.id_piezasProcesadas=idpie and piezas_procesadas_grading.stdE='-OK');

ELSEIF(proceso like '%MAQUINADO%') then

SET id=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SELECT piezas_procesadas.sobra_fin as sobrante from piezas_procesadas where registro_rbp_id_registro_rbp=id 
ORDER BY idpiezas_procesadas DESC LIMIT 1;

ELSE
SET id=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SELECT piezas_procesadas.sobrante from piezas_procesadas where registro_rbp_id_registro_rbp=id 
ORDER BY idpiezas_procesadas DESC LIMIT 1;
end if;


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ObtenerSupervisorAsignado` (IN `p_codigo_supervisor` INT, IN `p_codigo_maquina` VARCHAR(80), OUT `p_nombre_completo` VARCHAR(160), OUT `p_codigo_maquina_out` VARCHAR(80), OUT `p_nombre_work_center` VARCHAR(80))   BEGIN
    SELECT 
        CONCAT(nombre_empleado, ' ', apellido),
        codigo_maquina,
        nombre_work_center
    INTO
        p_nombre_completo,
        p_codigo_maquina_out,
        p_nombre_work_center
    FROM 
        empleado
    INNER JOIN 
        empleado_supervisor ON empleado_supervisor.empleado_id_empleado = empleado.id_empleado
    INNER JOIN 
        work_center_maquina ON work_center_maquina.empleado_supervisor_id_empleado_supervisor = empleado_supervisor.id_empleado_supervisor
    WHERE 
        empleado_supervisor.empleado_id_empleado = p_codigo_supervisor 
        AND work_center_maquina.codigo_maquina = p_codigo_maquina;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `obtener_piezas_x_hora_maq` (IN `id_rbp` INT)   BEGIN

SELECT hora,
	cantidadxhora,
	acumulado,
	ok_ng,
	empleado.nombre_empleado
FROM registro_x_hora_maq AS rxhmq
INNER JOIN empleado ON empleado.id_empleado = rxhmq.empleado_id_empleado
WHERE rxhmq.registro_rbp_id_registro_rbp = id_rbp
ORDER BY hora;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `obtener_registro_x_hora` (IN `id_rbp` INT)   BEGIN
    SELECT
        hora,
        cantidadxhora,
        acumulado,
        comentarios, 
        fecha
    FROM
        registro_x_hora as rxh
    WHERE
        rxh.registro_rbp_id_registro_rbp = id_rbp
    ORDER BY
        hora;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ordenAprobadas` (IN `mogg` VARCHAR(20))  NO SQL SELECT piezas_procesadas_fg.id_piezas_procesadas_fg, piezas_procesadas_fg.total_piezas_aprobadas, registro_rbp.orden_manufactura FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog WHERE mog.mog=mogg ORDER BY id_piezas_procesadas_fg$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ordenarMaquina` (IN `line` VARCHAR(20))  NO SQL BEGIN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog from registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE piezas_procesadas.linea=line and registro_rbp.estado=1 and registro_rbp.activo_op=0 GROUP by registro_rbp.orden_manufactura;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ordenesActualesBush` ()  NO SQL BEGIN 
DECLARE idblank int;
DECLARE idform int;
DECLARE idcoini int;
DECLARE idchamf int;
DECLARE idbgrin int;
DECLARE idbgrin1 int;
DECLARE idbgrin2 int;
DECLARE idbgrin3 int;
DECLARE idbgrin4 int;
DECLARE idbgrin5 int;
DECLARE idbgrin6 int;
DECLARE idbgrin7 int;
DECLARE idbgrin8 int;
DECLARE idbgrin9 int;
DECLARE idbgrin10 int;
DECLARE idbgrin11 int;
DECLARE idbgrin12 int;

SET idblank=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB02B' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idform=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB02F' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idcoini=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB31' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idchamf=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB51' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB71' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin1=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB03F' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin2=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB06' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin3=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB01' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin4=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB03B' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin5=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB05' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin6=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB06' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin7=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB32' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin8=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB92'AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin9=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TB91' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin10=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TI30' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin11=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TI29' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SET idbgrin12=(SELECT MAX(corriendoactualmente.id_corriendo) FROM corriendoactualmente WHERE corriendoactualmente.linea='TI28' AND corriendoactualmente.activo=1 and corriendoactualmente.fecha = CURRENT_DATE());

SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idblank UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idform UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idcoini UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idchamf UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin1 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin2 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin3 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin4 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin5 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin6 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin7 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin8 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin9 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin10 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin11 UNION
SELECT linea, hora_inicio, mog.no_parte, ordenActual AS ord1, mogActual FROM corriendoactualmente inner JOIN mog ON mog.mog=corriendoactualmente.mogActual WHERE
corriendoactualmente.id_corriendo=idbgrin12;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ordenRegistrada` (IN `filtro` VARCHAR(50))  NO SQL SELECT registro_rbp.orden_manufactura, mog.mog FROM registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog WHERE mog.mog LIKE filtro$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `parosLineaFab` (IN `idDas` INT, IN `orden` VARCHAR(30))  NO SQL BEGIN

DECLARE idmog int;

SET idmog=(SELECT mog.id_mog FROM mog WHERE mog.mog=orden);

SELECT registrocausasparo.hora_inicio, registrocausasparo.tiempo, causas_paro.numero_causas_paro, registrocausasparo.detalle from registrocausasparo INNER JOIN causas_paro on causas_paro.idcausas_paro=registrocausasparo.causas_paro_idcausas_paro
WHERE registrocausasparo.das_id_das=idDas and registrocausasparo.mog_idmog=idmog;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `permitirSiguiente` (IN `mogo` VARCHAR(20), OUT `sino` INT, OUT `idpf` INT)  NO SQL BEGIN

DECLARE id int;

SET id=(SELECT MAX(piezas_procesadas_fg.id_piezas_procesadas_fg) AS idfg FROM piezas_procesadas_fg 
INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=
piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog on 
mog.id_mog=registro_rbp.mog_id_mog WHERE registro_rbp.activo_op=0
and registro_rbp.estado=0 AND registro_rbp.aduana=0 and mog.mog=mogo);

if(id is NOT null) THEN
SET sino=1;
SET idpf=(SELECT MAX(piezas_procesadas_fg.id_piezas_procesadas_fg) AS idfg FROM piezas_procesadas_fg 
INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=
piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog on 
mog.id_mog=registro_rbp.mog_id_mog WHERE registro_rbp.activo_op=0 
and registro_rbp.estado=0 AND registro_rbp.aduana=0 and mog.mog=mogo);
ELSE
SET sino=0;
SET idpf=0;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `pesoPieza` (IN `parte` VARCHAR(50), IN `orden` VARCHAR(50))   BEGIN

select mog.peso from mog where mog.no_parte=parte and mog.mog=orden;


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `piezasProGrading` (IN `estandar` VARCHAR(10), IN `sobra_inicial` INT, IN `piexfila` INT, IN `fila` INT, IN `nivel` INT, IN `canast` INT, IN `niveles_com` INT, IN `fila_comple` INT, IN `sobra` INT, IN `id_piezaPro` INT, IN `sobra_f` INT)   BEGIN
DECLARE a1 int;
DECLARE a2 int;
DECLARE a3 int;
DECLARE tt int;
DECLARE pzab int;

SET a1=(piexfila*fila*nivel*canast);
SET a2=(piexfila*fila*niveles_com);
SET a3=((piexfila*fila_comple)+sobra_f);
SET tt=(a1+a2+a3);
SET pzab=(tt-sobra_inicial);

insert INTO piezas_procesadas_grading (stdE,sobrante_inicial,piezasxfila,filas,nivel,canastas,
niveles_completos,filas_completas,sobrante,cant_piezas_buenas,id_piezasProcesadas) 
values(estandar,sobra,piexfila,fila,nivel,canast,niveles_com,fila_comple,sobra_f,pzab,id_piezaPro);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `predatos` (IN `moggg` VARCHAR(20), OUT `mg` VARCHAR(20), OUT `num_part` VARCHAR(20), OUT `lot` VARCHAR(20), OUT `orden` VARCHAR(20), OUT `WC` VARCHAR(20), OUT `piezaspro` INT, OUT `piezasgood` INT, IN `ultm` VARCHAR(20), OUT `fech` DATE)  NO SQL BEGIN

DECLARE idmog int;
declare id_orden1 int;
declare id_orden2 int;
DECLARE idMax2 int;

SET idmog=(SELECT mog.id_mog from mog WHERE mog.mog = concat('MOG0','',moggg));

SET mg=(SELECT mog.mog from mog WHERE mog.id_mog=idmog);

set num_part=(SELECT mog.no_parte from mog WHERE mog.id_mog=idmog);

SET id_orden1=(SELECT registro_rbp.id_registro_rbp FROM `registro_rbp` WHERE registro_rbp.mog_id_mog=idmog and registro_rbp.orden_manufactura like '%BFO%');

SET orden=(SELECT registro_rbp.orden_manufactura from registro_rbp WHERE registro_rbp.id_registro_rbp=id_orden1);

SET lot=(SELECT registro_rbp.loteTM from registro_rbp  WHERE registro_rbp.id_registro_rbp=id_orden1);

set WC=(SELECT piezas_procesadas.linea from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=id_orden1 LIMIT 1);

SET id_orden2=(SELECT `registro_rbp`.id_registro_rbp FROM `registro_rbp` WHERE `registro_rbp`.mog_id_mog=idmog and registro_rbp.proceso=CONCAT('B/G'," ",ultm));

set idMax2=(SELECT MAx(tiempo.id_tiempos) from tiempo WHERE tiempo.registro_rbp_id_registro_rbp=id_orden2);

set fech=(SELECT tiempo.fecha from tiempo WHERE tiempo.registro_rbp_id_registro_rbp=id_orden2 and tiempo.id_tiempos=idMax2);

SET piezaspro=(SELECT piezas_procesadas_fg.totalpiezas_procesadas from piezas_procesadas_fg WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=id_orden1);

set piezasgood=(SELECT piezas_procesadas_fg.total_piezas_aprobadas from piezas_procesadas_fg WHERE piezas_procesadas_fg.registro_rbp_id_registro_rbp=id_orden2);


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `pruebaReporteAsakaiFabPrensa` (IN `ddd` INT, OUT `pro` INT)  NO SQL BEGIN

DECLARE proba varchar(20);

set proba=(SELECT sum(das_prod_bgprensa.pcs_pro) from das_prod_bgprensa WHERE das_prod_bgprensa.das_id_das=ddd );

if(proba is null) then
set pro=0;
else
set pro=1;
SELECT das.linea, das.fecha, das.turno,  sum(das_prod_bgprensa.pcs_pro), sum(das_prod_bgprensa.pcs_buenas),   sum(das_prod_bgprensa.pcs_scrap),sum(das_prod_bgprensa.pcs_bm)
from das 
INNER JOIN das_prod_bgprensa on das_prod_bgprensa.das_id_das=das.id_das  WHERE das.id_das=ddd GROUP by das.turno;

end if;
end$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `pruebaReporteAsakaiFabProces` (IN `ddd` INT, OUT `pro` INT)  NO SQL BEGIN

DECLARE proba varchar(20);

set proba=(SELECT sum(das_prod_bgproceso.pcs_pro_bush) from das_prod_bgproceso where das_prod_bgproceso.das_iddas=ddd );

if(proba is null) then
set pro=0;
else
set pro=1;

SELECT das.linea, das.fecha, das.turno, 
sum(das_prod_bgproceso.pcs_pro_bush), sum(das_prod_bgproceso.pcs_buen_bush),   sum(das_prod_bgproceso.scrap_bush), 
sum(das_prod_bgproceso.scrp_rg), 
sum(das_prod_bgproceso.scrap_sr)
from das 
INNER JOIN das_prod_bgproceso on das_prod_bgproceso.das_iddas=das.id_das WHERE das_prod_bgproceso.das_iddas=ddd GROUP BY das.turno;

end IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `pzabuenasLinea` (IN `fec1` VARCHAR(20), IN `fec2` VARCHAR(20))  NO SQL BEGIN
SELECT piezas_procesadas.linea, SUM(piezas_procesadas.cantPzaGood) as total, tiempo.fecha from piezas_procesadas LEFT JOIN tiempo on tiempo.id_tiempos=piezas_procesadas.idpiezas_procesadas WHERE tiempo.fecha BETWEEN fec1 and fec2 GROUP BY tiempo.fecha,piezas_procesadas.linea;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `rbp` ()   BEGIN 
SELECT * FROM registro_rbp WHERE estado=1 and activo_op=0;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `referenciaReport` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.id_das, das.linea, das.turno from das WHERE das.fecha BETWEEN fechaini and fechafin and das.linea <> 'TI30' and das.linea <> 'TI28' and das.linea <> 'TG03' and das.linea <> 'TI29' GROUP BY das.linea ORDER BY das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `registroxhora` (IN `mogg` VARCHAR(20), IN `orden` VARCHAR(20), IN `hor` VARCHAR(10), IN `cantxh` INT, IN `acum` INT, IN `comen` VARCHAR(1000), IN `fecha` DATE, IN `linea` VARCHAR(20))  NO SQL BEGIN

DECLARE idm int;
DECLARE idor int;

SET idm=(SELECT mog.id_mog FROM mog WHERE mog.mog=mogg);
SET idor=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

INSERT INTO `registro_x_hora`(`hora`, `cantidadxhora`, `acumulado`, `comentarios`, `fecha`, `mog_id_mog`, `registro_rbp_id_registro_rbp`, `linea`)VALUES(hor,cantxh,acum,comen,fecha,idm,idor, linea);
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `registro_x_hora_maq` (IN `mog_ingresada` VARCHAR(20), IN `orden_ingresada` VARCHAR(20), IN `num_empleado_ingresado` VARCHAR(20), IN `hora_p` VARCHAR(10), IN `cantxh_p` INT, IN `acum_p` INT, IN `calidad` VARCHAR(2), IN `fecha_p` DATE, IN `linea_p` VARCHAR(20))   BEGIN

DECLARE idmog int;
DECLARE idorden int;
DECLARE idempleado int;

SET idmog=(SELECT mog.id_mog FROM mog WHERE mog.mog=mog_ingresada);
SET idorden=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden_ingresada);
SET idempleado=(SELECT empleado.id_empleado FROM empleado WHERE empleado.codigo_alea=num_empleado_ingresado);

INSERT INTO `registro_x_hora_maq`(`hora`, `cantidadxhora`, `acumulado`, `ok_ng`, `fecha`, `linea`, `mog_id_mog`, `registro_rbp_id_registro_rbp`, `empleado_id_empleado`)
VALUES(hora_p,cantxh_p,acum_p,calidad,fecha_p,linea_p,idmog,idorden,idempleado);
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporte` (IN `che` INT, IN `fecha1` VARCHAR(20), IN `fecha2` VARCHAR(20))  NO SQL BEGIN
if(che=0)THEN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog, piezas_procesadas_fg.total_piezas_aprobadas, piezas_procesadas_fg.total_scrap, MAX(tiempo.fecha) as fe from registro_rbp INNER JOIN piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2;
END IF;
if(che=1)THEN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog, piezas_procesadas_fg.total_scrap, MAX(tiempo.fecha) as fe from registro_rbp INNER JOIN piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2;
END IF;
if(che=2)THEN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog, piezas_procesadas_fg.total_piezas_aprobadas, MAX(tiempo.fecha) as fe from registro_rbp INNER JOIN piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporte2` (IN `che` INT, IN `fecha1` VARCHAR(20), IN `fecha2` VARCHAR(20))  NO SQL BEGIN
if(che=0)THEN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog, CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nombreOpe,razon_rechazo.nombre_rechazo, defecto.cantidad_defecto, tiempo.fecha from registro_rbp INNER JOIN defecto on defecto.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN empleado_has_registro_rbp on empleado_has_registro_rbp.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN empleado ON empleado.id_empleado=empleado_has_registro_rbp.empleado_id_empleado INNER JOIN razon_rechazo ON razon_rechazo.id_razon_rechazo=defecto.razon_rechazo_id_razon_rechazo INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2;
END IF;
if(che=1)THEN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog, piezas_procesadas.linea,razon_rechazo.nombre_rechazo, defecto.cantidad_defecto, tiempo.fecha from registro_rbp INNER JOIN defecto on defecto.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN razon_rechazo ON razon_rechazo.id_razon_rechazo=defecto.razon_rechazo_id_razon_rechazo INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2;
END IF;
if(che=2) THEN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog, CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nombreSupe,razon_rechazo.nombre_rechazo, defecto.cantidad_defecto, tiempo.fecha from registro_rbp INNER JOIN defecto on defecto.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN empleado_has_registro_rbp on empleado_has_registro_rbp.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN empleado ON empleado.id_empleado=empleado_has_registro_rbp.empleado_supervisor_id_empleado_supervisor INNER JOIN razon_rechazo ON razon_rechazo.id_razon_rechazo=defecto.razon_rechazo_id_razon_rechazo INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2;
END IF;
if(che=3) THEN
SELECT registro_rbp.orden_manufactura, registro_rbp.mog, CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nombreCierra,razon_rechazo.nombre_rechazo, defecto.cantidad_defecto, tiempo.fecha from registro_rbp INNER JOIN defecto on defecto.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN empleado ON empleado.id_empleado=piezas_procesadas_fg.id_empleado INNER JOIN razon_rechazo ON razon_rechazo.id_razon_rechazo=defecto.razon_rechazo_id_razon_rechazo INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `Reporteador` (IN `filtromog` VARCHAR(30), IN `filtroarticulo` VARCHAR(30), IN `fecha1` VARCHAR(10), IN `fecha2` VARCHAR(10), IN `filtrpnumpar` VARCHAR(30))  NO SQL BEGIN
DECLARE  sit varchar (30);
DECLARE  si2 varchar (30);
DECLARE  si3 varchar (30);

if(filtromog is null) THEN
SET sit='';
ELSE 
SET sit=filtromog;
END IF;

if(filtroarticulo is null) THEN
SET si2='';
ELSE 
SET si2=filtroarticulo;
END IF;

if(filtrpnumpar is null) THEN
SET si3='';
ELSE 
SET si3=filtrpnumpar;
END IF;

SELECT tiempo.fecha, mog.mog, mog.descripcion, registro_rbp.orden_manufactura,registro_rbp.proceso,
piezas_procesadas.linea,mog.num_dibujo,mog.no_parte, piezas_procesadas_fg.total_piezas_aprobadas,piezas_procesadas_fg.total_piezas_recibidas,piezas_procesadas_fg.totalpiezas_procesadas,
piezas_procesadas_fg.cambio_mog,piezas_procesadas_fg.verificacion,
piezas_procesadas_fg.total_scrap, piezas_procesadas_fg.id_empleado as operador_orden, 
empleado_has_registro_rbp.empleado_supervisor_id_empleado_supervisor as supervisor 
FROM piezas_procesadas_fg INNER JOIN registro_rbp 
ON registro_rbp.id_registro_rbp = piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog 
on mog.id_mog = registro_rbp.mog_id_mog INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=
registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on 
piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN empleado_has_registro_rbp on empleado_has_registro_rbp.registro_rbp_id_registro_rbp=
registro_rbp.id_registro_rbp WHERE mog.descripcion LIKE filtroarticulo AND mog.mog LIKE filtromog and 
mog.no_parte LIKE filtrpnumpar and tiempo.fecha BETWEEN fecha1 AND fecha2 AND registro_rbp.activo_op=0 AND registro_rbp.estado=0 GROUP BY registro_rbp.orden_manufactura ORDER BY registro_rbp.proceso ,tiempo.fecha;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ReporteAnselmo` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
registro_rbp.orden_manufactura, 
mog.no_parte, 
piezas_procesadas.linea, 
tiempo.fecha, 
MIN(piezas_procesadas.idpiezas_procesadas), piezas_procesadas.sobrante_inicial,
piezas_procesadas_fg.sobrante_final,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas,  
SUM(tiempo.horas_trabajadas)

FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha and registro_rbp.aduana=0
BETWEEN fecha1 AND fecha2 GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ReporteAnselmoOP` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
registro_rbp.orden_manufactura, 
mog.no_parte, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
MIN(piezas_procesadas.idpiezas_procesadas), piezas_procesadas.sobrante_inicial,
piezas_procesadas_fg.sobrante_final,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas  

FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
WHERE tiempo.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%BFO%' or registro_rbp.orden_manufactura  LIKE '%BCO%' or registro_rbp.orden_manufactura  LIKE '%BCH%' or registro_rbp.orden_manufactura  LIKE '%BGR%' or registro_rbp.orden_manufactura  LIKE '%PLT%' or registro_rbp.orden_manufactura  LIKE '%PCK%' or registro_rbp.orden_manufactura  LIKE '%ASL%') 
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ReporteAnselmoPrensa` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
registro_rbp.orden_manufactura, 
mog.no_parte, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
MIN(piezas_procesadas.idpiezas_procesadas), piezas_procesadas.sobrante_inicial,
piezas_procesadas_fg.sobrante_final,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 

WHERE tiempo.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and registro_rbp.orden_manufactura  LIKE '%BHL%'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraBush` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog, 
registro_rbp.orden_manufactura, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas,
cerradoordenes.id_cerrado, cerradoordenes.fecha

FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE cerradoordenes.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and registro_rbp.orden_manufactura  LIKE '%BFO%' and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraEmpaque` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog as mog2, 
registro_rbp.orden_manufactura as po2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
MIN(piezas_procesadas.idpiezas_procesadas), piezas_procesadas.sobrante_inicial,
piezas_procesadas_fg.sobrante_final,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas,
piezas_procesadas_fg.total_scrap,
piezas_procesadas_fg.verificacion2,
piezas_procesadas_fg.verificacion,
cerradoordenes.id_cerrado,
cerradoordenes.fecha
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE cerradoordenes.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%PCK%' or registro_rbp.orden_manufactura  LIKE '%ASL%') and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraEmpaque2` (IN `mg` VARCHAR(20))  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog as mog2, 
registro_rbp.orden_manufactura as po2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
MIN(piezas_procesadas.idpiezas_procesadas), piezas_procesadas.sobrante_inicial,
piezas_procesadas_fg.sobrante_final,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas,
piezas_procesadas_fg.total_scrap,
piezas_procesadas_fg.verificacion2,
cerradoordenes.id_cerrado,
cerradoordenes.fecha
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE mog.mog like mg and
registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%PCK%' or registro_rbp.orden_manufactura  LIKE '%ASL%') and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraEmpaqueSC` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog as mog2, 
registro_rbp.orden_manufactura as po2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
MIN(piezas_procesadas.idpiezas_procesadas), piezas_procesadas.sobrante_inicial,
piezas_procesadas_fg.sobrante_final,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas, piezas_procesadas_fg.total_piezas_recibidas, 
piezas_procesadas_fg.total_scrap,
piezas_procesadas_fg.verificacion2,
cerradoordenes.id_cerrado,
cerradoordenes.fecha
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE cerradoordenes.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%PCK%' or registro_rbp.orden_manufactura  LIKE '%ASL%') and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraMaquinado` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog as mog2, 
registro_rbp.orden_manufactura as po2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
piezas_procesadas_fg.total_piezas_recibidas, piezas_procesadas_fg.total_piezas_aprobadas,
piezas_procesadas_fg.total_scrap,
piezas_procesadas_fg.verificacion,
piezas_procesadas_fg.verificacion2,
cerradoordenes.id_cerrado,
cerradoordenes.fecha
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE cerradoordenes.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%HBL%' or                   registro_rbp.orden_manufactura  LIKE '%PLT%') and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraMaquinado2` (IN `pro` VARCHAR(20))  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog as mog2, 
registro_rbp.orden_manufactura as po2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
piezas_procesadas_fg.total_piezas_recibidas, piezas_procesadas_fg.total_piezas_aprobadas,
piezas_procesadas_fg.total_scrap,
piezas_procesadas_fg.verificacion,
piezas_procesadas_fg.verificacion2,
cerradoordenes.id_cerrado,
cerradoordenes.fecha
FROM piezas_procesadas_fg 
INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp 
INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog 
INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp

WHERE mog.mog like pro and registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%HBL%' or registro_rbp.orden_manufactura  LIKE '%PLT%') and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraPlatinado` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog as MOG1, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura as PO1, 
mog.mog as MOG2, 
registro_rbp.orden_manufactura as PO2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas, cerradoordenes.id_cerrado, cerradoordenes.fecha  

FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE cerradoordenes.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and registro_rbp.orden_manufactura  LIKE '%PLT%' and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBitacoraPrensa` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog as mog2, 
registro_rbp.orden_manufactura as po2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha, piezas_procesadas_fg.total_piezas_aprobadas, cerradoordenes.id_cerrado, cerradoordenes.fecha,
piezas_procesadas_fg.total_scrap, piezas_procesadas_fg.totalpiezas_procesadas
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE cerradoordenes.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and (      registro_rbp.orden_manufactura  LIKE '%PRS%') and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ReporteBitacoraPrensaFiltro` (IN `mg` VARCHAR(20))  NO SQL SELECT mog.mog, 
mog.no_parte, 
registro_rbp.loteTM,
registro_rbp.orden_manufactura, 
mog.mog as mog2, 
registro_rbp.orden_manufactura as po2, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha, piezas_procesadas_fg.total_piezas_aprobadas, cerradoordenes.id_cerrado, cerradoordenes.fecha , piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_scrap
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
INNER JOIN cerradoordenes on cerradoordenes.id_registro_rbp=registro_rbp.id_registro_rbp
WHERE mog.mog=(concat('MOG0','',mg)) and registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%BHL%' or      registro_rbp.orden_manufactura  LIKE '%PRS%') and cerradoordenes.tipo_liberacion='Aduana'
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteBush` ()  NO SQL SELECT mog.mog, registro_rbp.orden_manufactura, mog.no_parte, piezas_procesadas_fg.total_piezas_aprobadas, piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_scrap from piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog = registro_rbp.mog_id_mog WHERE registro_rbp.estado=0 and registro_rbp.activo_op=0 AND registro_rbp.proceso='B/G PRENSA' OR registro_rbp.proceso='B/G FORMING' OR registro_rbp.proceso='B/G COINING' OR registro_rbp.proceso='B/G CHAMFER' OR registro_rbp.proceso='B/G GRINDING' OR registro_rbp.proceso='B/G EMPAQUE' OR registro_rbp.proceso='B/G PLATINADO' OR registro_rbp.proceso='B/G ASSY' GROUP by registro_rbp.orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteColoresAduana` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha FROM das WHERE fecha BETWEEN fechaini and fechafin GROUP BY das.fecha, das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGAssy91` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgproceso.ini_cm, das_prod_bgproceso.fin_cm, 
das_prod_bgproceso.ini_tp, das_prod_bgproceso.fin_tp, das_prod_bgproceso.pcs_pro_bush, das_prod_bgproceso.pcs_pro_rg,
das_prod_bgproceso.pcs_buen_bush, das_prod_bgproceso.pcs_buen_rg, das_prod_bgproceso.scrap_bush, das_prod_bgproceso.lote_bush, das_prod_bgproceso.scrp_rg FROM das_prod_bgproceso INNER JOIN mog ON mog.id_mog=das_prod_bgproceso.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas 
WHERE das.fecha BETWEEN fechaini AND fechafin AND das.linea='TB91' ORDER BY das.fecha, das.turno, das_prod_bgproceso.ini_tp, mog.mog, das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGAssy91v2` (IN `fechaini` DATE, IN `fechafin` DATE, OUT `res` INT)  NO SQL BEGIN

DECLARE pur varchar(20);
set pur=(SELECT  das_prod_bgproceso.ini_cm FROM das_prod_bgproceso  INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas 
WHERE das.fecha BETWEEN fechaini AND fechafin AND das.linea='TB91'  limit 1);

if (pur is not null) THEN

SELECT das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgproceso.ini_cm, das_prod_bgproceso.fin_cm, 
das_prod_bgproceso.ini_tp, das_prod_bgproceso.fin_tp, das_prod_bgproceso.pcs_pro_bush, das_prod_bgproceso.pcs_pro_rg,
das_prod_bgproceso.pcs_buen_bush, das_prod_bgproceso.pcs_buen_rg, das_prod_bgproceso.scrap_bush, das_prod_bgproceso.scrp_rg FROM das_prod_bgproceso INNER JOIN mog ON mog.id_mog=das_prod_bgproceso.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas 
WHERE das.fecha BETWEEN fechaini AND fechafin AND das.linea='TB91' ORDER BY das.fecha, das.turno, das_prod_bgproceso.ini_tp, mog.mog, das.linea;

set res=1;

ELSE

set res=0;

END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGAssy92` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgproceso.ini_cm, das_prod_bgproceso.fin_cm, das_prod_bgproceso.ini_tp, das_prod_bgproceso.fin_tp, das_prod_bgproceso.pcs_pro_bush, das_prod_bgproceso.pcs_pro_rg, das_prod_bgproceso.pcs_pro_sr, das_prod_bgproceso.pcs_buen_bush, das_prod_bgproceso.scrap_bush, das_prod_bgproceso.scrp_rg, das_prod_bgproceso.lote_bush, das_prod_bgproceso.scrap_sr FROM das_prod_bgproceso INNER JOIN mog ON mog.id_mog=das_prod_bgproceso.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas WHERE das.fecha BETWEEN fechaini AND fechafin AND das.linea='TB92' ORDER BY das.fecha,das_prod_bgproceso.ini_tp, mog.mog, das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGAssy92v2` (IN `fechaini` DATE, IN `fechafin` DATE, OUT `res` INT)  NO SQL BEGIN

DECLARE pur varchar(20);

set pur=(SELECT das_prod_bgproceso.ini_tp FROM das_prod_bgproceso  INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas WHERE das.fecha BETWEEN fechaini AND fechafin AND das.linea='TB92' limit 1);

if (pur is not null) then

SELECT das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgproceso.ini_cm, das_prod_bgproceso.fin_cm, das_prod_bgproceso.ini_tp, das_prod_bgproceso.fin_tp, das_prod_bgproceso.pcs_pro_bush, das_prod_bgproceso.pcs_pro_rg, das_prod_bgproceso.pcs_pro_sr, das_prod_bgproceso.pcs_buen_bush, das_prod_bgproceso.scrap_bush, das_prod_bgproceso.scrp_rg, das_prod_bgproceso.scrap_sr FROM das_prod_bgproceso INNER JOIN mog ON mog.id_mog=das_prod_bgproceso.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas WHERE das.fecha BETWEEN fechaini AND fechafin AND das.linea='TB92' ORDER BY das.fecha,das_prod_bgproceso.ini_tp, mog.mog, das.linea;

set res=1;

ELSE
set res=0;

END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGEmpaque` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha, das.turno, mog.mog, mog.no_parte, das_produ_empamesas.ini_tu, das_produ_empamesas.fin_tu, das_produ_empamesas.cant_proce, 
das_produ_empamesas.pza_buenas, das_produ_empamesas.pza_Sort, das_produ_empamesas.Sobrante_Final FROM das_produ_empamesas INNER JOIN mog ON mog.id_mog=
das_produ_empamesas.mog_idmog INNER JOIN das ON das.id_das = das_produ_empamesas.das_iddas WHERE das.fecha BETWEEN fechaini AND fechafin ORDER BY das.fecha,das_produ_empamesas.ini_tu, mog.mog, das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGEmpaque2` (IN `fechaini` DATE, IN `fechafin` DATE, OUT `res` INT)  NO SQL BEGIN

DECLARE dato varchar(20);

set dato=(SELECT das_produ_empamesas.ini_tu FROM das_produ_empamesas INNER JOIN das ON das.id_das = das_produ_empamesas.das_iddas WHERE das.fecha BETWEEN fechaini AND fechafin LIMIT 1);

if (dato is not null) then

SELECT das.linea, das.fecha, das.turno, mog.mog, mog.no_parte, das_produ_empamesas.ini_tu, das_produ_empamesas.fin_tu, das_produ_empamesas.cant_proce, 
das_produ_empamesas.pza_buenas, das_produ_empamesas.pza_Sort, das_produ_empamesas.Sobrante_Final FROM das_produ_empamesas INNER JOIN mog ON mog.id_mog=
das_produ_empamesas.mog_idmog INNER JOIN das ON das.id_das = das_produ_empamesas.das_iddas WHERE das.fecha BETWEEN fechaini AND fechafin ORDER BY das.fecha,das_produ_empamesas.ini_tu, mog.mog, das.linea;
SET res=1;
ELSE
set res=0;
end if;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGGeneral` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgproceso.ini_cm, das_prod_bgproceso.fin_cm, 
das_prod_bgproceso.ini_tp, das_prod_bgproceso.fin_tp, das_prod_bgproceso.pcs_pro_bush, 
das_prod_bgproceso.pcs_buen_bush, das_prod_bgproceso.scrap_bush, das_prod_bgproceso.scrp_rg, das_prod_bgproceso.lote_bush,
das_prod_bgproceso.scrap_sr FROM das_prod_bgproceso INNER JOIN mog ON mog.id_mog=
das_prod_bgproceso.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas 
WHERE das.fecha BETWEEN fechaini AND fechafin and das.linea <> 'TB91' and das.linea <> 'TB92' ORDER BY das.linea, das_prod_bgproceso.ini_tp,  das.fecha, mog.mog$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGGeneral2` (IN `fechaini` DATE, IN `fechafin` DATE, OUT `res` INT)  NO SQL BEGIN

DECLARE pur varchar(20);

set pur=(SELECT das_prod_bgproceso.ini_tp FROM das_prod_bgproceso  INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas 
WHERE das.fecha BETWEEN fechaini AND fechafin and das.linea <> 'TB91' and das.linea <> 'TB92' LIMIT 1);
if ( pur is not null) THEN

SELECT das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgproceso.ini_cm, das_prod_bgproceso.fin_cm, 
das_prod_bgproceso.ini_tp, das_prod_bgproceso.fin_tp, das_prod_bgproceso.pcs_pro_bush, 
das_prod_bgproceso.pcs_buen_bush, das_prod_bgproceso.scrap_bush, das_prod_bgproceso.scrp_rg, 
das_prod_bgproceso.scrap_sr FROM das_prod_bgproceso INNER JOIN mog ON mog.id_mog=
das_prod_bgproceso.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas 
WHERE das.fecha BETWEEN fechaini AND fechafin and das.linea <> 'TB91' and das.linea <> 'TB92' ORDER BY das.linea, das.fecha, das_prod_bgproceso.ini_tp, mog.mog;

set res=1;

ELSE

set res=0;

END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGGeneralFab` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.id_das, das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgproceso.ini_cm,
das_prod_bgproceso.fin_cm, das_prod_bgproceso.ini_tp, das_prod_bgproceso.fin_tp, 
das_prod_bgproceso.pcs_pro_bush,
das_prod_bgproceso.pcs_buen_bush,
das_prod_bgproceso.scrap_bush,
das_prod_bgproceso.pcs_pro_rg,
das_prod_bgproceso.pcs_buen_rg,
das_prod_bgproceso.scrp_rg,
das_prod_bgproceso.pcs_pro_sr,
das_prod_bgproceso.pcs_buen_sr,
das_prod_bgproceso.scrap_sr 
FROM das_prod_bgproceso INNER JOIN mog ON mog.id_mog=
das_prod_bgproceso.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgproceso.das_iddas 
WHERE das.fecha  BETWEEN fechaini AND fechafin ORDER BY das.fecha, das_prod_bgproceso.ini_tp,  das.linea, mog.mog$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGPlatinado` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL BEGIN

SELECT piezas_procesadas.idpiezas_procesadas, das_prod_plat.das_prod_plat_id, piezas_procesadas.cantPzaGood, piezas_procesadas.cantidad_piezas_procesadas, das.linea, das.fecha, das.turno, mog.mog, das_prod_plat.ini_tur, das_prod_plat.fin_tur, das_prod_plat.razon1, das_prod_plat.razon2, das_prod_plat.razon3, das_prod_plat.razon4, das_prod_plat.razon5, das_prod_plat.razon6, das_prod_plat.razon7, das_prod_plat.razon8, das_prod_plat.razon9 FROM das_prod_plat INNER JOIN piezas_procesadas ON piezas_procesadas.dasiddas=das_prod_plat.das_id_das INNER JOIN mog ON mog.id_mog=
das_prod_plat.mog_idmog INNER JOIN das ON das.id_das = das_prod_plat.das_id_das
WHERE das.fecha BETWEEN fechaini AND fechafin GROUP by das_prod_plat.das_prod_plat_id ORDER BY das.fecha, das_prod_plat.ini_tur, mog.mog, das.linea;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGPlatinado2` (IN `idPiezasPro` INT)  NO SQL BEGIN

SELECT piezas_procesadas.cantPzaGood, piezas_procesadas.cantidad_piezas_procesadas, tiempo.hora_inicio, tiempo.hora_fin, tiempo.fecha from piezas_procesadas INNER JOIN tiempo on tiempo.pza_pro_id=piezas_procesadas.idpiezas_procesadas;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGPlatinado3` (IN `fechaini` DATE, IN `fechafin` DATE, OUT `res` INT)  NO SQL BEGIN

if (select das_prod_plat.ini_tur FROM das_prod_plat INNER JOIN das ON das.id_das = das_prod_plat.das_id_das
WHERE das.fecha BETWEEN fechaini AND fechafin GROUP by das_prod_plat.das_prod_plat_id is not null) then

SELECT piezas_procesadas.idpiezas_procesadas, das_prod_plat.das_prod_plat_id, piezas_procesadas.cantPzaGood, piezas_procesadas.cantidad_piezas_procesadas, das.linea, das.fecha, das.turno, mog.mog, das_prod_plat.ini_tur, das_prod_plat.fin_tur, das_prod_plat.razon1, das_prod_plat.razon2, das_prod_plat.razon3, das_prod_plat.razon4, das_prod_plat.razon5, das_prod_plat.razon6, das_prod_plat.razon7, das_prod_plat.razon8, das_prod_plat.razon9 FROM das_prod_plat INNER JOIN piezas_procesadas ON piezas_procesadas.dasiddas=das_prod_plat.das_id_das INNER JOIN mog ON mog.id_mog=
das_prod_plat.mog_idmog INNER JOIN das on das_prod_plat.das_id_das = das.id_das
WHERE das.fecha BETWEEN fechaini AND fechafin GROUP by das_prod_plat.das_prod_plat_id ORDER BY das.fecha, das_prod_plat.ini_tur, mog.mog, das.linea;

set res=1;

ELSE

set res=0;

END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGPrensa` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha, das.turno, das_prod_bgprensa.num_lot_mat as lote_coil, das_prod_bgprensa.metros, mog.mog, mog.modelo, mog.no_parte, das_prod_bgprensa.ini_cm, das_prod_bgprensa.lote,
das_prod_bgprensa.fin_cm, das_prod_bgprensa.ini_tp, das_prod_bgprensa.fin_tp, das_prod_bgprensa.pcs_pro, 
das_prod_bgprensa.pcs_buenas, das_prod_bgprensa.pcs_scrap, das_prod_bgprensa.pcs_bm FROM das_prod_bgprensa INNER JOIN mog ON mog.id_mog=
das_prod_bgprensa.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgprensa.das_id_das WHERE das.fecha BETWEEN fechaini AND fechafin ORDER BY das.linea,  das.fecha, das_prod_bgprensa.ini_tp,   mog.mog$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGPrensaFab` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.id_das, das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_prod_bgprensa.ini_cm,
das_prod_bgprensa.fin_cm, das_prod_bgprensa.ini_tp, das_prod_bgprensa.fin_tp, das_prod_bgprensa.pcs_pro, 
das_prod_bgprensa.pcs_buenas, das_prod_bgprensa.pcs_scrap, das_prod_bgprensa.pcs_bm FROM das_prod_bgprensa INNER JOIN mog ON mog.id_mog=
das_prod_bgprensa.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgprensa.das_id_das 
WHERE das.fecha  BETWEEN fechaini AND fechafin ORDER BY das.fecha, das_prod_bgprensa.ini_tp, mog.mog, das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGPrensaRuth` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das_prod_bgprensa.id_dasprodbgp, das.id_das, das.linea, das.fecha, das.turno, mog.mog, mog.modelo,das_prod_bgprensa.num_lot_mat as lote_coil, das_prod_bgprensa.metros, mog.no_parte, das_prod_bgprensa.ini_cm,
das_prod_bgprensa.fin_cm, das_prod_bgprensa.ini_tp, das_prod_bgprensa.fin_tp, das_prod_bgprensa.pcs_pro, 
das_prod_bgprensa.pcs_buenas, das_prod_bgprensa.pcs_scrap, das_prod_bgprensa.pcs_bm FROM das_prod_bgprensa INNER JOIN mog ON mog.id_mog=
das_prod_bgprensa.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgprensa.das_id_das 
WHERE das.fecha BETWEEN fechaini AND fechafin
GROUP BY das_prod_bgprensa.id_dasprodbgp ORDER BY das.fecha, das.linea, das_prod_bgprensa.ini_tp, mog.mog$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasBGPrensaRuth2` (IN `fechaini` DATE, IN `fechafin` DATE, OUT `res` INT)  NO SQL BEGIN

if (SELECT  das_prod_bgprensa.ini_tp  FROM das_prod_bgprensa INNER JOIN das ON das.id_das = das_prod_bgprensa.das_id_das 
WHERE das.fecha BETWEEN fechaini AND fechafin
GROUP BY das_prod_bgprensa.id_dasprodbgp  is not null) THEN


SELECT das_prod_bgprensa.id_dasprodbgp, das.id_das, das.linea, das.fecha, das.turno, mog.mog, mog.modelo,das_prod_bgprensa.num_lot_mat as lote_coil, das_prod_bgprensa.metros, mog.no_parte, das_prod_bgprensa.ini_cm,
das_prod_bgprensa.fin_cm, das_prod_bgprensa.ini_tp, das_prod_bgprensa.fin_tp, das_prod_bgprensa.pcs_pro, 
das_prod_bgprensa.pcs_buenas, das_prod_bgprensa.pcs_scrap, das_prod_bgprensa.pcs_bm FROM das_prod_bgprensa INNER JOIN mog ON mog.id_mog=
das_prod_bgprensa.mog_idmog INNER JOIN das ON das.id_das = das_prod_bgprensa.das_id_das 
WHERE das.fecha BETWEEN fechaini AND fechafin 
GROUP BY das_prod_bgprensa.id_dasprodbgp
ORDER BY das.fecha, das.linea, das_prod_bgprensa.ini_tp, mog.mog;

set res=1;

ELSE

set res=0;

END IF;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `reporteDasGrading` (IN `fecha1` DATE, IN `fecha2` DATE)   BEGIN 
select mog.mog, mog.STD, mog.modelo, mog.no_parte, das.linea, das.turno, das.fecha, das.empleado_id_empleado, das.id_keeper, das_prod_grading.lote, das_prod_grading.in_tp, 
das_prod_grading.fin_tp, das_prod_grading.in_cm, das_prod_grading.fin_cm, das_prod_grading.sorting, das_prod_grading.maquina, 
piezas_procesadas_fg.total_piezas_aprobadas, piezas_procesadas_fg.total_scrap, piezas_procesadas_fg.totalpiezas_procesadas from das_prod_grading inner join das on 
das.id_das = das_prod_grading.das_idas inner join mog on mog.id_mog = das_prod_grading.mog_idmog inner join registro_rbp on mog.id_mog = registro_rbp.mog_id_mog inner join 
piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp = registro_rbp.id_registro_rbp where das.proceso='GRADING' and das.fecha between fecha1 and fecha2;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteDasMaquinado` (IN `fecha1` DATE, IN `fecha2` DATE, OUT `res` INT)   BEGIN

if (SELECT das_produccion.inicio_tp  FROM das_produccion INNER JOIN das ON das.id_das = das_produccion.das_ida_das
WHERE das.fecha BETWEEN fecha1 AND fecha2
GROUP BY das_produccion.id_dasproduccion is not null) THEN

SELECT das_produccion.id_dasproduccion, das_produccion.piezasProcesadas_id_piezadprocesada, das.id_das, das.linea, das.fecha, das.turno, mog.mog, mog.modelo, mog.no_parte, das_produccion.inicio_cm, das_produccion.fin_cm, das_produccion.inicio_tp, das_produccion.fin_tp, piezas_procesadas.cantPzaGood, piezas_procesadas.cantidad_piezas_procesadas FROM das_produccion INNER JOIN piezas_procesadas ON piezas_procesadas.dasiddas = das_produccion.das_ida_das INNER JOIN mog ON mog.id_mog=
das_produccion.mog_idmog INNER JOIN das ON das.id_das = das_produccion.das_ida_das 
WHERE das.fecha BETWEEN fecha1 AND fecha2 and das_produccion.id_Piezas_Procesadas=piezas_procesadas.idpiezas_procesadas
GROUP BY das_produccion.piezasProcesadas_id_piezadprocesada,das_produccion.id_dasproduccion
ORDER BY das.fecha, das.linea, das_produccion.inicio_tp, mog.mog;

set res=1;

ELSE

set res=0;

END IF;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `reporteDasPlatinado` (IN `fecha1` DATE, IN `fecha2` DATE)   BEGIN
select mog.mog, mog.STD, mog.modelo, mog.no_parte, das.linea, das.turno, das.fecha, das.empleado_id_empleado, das.id_keeper, das_prod_plat.lote, das_prod_plat.ini_tur, das_prod_plat.fin_tur, 
das_prod_plat.razon1, das_prod_plat.razon2, das_prod_plat.razon3, das_prod_plat.razon4, das_prod_plat.razon5, das_prod_plat.razon6, das_prod_plat.razon7, das_prod_plat.razon8, 
das_prod_plat.razon9, piezas_procesadas_fg.total_piezas_aprobadas, piezas_procesadas_fg.total_scrap, piezas_procesadas_fg.totalpiezas_procesadas from das_prod_plat inner join das on 
das.id_das = das_prod_plat.das_id_das inner join mog on mog.id_mog = das_prod_plat.mog_idmog inner join registro_rbp on mog.id_mog = registro_rbp.mog_id_mog inner join piezas_procesadas_fg 
on piezas_procesadas_fg.registro_rbp_id_registro_rbp = registro_rbp.id_registro_rbp where das.proceso='PLATINADO' and das.fecha between fecha1 and fecha2;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `reporteDasPrensa` (IN `fecha1` DATE, IN `fecha2` DATE, OUT `res` INT)   BEGIN

if (SELECT das_prod_pren.inicio_tp  FROM das_prod_pren INNER JOIN das ON das.id_das = das_prod_pren.das_iddas 
WHERE das.fecha BETWEEN fecha1 AND fecha2
GROUP BY das_prod_pren.id_daspropren  is not null) THEN


SELECT das_prod_pren.id_daspropren, das.id_das, das.linea, das.fecha, das.turno, mog.mog, mog.modelo,das_prod_pren.lot_material as lote_coil, das_prod_pren.metros, mog.no_parte, das_prod_pren.inicio_cm,
das_prod_pren.fin_cm, das_prod_pren.inicio_tp, das_prod_pren.fin_tp, das_prod_pren.pzasTotales, 
das_prod_pren.pza_ok, 
das_prod_pren.pcs_bm,
das_prod_pren.pcs_ng,
das_prod_pren.bm_kg,
das_prod_pren.centroEstampado, das_prod_pren.extremo, das_prod_pren.sello,
das_prod_pren.ng_kg FROM das_prod_pren INNER JOIN mog ON mog.id_mog=
das_prod_pren.mog_idmog INNER JOIN das ON das.id_das = das_prod_pren.das_iddas 
WHERE das.fecha BETWEEN fecha1 AND fecha2 
GROUP BY das_prod_pren.id_daspropren
ORDER BY das.fecha, das.linea, das_prod_pren.iniCIO_tp, mog.mog;

set res=1;

ELSE

set res=0;

END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteScrapFechas` (IN `fecha1` VARCHAR(20), IN `fecha2` VARCHAR(20))  NO SQL BEGIN
SELECT razon_rechazo.nombre_rechazo as nombre, SUM(defecto.cantidad_defecto) as cant, registro_rbp.mog, categoria_rechazo.categoria, tiempo.fecha as fecha from registro_rbp INNER JOIN defecto on defecto.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN razon_rechazo on razon_rechazo.id_razon_rechazo=defecto.razon_rechazo_id_razon_rechazo 
INNER JOIN categoria_rechazo ON categoria_rechazo.id_categoria_rechazo=razon_rechazo.categoria_rechazo_id_categoria_rechazo
INNER JOIN tiempo on tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp WHERE tiempo.fecha BETWEEN fecha1 AND fecha2 GROUP BY razon_rechazo.id_razon_rechazo,tiempo.fecha ORDER BY  tiempo.fecha, razon_rechazo.id_razon_rechazo;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reportesDas` (IN `fecha1` VARCHAR(10), IN `fecha2` VARCHAR(10))  NO SQL SELECT das.linea, das.fecha, das.empleado_id_empleado, das.id_keeper, das.id_inspector, das.turno, das_produccion.modelo, 
das_produccion.estandar, das_produccion.lote, das_produccion.inicio_cm, das_produccion.fin_cm, das_produccion.inicio_tp, 
das_produccion.fin_tp, mog.mog, piezas_procesadas_fg.total_piezas_aprobadas, piezas_procesadas_fg.total_scrap, 
piezas_procesadas_fg.totalpiezas_procesadas FROM das INNER JOIN das_produccion on das_produccion.das_ida_das = 
das.id_das INNER JOIN mog on mog.id_mog = das_produccion.mog_idmog INNER JOIN registro_rbp ON registro_rbp.mog_id_mog = 
mog.id_mog INNER JOIN piezas_procesadas_fg ON piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp
where das.proceso='MAQUINADO' AND das.fecha BETWEEN fecha1 AND fecha2$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ReporteSobueGeneral` (IN `fecha1` DATE, IN `fecha2` DATE)  NO SQL SELECT mog.mog, 
registro_rbp.orden_manufactura, 
mog.no_parte, 
piezas_procesadas.linea, 
MAX(tiempo.id_tiempos),
tiempo.fecha,
MIN(piezas_procesadas.idpiezas_procesadas), piezas_procesadas.sobrante_inicial,
piezas_procesadas_fg.sobrante_final,
piezas_procesadas_fg.totalpiezas_procesadas, piezas_procesadas_fg.total_piezas_aprobadas, piezas_procesadas_fg.total_scrap
FROM piezas_procesadas_fg INNER JOIN registro_rbp ON registro_rbp.id_registro_rbp=piezas_procesadas_fg.registro_rbp_id_registro_rbp INNER JOIN mog ON mog.id_mog=registro_rbp.mog_id_mog INNER JOIN tiempo ON tiempo.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN piezas_procesadas on piezas_procesadas.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp 
WHERE tiempo.fecha
BETWEEN fecha1 AND fecha2 and registro_rbp.aduana=0 and (registro_rbp.orden_manufactura  LIKE '%BFO%' or registro_rbp.orden_manufactura  LIKE '%BCO%' or registro_rbp.orden_manufactura  LIKE '%BCH%' or registro_rbp.orden_manufactura  LIKE '%BGR%' or registro_rbp.orden_manufactura  LIKE '%PLT%' or registro_rbp.orden_manufactura  LIKE '%PCK%' or registro_rbp.orden_manufactura  LIKE '%ASL%' or
registro_rbp.orden_manufactura  LIKE '%BHL%') 
GROUP BY orden_manufactura$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteSumarizadoBGPrensa` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha, das.turno, sum(das_prod_bgprensa.pcs_pro), sum(das_prod_bgprensa.pcs_buenas),   sum(das_prod_bgprensa.pcs_scrap),sum(das_prod_bgprensa.pcs_bm) from das INNER JOIN das_prod_bgprensa on das_prod_bgprensa.das_id_das=das.id_das  WHERE das.fecha BETWEEN fechaini and fechafin GROUP by das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `reporteSumarizadoProcesos` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.linea, das.fecha, das.turno, 
sum(das_prod_bgproceso.pcs_pro_bush), sum(das_prod_bgproceso.pcs_buen_bush),   sum(das_prod_bgproceso.scrap_bush), 
sum(das_prod_bgproceso.scrp_rg), 
sum(das_prod_bgproceso.scrap_sr)
from das 
INNER JOIN das_prod_bgproceso on das_prod_bgproceso.das_iddas=das.id_das WHERE das.fecha BETWEEN fechaini and fechafin GROUP BY das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `restaCausasParo` (IN `mo` VARCHAR(30), IN `idd` INT)  NO SQL BEGIN 

DECLARE idmo INT;

set idmo=(SELECT mog.id_mog FROM mog WHERE mog.mog=mo);

SELECT SUM(registrocausasparo.tiempo) as tot FROM registrocausasparo WHERE registrocausasparo.das_id_das=idd AND registrocausasparo.mog_idmog=idmo;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ruth` ()  NO SQL BEGIN

SELECT `piezas_procesadas`.`sobrante_inicial` FROM `piezas_procesadas` WHERE registro_rbp_id_registro_rbp=187 and idpiezas_procesadas=276;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `scrapRg` (IN `orden` VARCHAR(20), OUT `total` INT)  NO SQL BEGIN

DECLARE id int;
DECLARE tot int;

set id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

set tot=(SELECT SUM(defecto.cantidad_defecto) as scrapTotal from defecto WHERE defecto.registro_rbp_id_registro_rbp=id and defecto.razon_rechazo_id_razon_rechazo BETWEEN 111 and 114);

if(tot is null) THEN
set total=0;
ELSE
SET total=tot;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `scrapRg1` (IN `orden` VARCHAR(20), OUT `total` INT)  NO SQL BEGIN

DECLARE id int;
DECLARE tot int;

set id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

set tot=(SELECT SUM(defecto1.cantidad_defecto) as scrapTotal from defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=id and defecto1.razon_rechazo_id_razon_rechazo BETWEEN 168 and 171);

if(tot is null) THEN
set total=0;
ELSE
SET total=tot;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `searchOperator` (IN `cod` VARCHAR(20))  NO SQL BEGIN
SELECT empleado.nombre_empleado, empleado.apellido from empleado WHERE empleado.codigo_alea=cod and (empleado.tipo_usuario='Operador' or empleado.tipo_usuario='Lider' or empleado.tipo_usuario='Keeper');
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `secuenciaPiezasProcesadas` (IN `mogg` VARCHAR(20))  NO SQL SELECT registro_rbp.proceso FROM registro_rbp INNER JOIN piezas_procesadas_fg on piezas_procesadas_fg.registro_rbp_id_registro_rbp =
registro_rbp.id_registro_rbp INNER JOIN mog on mog.id_mog = registro_rbp.mog_id_mog WHERE registro_rbp.estado=0 AND registro_rbp.activo_op=0 AND  registro_rbp.aduana=0 AND mog.mog=mogg$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `secuenciaPorOrden` (IN `orden` VARCHAR(20))  NO SQL SELECT proceso FROM registro_rbp WHERE orden_manufactura = orden$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `setTruncateAll` ()  NO SQL BEGIN
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE defecto;
TRUNCATE TABLE piezas_procesadas;
TRUNCATE TABLE piezas_procesadas_fg;
TRUNCATE TABLE tiempo;
TRUNCATE TABLE empleado_has_registro_rbp;
TRUNCATE TABLE lote_coil;
TRUNCATE table das_produccion;
TRUNCATE TABLE das_registrer;
TRUNCATE TABLE das_slitter;
TRUNCATE TABLE das_reg_prensa;
TRUNCATE TABLE das_prod_pren;
TRUNCATE TABLE das_prod_plat;
TRUNCATE TABLE das_prod_empmaq;
TRUNCATE TABLE das_prod_bgproceso;
TRUNCATE TABLE das_prod_bgprensa;
TRUNCATE TABLE das_produ_empamesas;
TRUNCATE TABLE das_has_registro_rbp;
TRUNCATE TABLE das_coiling_totales;
TRUNCATE TABLE registrocausasparo;
TRUNCATE TABLE piezas_procesadas_grading;
TRUNCATE TABLE das;
TRUNCATE TABLE registro_rbp;
TRUNCATE TABLE mog;
SET FOREIGN_KEY_CHECKS=1;
ALTER TABLE lote_coil AUTO_INCREMENT=1;
ALTER TABLE defecto AUTO_INCREMENT=1;
ALTER TABLE piezas_procesadas AUTO_INCREMENT=1;
ALTER TABLE piezas_procesadas_fg AUTO_INCREMENT=1;
ALTER TABLE piezas_procesadas_grading AUTO_INCREMENT=1;
ALTER TABLE tiempo AUTO_INCREMENT=1;
ALTER TABLE mog AUTO_INCREMENT=1;
ALTER TABLE registro_rbp AUTO_INCREMENT=1;
UPDATE `registro_rbp` SET `estado`=1,`activo_op`=1,`aduana`=1;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sobraEmpaqueBush` (IN `orden_manu` VARCHAR(20))  NO SQL BEGIN

DECLARE idpiepro int;

SET idpiepro=(SELECT MIN(piezas_procesadas.idpiezas_procesadas) AS idpie from piezas_procesadas inner JOIN registro_rbp ON registro_rbp.id_registro_rbp = piezas_procesadas.registro_rbp_id_registro_rbp WHERE registro_rbp.orden_manufactura=orden_manu);

SELECT piezas_procesadas.sobrante_inicial FROM piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpiepro;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sobraInicialBG` (IN `orden` VARCHAR(30), OUT `sobr` INT)  NO SQL BEGIN
DECLARE id int;
SET id = (SELECT registro_rbp.id_registro_rbp from registro_rbp where registro_rbp.orden_manufactura=orden);

if(id is not null) THEN

SET sobr=(SELECT piezas_procesadas.sobrante_inicial from piezas_procesadas where piezas_procesadas.registro_rbp_id_registro_rbp=id limit 1);

ELSE

SET sobr = 0;

END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sobraIniPacking` (IN `orderM` VARCHAR(30), OUT `sobr` INT)   BEGIN
DECLARE cant int;

SET cant=(SELECT sobrante_inicial FROM `piezas_procesadas` INNER JOIN registro_rbp on registro_rbp.id_registro_rbp=piezas_procesadas.registro_rbp_id_registro_rbp WHERE registro_rbp.activo_op=0 AND registro_rbp.aduana=1 AND registro_rbp.estado=0 AND registro_rbp.orden_manufactura=orderM ORDER BY idpiezas_procesadas ASC LIMIT 1);

if(cant is null) then
set sobr=0;
ELSE
set sobr=cant;
end IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sobraIniPackingOperador` (IN `orderM` VARCHAR(30), OUT `sobr` INT)   BEGIN
DECLARE cant int;

SET cant=(SELECT sobrante_inicial FROM `piezas_procesadas` INNER JOIN registro_rbp on registro_rbp.id_registro_rbp=piezas_procesadas.registro_rbp_id_registro_rbp WHERE registro_rbp.orden_manufactura=orderM ORDER BY idpiezas_procesadas ASC LIMIT 1);

if(cant is null) then
set sobr=0;
ELSE
set sobr=cant;
end IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sobraIniPackingRev` (IN `orderM` VARCHAR(30), OUT `sobr` INT)   BEGIN
DECLARE cant int;

SET cant=(SELECT sobrante_inicial FROM `piezas_procesadas` INNER JOIN registro_rbp on registro_rbp.id_registro_rbp=piezas_procesadas.registro_rbp_id_registro_rbp WHERE registro_rbp.orden_manufactura=orderM ORDER BY idpiezas_procesadas ASC LIMIT 1);

SET sobr=cant;



END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sobraIniPackingSuper` (IN `orderM` VARCHAR(30), OUT `sobr` INT)   BEGIN
DECLARE cant int;

SET cant=(SELECT sobrante_inicial FROM `piezas_procesadas` INNER JOIN registro_rbp on registro_rbp.id_registro_rbp=piezas_procesadas.registro_rbp_id_registro_rbp WHERE registro_rbp.activo_op=0 AND registro_rbp.aduana=1 AND registro_rbp.estado=1 AND registro_rbp.orden_manufactura=orderM ORDER BY idpiezas_procesadas ASC LIMIT 1);

if(cant is null) then
set sobr=0;
ELSE
set sobr=cant;
end IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sobranteFinalBGGeneral` (IN `orden_man` VARCHAR(20))  NO SQL BEGIN

declare id int;
DECLARE id_max int;

SET id=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden_man);

SET id_max=(SELECT MAX(piezas_procesadas.idpiezas_procesadas) FROM piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=id);

SELECT piezas_procesadas.sobrante from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=id_max;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `soloRangosCalculados` (IN `orden` VARCHAR(20), IN `tProce` INT, OUT `r1` INT, OUT `r2` INT, IN `s_anterior` INT)  NO SQL BEGIN
DECLARE id int;
DECLARE res int;
DECLARE rango1 int;
DECLARE rango2 int;
DECLARE res2 int;

DECLARE tt int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET res=(SELECT rango_canasta_2 from piezas_procesadas where registro_rbp_id_registro_rbp=id ORDER BY idpiezas_procesadas DESC LIMIT 1);

IF (res is null) then
SET rango1=1;
SET rango2=((tProce-1)+rango1);
ELSE
if(s_anterior=0) then
SET rango1=res+1;
SET rango2=((tProce-1)+rango1);
ELSE
SET rango1=res;
SET rango2=((tProce-1)+rango1);
END IF;
END IF;

SET r1=rango1;
SET r2=rango2;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sumaScrapReportBush` (IN `idRe` INT, OUT `tot` INT)  NO SQL BEGIN

if((SELECT sum(defecto1.cantidad_defecto) from defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=idRe and defecto1.columna_sorting is null) is null) then

set tot=0;
else
set tot=(SELECT sum(defecto1.cantidad_defecto) from defecto1 WHERE defecto1.registro_rbp_id_registro_rbp=idRe and defecto1.columna_sorting is null);

end if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `sumas_std_ok_s` (IN `orden_manu` VARCHAR(30))   BEGIN
DECLARE id_registro int DEFAULT 0;
SET id_registro=(SELECT id_registro_rbp FROM registro_rbp WHERE orden_manufactura=orden_manu);
SELECT SUM(sobrante) AS sobrantetotal,SUM(sobrante_inicial) AS sobranteinicial, SUM(cantPzaGood) AS totalpiezasaprobadas
FROM piezas_procesadas WHERE registro_rbp_id_registro_rbp = id_registro;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `Supervisorname` (IN `codeline` VARCHAR(50), IN `proce` VARCHAR(20), OUT `sis` VARCHAR(30), OUT `proceso_maquina` VARCHAR(25))  NO SQL BEGIN
DECLARE nom varchar(30);

SET nom=(SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) from empleado INNER JOIN 
empleado_supervisor on empleado_supervisor.empleado_id_empleado=empleado.id_empleado INNER join work_center_maquina 
on work_center_maquina.empleado_supervisor_id_empleado_supervisor=empleado_supervisor.id_empleado_supervisor 
INNER JOIN procesos on procesos.id_proceso=empleado.id_proceso
where work_center_maquina.codigo_maquina=codeline and procesos.descripcion=proce);
if(nom is null) then
SET sis=NULL;
ELSE
SET proceso_maquina=(select work_center_maquina.nombre_work_center
                     from work_center_maquina
                     where work_center_maquina.codigo_maquina=codeline);                     
SET sis=nom;
END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `temporalOrdenCerrada` (IN `codigoEm` VARCHAR(20), IN `ordenManu` VARCHAR(30), IN `hora` VARCHAR(20), IN `tipo` VARCHAR(20))  NO SQL BEGIN

DECLARE id_emp int; 
DECLARE id_rbp int; 
DECLARE id_mo int; 
DECLARE exi int; 

SET id_emp=(SELECT empleado.id_empleado FROM empleado WHERE empleado.codigo_alea=codigoEm or empleado.codigo=codigoEm);

SET id_mo=(SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.orden_manufactura=ordenManu);

SET id_rbp=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=ordenManu);

SET exi=(SELECT cerradoordenes.id_cerrado FROM cerradoordenes WHERE cerradoordenes.id_mog=id_mo AND cerradoordenes.id_registro_rbp =id_rbp AND cerradoordenes.tipo_liberacion=tipo);

if(exi is null) THEN

INSERT INTO `cerradoordenes`(`id_empleado`, `hora_liberacion`, `id_registro_rbp`, `id_mog`,`tipo_liberacion`, `fecha`) VALUES (id_emp,hora,id_rbp,id_mo,tipo,CURRENT_DATE);

ELSE

UPDATE `cerradoordenes` SET `hora_liberacion`=hora,`fecha`=CURRENT_DATE WHERE cerradoordenes.id_mog = id_mo AND cerradoordenes.id_registro_rbp = id_rbp AND cerradoordenes.tipo_liberacion = tipo;

END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `temporalOrdenCerradaAduana` (IN `codigoEm` VARCHAR(20), IN `ordenManu` VARCHAR(25), IN `hora` VARCHAR(20), IN `tipo` VARCHAR(20))  NO SQL BEGIN

DECLARE id_emp int;
DECLARE id_rbp int;
DECLARE id_mo int;
DECLARE exi int;

SET id_emp = (SELECT empleado.id_empleado FROM empleado WHERE empleado.id_empleado = codigoEm);

SET id_mo=(SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.orden_manufactura=ordenManu);

SET id_rbp=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=ordenManu);

SET exi=(SELECT cerradoordenes.id_cerrado FROM cerradoordenes WHERE cerradoordenes.id_mog=id_mo AND cerradoordenes.id_registro_rbp =id_rbp AND cerradoordenes.tipo_liberacion=tipo);

if(exi is null) THEN

INSERT INTO `cerradoordenes`(`id_empleado`, `hora_liberacion`, `id_registro_rbp`, `id_mog`,`tipo_liberacion`, `fecha`) VALUES (id_emp,hora,id_rbp,id_mo,tipo,CURRENT_DATE);

ELSE

UPDATE `cerradoordenes` SET `hora_liberacion`=hora,`fecha`=CURRENT_DATE WHERE cerradoordenes.id_mog = id_mo AND cerradoordenes.id_registro_rbp = id_rbp AND cerradoordenes.tipo_liberacion = tipo;

END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `ternerSobrante` (IN `orden` VARCHAR(20), OUT `cant` INT)  NO SQL BEGIN
DECLARE gh int;

set gh=(SELECT MAX(piezas_procesadas.idpiezas_procesadas) FROM piezas_procesadas INNER JOIN registro_rbp on registro_rbp.id_registro_rbp=piezas_procesadas.registro_rbp_id_registro_rbp WHERE registro_rbp.orden_manufactura=orden);

Set cant=(SELECT sobrante_inicial FROM `piezas_procesadas` WHERE piezas_procesadas.idpiezas_procesadas=gh);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `tiempoTotalOrden` (IN `orden` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;



SET id=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SELECT tiempo.horas_trabajadas from tiempo WHERE tiempo.registro_rbp_id_registro_rbp=id;


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerCalidad` (IN `codkep` VARCHAR(20), OUT `si` INT, IN `proce` VARCHAR(20))  NO SQL begin
DECLARE nom varchar(50);
DECLARE id int;
SET id=(SELECT procesos.id_proceso FROM procesos WHERE procesos.descripcion=proce);
SET nom=(SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) from empleado WHERE empleado.codigo_alea=codkep and empleado.id_proceso=id);
if(nom is null) THEN
set si=0;
ELSE
SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nomcompl from empleado WHERE empleado.codigo_alea=codkep;
set si=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerCausasParo` ()  NO SQL SELECT `tiempo`, `detalle`, `hora_inicio`, `fecha`, `linea`, mog.mog FROM `registrocausasparo` 
INNER JOIN mog on mog.id_mog = registrocausasparo.mog_idmog$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerCoils` (IN `orden_man` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden_man);

SELECT lote_coil.lote_coil as coil, lote_coil.metros as metros, lote_coil.f_terminado as finalizado from lote_coil WHERE lote_coil.registro_rbp_id_registro_rbp=id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerCoils1` (IN `orden_man` VARCHAR(20))  NO SQL BEGIN
DECLARE id int;

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden_man);

SELECT lote_coil.lote_coil as coil, SUM(lote_coil.metros) as metros, sum(lote_coil.f_terminado) as finalizado from lote_coil WHERE lote_coil.registro_rbp_id_registro_rbp=id GROUP BY lote_coil.lote_coil;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerCoils2` (IN `orden_man` VARCHAR(20))  NO SQL BEGIN 

DECLARE id int;
DECLARE idMg int;


set idMg=(SELECT mog.id_mog from mog WHERE mog.mog=orden_man);

SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.mog_id_mog=idMg and registro_rbp.orden_manufactura like '%BHL%');

SELECT lote_coil.lote_coil as coil, SUM(lote_coil.metros) as metros, sum(lote_coil.f_terminado) as finalizado from lote_coil WHERE lote_coil.registro_rbp_id_registro_rbp=id GROUP BY lote_coil.lote_coil;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerDatosPLTReporteDas` (IN `idDass` INT)  NO SQL BEGIN

SELECT das_prod_plat.mog_idmog, mog.mog, das.linea, das.fecha, das.turno, das_prod_plat.ini_tur, das_prod_plat.fin_tur, das_prod_plat.razon1 ,das_prod_plat.razon2, das_prod_plat.razon3, das_prod_plat.razon4, das_prod_plat.razon5, das_prod_plat.razon6, das_prod_plat.razon7 ,das_prod_plat.razon8, das_prod_plat.razon9 from das_prod_plat
INNER JOIN das on das.id_das=das_prod_plat.das_id_das
INNER JOIN mog on mog.id_mog=das_prod_plat.mog_idmog
WHERE das_prod_plat.das_id_das=idDass;



END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerEmpleadosTotales` ()  NO SQL SELECT * from empleado WHERE (empleado.id_proceso=1 or empleado.id_proceso=3 or empleado.id_proceso=16)$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traeridAnteriorRevision` (IN `mogg` VARCHAR(20), OUT `val` INT)  NO SQL BEGIN

DECLARE idMgg int;

set idMgg=(SELECT mog.id_mog from mog WHERE mog.mog=mogg);

SELECT registro_rbp.id_registro_rbp, registro_rbp.orden_manufactura from registro_rbp WHERE registro_rbp.mog_id_mog=idMgg  and registro_rbp.proceso!='B/G EMPAQUE' ORDER by registro_rbp.secuencia;

set val=(SELECT count(id_registro_rbp) from registro_rbp WHERE registro_rbp.mog_id_mog=idMgg);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traeridAnteriorRevision2` (IN `mogg` VARCHAR(20), OUT `val` INT)  NO SQL BEGIN

DECLARE idMgg int;

set idMgg=(SELECT mog.id_mog from mog WHERE mog.mog=mogg);

SELECT registro_rbp.id_registro_rbp, registro_rbp.orden_manufactura from registro_rbp WHERE registro_rbp.mog_id_mog=idMgg ORDER by registro_rbp.secuencia;

set val=(SELECT count(id_registro_rbp) from registro_rbp WHERE registro_rbp.mog_id_mog=idMgg);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerIDDasPLT` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.id_das from das WHERE das.linea='TG03' and das.fecha BETWEEN fechaini and fechafin$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerIDDasReportAsakai` (IN `fechaini` DATE, IN `fechafin` DATE)  NO SQL SELECT das.id_das, das.linea, das.turno from das WHERE das.fecha BETWEEN fechaini and fechafin and das.linea <> 'TI30' and das.linea <> 'TI28' and das.linea <> 'TG03' and das.linea <> 'TI29' ORDER BY das.linea$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerIdsOP` (IN `mff` VARCHAR(20))  NO SQL BEGIN

DECLARE idmog int;

set idmog=(SELECT mog.id_mog from mog WHERE mog.mog=CONCAT('MOG0','',mff));

SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.mog_id_mog=idmog and (registro_rbp.proceso='B/G FORMING' or registro_rbp.proceso='B/G COINING' or registro_rbp.proceso='B/G CHAMFER' or registro_rbp.proceso='B/G GRINDING');

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerInspector` (IN `cod_ins` VARCHAR(20), OUT `si` INT, IN `proce` VARCHAR(20), OUT `nombre_ins` VARCHAR(50))  NO SQL begin
DECLARE id int;

SET nombre_ins=(SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) from empleado WHERE empleado.codigo_alea=cod_ins and empleado.tipo_usuario = "Inspector" and empleado.id_proceso = proce);

if(nombre_ins is null) THEN
set si=0;
ELSE
SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nomcompl from empleado WHERE empleado.codigo_alea=cod_ins;
set si=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerKeeper` (IN `codkep` VARCHAR(20), OUT `si` INT, IN `proce` INT, OUT `nom` VARCHAR(40))  NO SQL begin
DECLARE id int;

SET nom=(SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) from empleado WHERE empleado.codigo_alea=codkep and empleado.tipo_usuario = "Keeper" and empleado.id_proceso = proce);

if(nom is null) THEN
set si=0;
ELSE
SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nomcompl from empleado WHERE empleado.codigo_alea=codkep;
set si=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerKeeperPlatinado` (IN `codkep` VARCHAR(20), OUT `si` INT, IN `proce` VARCHAR(20))   begin
DECLARE nom varchar(50);
DECLARE id int;
SET id=(SELECT procesos.id_proceso FROM procesos WHERE procesos.descripcion=proce);
SET nom=(SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) from empleado WHERE empleado.codigo_alea='#3897122426#' and 
((empleado.tipo_usuario='Keeper' and empleado.id_proceso=16 or empleado.id_proceso=3 ) or ( empleado.tipo_usuario='Supervisor' and empleado.id_proceso=16 or empleado.id_proceso=3 ) or (empleado.tipo_usuario='Operador' and empleado.id_proceso=16 or empleado.id_proceso=3)));
if(nom is null) THEN
set si=0;
ELSE
SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nomcompl from empleado WHERE empleado.codigo_alea=codkep;
set si=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerLider` (IN `codKep` VARCHAR(20), OUT `si` INT, IN `proce` VARCHAR(20))  NO SQL begin
DECLARE nom varchar(50);
DECLARE id int;
SET id=(SELECT procesos.id_proceso FROM procesos WHERE procesos.descripcion=proce);
SET nom=(SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) from empleado WHERE (empleado.codigo_alea=codkep and empleado.tipo_usuario='Lider') or (empleado.codigo_alea=codkep and empleado.tipo_usuario='Supervisor') or (empleado.codigo_alea=codkep and empleado.tipo_usuario='Operador') or (empleado.codigo_alea=codkep and empleado.tipo_usuario='Inspector'));
if(nom is null) THEN
set si=0;
ELSE
SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nomcompl from empleado WHERE empleado.codigo_alea=codkep;
set si=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerLotesDas` (IN `idDas` INT, IN `orden` VARCHAR(30), IN `timpIn` VARCHAR(30), IN `timpFi` VARCHAR(30))  NO SQL BEGIN

DECLARE idMog int;

SET idMog=(SELECT mog.id_mog from mog WHERE mog.mog=orden);

if(timpIn BETWEEN '16:20:00' and '23:59:59' && timpFi BETWEEN '00:00:00' and '06:59:59')then

SELECT lote_coil.lote_coil, lote_coil.metros from lote_coil WHERE lote_coil.das_id_das=idDas and lote_coil.mog_id_mog=idMog and (lote_coil.tiempo_insercion BETWEEN timpIn and '23:59:59' or  lote_coil.tiempo_insercion BETWEEN '00:00:00' and timpFi);

ELSE

SELECT lote_coil.lote_coil, lote_coil.metros from lote_coil WHERE lote_coil.das_id_das=idDas and lote_coil.mog_id_mog=idMog and lote_coil.tiempo_insercion BETWEEN timpIn and timpFi;
end if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerMaterialHora` (IN `idpie` INT)  NO SQL BEGIN

DECLARE id int;

SET id = (SELECT piezas_procesadas.registro_rbp_id_registro_rbp FROM piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idpie);

SELECT lote_coil.id_lote_coil, lote_coil.tiempo_insercion, lote_coil.registro_rbp_id_registro_rbp, lote_coil.metros FROM lote_coil WHERE lote_coil.registro_rbp_id_registro_rbp = id;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerMogAsc` (IN `process` VARCHAR(30))  NO SQL BEGIN
SELECT * FROM registro_rbp INNER JOIN mog ON registro_rbp.mog_id_mog = mog.id_mog 
INNER JOIN tiempo ON registro_rbp.id_registro_rbp = tiempo.registro_rbp_id_registro_rbp 
WHERE registro_rbp.estado = 1 and registro_rbp.activo_op = 0 and proceso = process GROUP BY mog.mog ORDER BY tiempo.fecha ASC;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerMogDes` (IN `process` VARCHAR(30))  NO SQL BEGIN
SELECT * FROM registro_rbp INNER JOIN mog ON registro_rbp.mog_id_mog = 	mog.id_mog WHERE registro_rbp.estado=1 and registro_rbp.activo_op = 0 AND proceso = process ORDER BY registro_rbp.id_registro_rbp DESC;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerNoParte_S` ()  NO SQL BEGIN 
SELECT no_parte FROM mog INNER JOIN registro_rbp ON mog.id_mog=registro_rbp.mog_id_mog GROUP BY mog.no_parte;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerNum` (IN `num` VARCHAR(60))  NO SQL SELECT corte FROM bom where numero_parte = num$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerOperador` (IN `num_emp` VARCHAR(20), OUT `si` INT, IN `proceso` INT, OUT `nombre_op` VARCHAR(50))  NO SQL begin
DECLARE id int;

SET nombre_op=(SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) from empleado WHERE empleado.codigo_alea=num_emp and empleado.tipo_usuario = "Operador" and empleado.id_proceso = proceso);

if(nombre_op is null) THEN
set si=0;
ELSE
SELECT CONCAT(empleado.nombre_empleado,' ',empleado.apellido) as nomcompl from empleado WHERE empleado.codigo_alea=num_emp;
set si=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerPiezas` (IN `mogo` VARCHAR(10), OUT `salida` INT)  NO SQL BEGIN
DECLARE id int;
DECLARE rbp int;
SET id=(SELECT MAX(id_piezas_procesadas_fg) AS mayor FROM piezas_procesadas_fg inner join registro_rbp on piezas_procesadas_fg.registro_rbp_id_registro_rbp=registro_rbp.id_registro_rbp INNER JOIN mog ON 
mog.id_mog=registro_rbp.mog_id_mog  WHERE registro_rbp.activo_op=0 AND mog.mog=mogo);
SET salida=0;
if(id IS NOT null)THEN
SELECT piezas_procesadas_fg.total_piezas_aprobadas FROM piezas_procesadas_fg WHERE piezas_procesadas_fg.id_piezas_procesadas_fg=id;
SET salida=1;
ELSE
SELECT mog.cantidad_planeada AS total_piezas_aprobadas FROM mog WHERE mog.mog=mogo;
SET salida=1;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerPiezas2` (IN `mogo` INT, OUT `salida` INT, OUT `total` INT)  NO SQL BEGIN
DECLARE id int;
declare idMgg int;

SET id=(SELECT piezas_procesadas_fg.id_piezas_procesadas_fg FROM piezas_procesadas_fg where piezas_procesadas_fg.registro_rbp_id_registro_rbp =mogo);
SET salida=0;

if(id IS NOT null) THEN

set total=(SELECT piezas_procesadas_fg.total_piezas_aprobadas FROM piezas_procesadas_fg WHERE piezas_procesadas_fg.id_piezas_procesadas_fg=id);

SET salida=1;
ELSE

set idMgg=(SELECT registro_rbp.mog_id_mog from registro_rbp where registro_rbp.id_registro_rbp=mogo);

set total=(SELECT mog.cantidad_planeada AS total_piezas_aprobadas FROM mog WHERE mog.id_mog=idMgg );
SET salida=2;
END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerPiezas3` (IN `mogo` INT, OUT `salida` INT, OUT `total` INT, IN `sino` INT)  NO SQL BEGIN
DECLARE id int;
declare idMgg int;

SET id=(SELECT piezas_procesadas_fg.id_piezas_procesadas_fg FROM piezas_procesadas_fg where piezas_procesadas_fg.registro_rbp_id_registro_rbp =mogo);
SET salida=0;

if(id IS NOT null) THEN

if(sino <> 0) THEN 

set idMgg=(SELECT registro_rbp.mog_id_mog from registro_rbp where registro_rbp.id_registro_rbp=mogo);

set total=(SELECT mog.cantidad_planeada AS total_piezas_aprobadas FROM mog WHERE mog.id_mog=idMgg );
SET salida=2;

ELSE

set total=(SELECT piezas_procesadas_fg.total_piezas_aprobadas FROM piezas_procesadas_fg WHERE piezas_procesadas_fg.id_piezas_procesadas_fg=id);
SET salida=1;

END IF;

END IF;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerPlatProduccion` (IN `iddas` INT)  NO SQL BEGIN
SELECT mog.mog, mog.no_parte, mog.STD ,`lote`, `ini_tur`, `fin_tur`, `razon1`, `razon2`, `razon3`, `razon4`, `razon5`, `razon6`, `razon7`, `razon8`, `razon9` FROM `das_prod_plat` INNER JOIN mog on mog.id_mog = das_prod_plat.mog_idmog WHERE das_prod_plat.das_id_das=iddas;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerProcesos` (IN `mog1` VARCHAR(20))  NO SQL BEGIN

DECLARE partNum varchar(20);

SET partNum=(SELECT mog.no_parte from mog WHERE mog.mog= concat('MOG0','',mog1));

SELECT catalogo_no_parte.no_parte, catalogo_no_parte.forming, catalogo_no_parte.coining, catalogo_no_parte.chamfer, catalogo_no_parte.grinding from catalogo_no_parte WHERE catalogo_no_parte.no_parte=partNum;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `TraerScrapTotalBushReport` (IN `mogg` VARCHAR(20))  NO SQL BEGIN

DECLARE idmg int;

set idmg=(SELECT mog.id_mog from mog WHERE mog.mog=mogg);

SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.mog_id_mog=idmg and (registro_rbp.proceso='B/G FORMING' or registro_rbp.proceso='B/G COINING' or registro_rbp.proceso='B/G CHAMFER' or registro_rbp.proceso='B/G GRINDING');


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerSumaMetrosT` (IN `idP` INT, IN `orden` VARCHAR(30), IN `timpIn` VARCHAR(30), IN `timpFi` VARCHAR(30))   BEGIN

DECLARE idMog int;
DECLARE idDas int;

SET idDas=(SELECT piezas_procesadas.dasiddas from piezas_procesadas WHERE piezas_procesadas.idpiezas_procesadas=idP);
SET idMog=(SELECT mog.id_mog from mog WHERE mog.mog=orden);

if(timpIn BETWEEN '16:20:00' and '23:59:59' && timpFi BETWEEN '00:00:00' and '06:59:59')then

SELECT lote_coil.lote_coil, sum(lote_coil.metros) from lote_coil WHERE lote_coil.das_id_das=idDas and lote_coil.mog_id_mog=idMog and (lote_coil.tiempo_insercion BETWEEN timpIn and '23:59:59' or  lote_coil.tiempo_insercion BETWEEN '00:00:00' and timpFi);

ELSE

SELECT lote_coil.lote_coil,sum(lote_coil.metros) from lote_coil WHERE lote_coil.das_id_das=idDas and lote_coil.mog_id_mog=idMog and lote_coil.tiempo_insercion BETWEEN timpIn and timpFi;
end if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerSumaMetrosT2` (IN `idP` INT, IN `orden` VARCHAR(30), IN `timpIn` VARCHAR(30), IN `timpFi` VARCHAR(30))   BEGIN

DECLARE idMog int;


SET idMog=(SELECT mog.id_mog from mog WHERE mog.mog=orden);

if(timpIn BETWEEN '16:20:00' and '23:59:59' && timpFi BETWEEN '00:00:00' and '06:59:59')then

SELECT lote_coil.lote_coil, sum(lote_coil.metros) from lote_coil WHERE lote_coil.das_id_das=idP and lote_coil.mog_id_mog=idMog and (lote_coil.tiempo_insercion BETWEEN timpIn and '23:59:59' or  lote_coil.tiempo_insercion BETWEEN '00:00:00' and timpFi);

ELSE

SELECT lote_coil.lote_coil,sum(lote_coil.metros) from lote_coil WHERE lote_coil.das_id_das=idP and lote_coil.mog_id_mog=idMog and lote_coil.tiempo_insercion BETWEEN timpIn and timpFi;
end if;
END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerTiempo` (IN `idpie` INT)  NO SQL SELECT tiempo.hora_inicio, tiempo.hora_fin FROM tiempo WHERE tiempo.pza_pro_id=idpie$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerTiempos` (IN `mff` INT)  NO SQL BEGIN

SELECT tiempo.horas_trabajadas as tiempito from tiempo WHERE tiempo.registro_rbp_id_registro_rbp=mff;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traerUltimaFecha` (IN `orden` VARCHAR(20), OUT `fecha1` DATE)  NO SQL BEGIN	

DECLARE id int;
DECLARE idtiempo int;


SET id=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET idtiempo=(SELECT MAX(tiempo.id_tiempos) from tiempo WHERE tiempo.registro_rbp_id_registro_rbp=id);


SET fecha1=(SELECT tiempo.fecha from tiempo WHERE tiempo.id_tiempos=idtiempo);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `traer_piezasPLTDas` (IN `idMOG` INT, IN `idDas` INT)  NO SQL BEGIN

DECLARE idReg int;

set idReg=(SELECT registro_rbp.id_registro_rbp from registro_rbp WHERE registro_rbp.mog_id_mog=idMOG and registro_rbp.orden_manufactura like '%PLT%');

SELECT piezas_procesadas.cantPzaGood, piezas_procesadas.cantidad_piezas_procesadas from piezas_procesadas WHERE piezas_procesadas.registro_rbp_id_registro_rbp=idReg and piezas_procesadas.dasiddas=idDas;


END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `valdiacionBushReport` (IN `lastp` VARCHAR(20), IN `mog1` VARCHAR(20), OUT `val` INT)  NO SQL BEGIN

DECLARE idMg int;

SET idMg=(SELECT mog.id_mog from mog WHERE mog.mog = concat('MOG0','',mog1));

SET val=(SELECT registro_rbp.aduana from registro_rbp WHERE registro_rbp.proceso=concat('B/G',' ',lastp) and registro_rbp.mog_id_mog=idMg);

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `validarCoil` (IN `numPart` VARCHAR(30))  NO SQL SELECT catalogo_no_parte.Bobina from catalogo_no_parte WHERE catalogo_no_parte.no_parte=numPart$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `validarInsercionLiberacion` (IN `orden` VARCHAR(20), IN `tipo` VARCHAR(20), OUT `exis` INT)  NO SQL BEGIN

DECLARE idm int;
DECLARE idrb int;
DECLARE idcerra int;


SET idrb=(SELECT registro_rbp.id_registro_rbp FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET idm=(SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.orden_manufactura=orden);

SET idcerra=(SELECT cerradoordenes.id_cerrado from cerradoordenes WHERE cerradoordenes.id_registro_rbp=idrb AND cerradoordenes.id_mog = idm and cerradoordenes.tipo_liberacion=tipo);

if (idcerra is null) THEN
SET exis=0;
ELSE
SET exis=1;
END IF;

END$$

CREATE DEFINER=`adminpaperless`@`%` PROCEDURE `validar_linea` (IN `no_linea` VARCHAR(20), OUT `area` VARCHAR(20), OUT `codigo_supervisor` VARCHAR(30), OUT `nombre_supervisor` VARCHAR(30))   BEGIN
    -- Asigna el valor de w.area a la variable de salida `area`
    SELECT w.area INTO area
    FROM work_center_maquina AS w
    WHERE w.codigo_maquina = no_linea;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bom`
--

CREATE TABLE `bom` (
  `id_corte` int(11) NOT NULL,
  `numero_parte` varchar(60) NOT NULL,
  `corte` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `bom`
--

INSERT INTO `bom` (`id_corte`, `numero_parte`, `corte`) VALUES
(1, '12665019', 'Y7185-20.5-TC'),
(2, '12665019', 'Y7006-20.5-TC'),
(3, '55487304', 'Y7185-21.0-TC'),
(4, '55487304', 'Y7006-21.0-TC'),
(5, '04893521AB', 'Y7658-19.1-TC'),
(6, '04893521AB', 'Y7610-19.1-TC'),
(7, '04893521AC', 'Y7658-19.1-TC'),
(8, '04893521AC', 'Y7610-19.1-TC'),
(9, '04893522AB', 'Y7658-19.1-TC'),
(10, '04893522AB', 'Y7610-19.1-TC'),
(11, '04893522AC', 'Y7658-19.1-TC'),
(12, '04893522AC', 'Y7610-19.1-TC'),
(13, '04893523AB', 'Y7658-19.1-TC'),
(14, '04893523AB', 'Y7610-19.1-TC'),
(15, '04893523AC', 'Y7658-19.1-TC'),
(16, '04893523AC', 'Y7610-19.1-TC'),
(17, '04893524AB', 'Y7658-19.1-TC'),
(18, '04893524AB', 'Y7610-19.1-TC'),
(19, '04893525AB', 'Y7658-19.1-TC'),
(20, '04893525AB', 'Y7610-19.1-TC'),
(21, '04893526AB', 'Y7658-19.1-TC'),
(22, '04893526AB', 'Y7610-19.1-TC'),
(23, '04893527AB', 'Y7658-19.1-TC'),
(24, '04893527AB', 'Y7610-19.1-TC'),
(25, '04893528AB', 'Y7658-19.1-TC'),
(26, '04893528AB', 'Y7610-19.1-TC'),
(27, '04893529AA', 'Y7658-19.1-TC'),
(28, '04893529AA', 'Y7610-19.1-TC'),
(29, '04893529AB', 'Y7658-19.1-TC'),
(30, '04893529AB', 'Y7610-19.1-TC'),
(31, '04893530AA', 'Y7658-19.1-TC'),
(32, '04893530AA', 'Y7610-19.1-TC'),
(33, '04893530AB', 'Y7658-19.1-TC'),
(34, '04893530AB', 'Y7610-19.1-TC'),
(35, '04893531AA', 'Y7658-19.1-TC'),
(36, '04893531AA', 'Y7610-19.1-TC'),
(37, '04893531AB', 'Y7658-19.1-TC'),
(38, '04893531AB', 'Y7610-19.1-TC'),
(39, '04893532AA', 'Y7658-19.1-TC'),
(40, '04893532AA', 'Y7610-19.1-TC'),
(41, '04893533AA', 'Y7658-19.1-TC'),
(42, '04893533AA', 'Y7610-19.1-TC'),
(43, '04893534AA', 'Y7658-19.1-TC'),
(44, '04893534AA', 'Y7610-19.1-TC'),
(45, '04893535AA', 'Y7658-19.1-TC'),
(46, '04893535AA', 'Y7610-19.1-TC'),
(47, '04893536AA', 'Y7658-19.1-TC'),
(48, '04893536AA', 'Y7610-19.1-TC'),
(49, '11911-F0010-01', 'Y7185-23.0-TC'),
(50, '11911-F0010-01', 'Y7006-23.0-TC'),
(51, '11911-F0010-02', 'Y7185-23.0-TC'),
(52, '11911-F0010-02', 'Y7006-23.0-TC'),
(53, '11911-F0010-03', 'Y7185-23.0-TC'),
(54, '11911-F0010-03', 'Y7006-23.0-TC'),
(55, '121104BC0A', 'Y7007-17.0-TC'),
(56, '121104BC0A', 'Y7010-17.0-TC'),
(57, '121104BC1A', 'Y7007-17.0-TC'),
(58, '121104BC1A', 'Y7010-17.0-TC'),
(59, '121104BC2A', 'Y7007-17.0-TC'),
(60, '121104BC2A', 'Y7010-17.0-TC'),
(61, '121104BC3A', 'Y7007-17.0-TC'),
(62, '121104BC3A', 'Y7010-17.0-TC'),
(63, '121104BC4A', 'Y7007-17.0-TC'),
(64, '121104BC4A', 'Y7010-17.0-TC'),
(65, '12110-5RB1A', 'Y7007-16.0-TC'),
(66, '12110-5RB1A', 'Y7010-16.0-TC'),
(67, '12110-5RB2A', 'Y7007-16.0-TC'),
(68, '12110-5RB2A', 'Y7010-16.0-TC'),
(69, '12110-5RB3A', 'Y7007-16.0-TC'),
(70, '12110-5RB3A', 'Y7010-16.0-TC'),
(71, '12110-5RB4A', 'Y7007-16.0-TC'),
(72, '12110-5RB4A', 'Y7010-16.0-TC'),
(73, '12110-5RB5A', 'Y7007-16.0-TC'),
(74, '12110-5RB5A', 'Y7010-16.0-TC'),
(75, '12111 6KA0A', 'Y7007-18.0-TC'),
(76, '12111 6KA0A', 'Y7010-18.0-TC'),
(77, '12111 6KA1A', 'Y7007-18.0-TC'),
(78, '12111 6KA1A', 'Y7010-18.0-TC'),
(79, '12111 6KA2A', 'Y7007-18.0-TC'),
(80, '12111 6KA2A', 'Y7010-18.0-TC'),
(81, '12111 9FT1A', 'Y7007-19.4-TC'),
(82, '12111 9FT1A', 'Y7010-19.4-TC'),
(83, '12111 9FT2A', 'Y7007-19.4-TC'),
(84, '12111 9FT2A', 'Y7010-19.4-TC'),
(85, '12111 9FT3A', 'Y7007-19.4-TC'),
(86, '12111 9FT3A', 'Y7010-19.4-TC'),
(87, '12111 9FT4A', 'Y7007-19.4-TC'),
(88, '12111 9FT4A', 'Y7010-19.4-TC'),
(89, '121114BC0A', 'Y7007-17.0-TC'),
(90, '121114BC0A', 'Y7010-17.0-TC'),
(91, '121114BC1A', 'Y7007-17.0-TC'),
(92, '121114BC1A', 'Y7010-17.0-TC'),
(93, '121114BC2A', 'Y7007-17.0-TC'),
(94, '121114BC2A', 'Y7010-17.0-TC'),
(95, '121114BC3A', 'Y7007-17.0-TC'),
(96, '121114BC3A', 'Y7010-17.0-TC'),
(97, '121114BC4A', 'Y7007-17.0-TC'),
(98, '121114BC4A', 'Y7010-17.0-TC'),
(99, '121114KH0A', 'Y7007-20.0-TC'),
(100, '121114KH0A', 'Y7010-20.0-TC'),
(101, '121114KH1A', 'Y7007-20.0-TC'),
(102, '121114KH1A', 'Y7010-20.0-TC'),
(103, '121114KH2A', 'Y7007-20.0-TC'),
(104, '121114KH2A', 'Y7010-20.0-TC'),
(105, '121114KH3A', 'Y7007-20.0-TC'),
(106, '121114KH3A', 'Y7010-20.0-TC'),
(107, '121114KH4A', 'Y7007-20.0-TC'),
(108, '121114KH4A', 'Y7010-20.0-TC'),
(109, '12111-5RB2A', 'Y7185-16.0-TC'),
(110, '12111-5RB2A', 'Y7006-16.0-TC'),
(111, '12111-5RB3A', 'Y7185-16.0-TC'),
(112, '12111-5RB3A', 'Y7006-16.0-TC'),
(113, '12111-5RB4A', 'Y7185-16.0-TC'),
(114, '12111-5RB4A', 'Y7006-16.0-TC'),
(115, '12111-5RB5A', 'Y7185-16.0-TC'),
(116, '12111-5RB5A', 'Y7006-16.0-TC'),
(117, '12111-5RB6A', 'Y7185-16.0-TC'),
(118, '12111-5RB6A', 'Y7006-16.0-TC'),
(119, '12215 6KA0A', 'Y7309-21.2-TC'),
(120, '12215 6KA0A', 'Y7009-21.2-TC'),
(121, '12215 6KA1A', 'Y7309-21.2-TC'),
(122, '12215 6KA1A', 'Y7009-21.2-TC'),
(123, '12215 6KA2A', 'Y7309-21.2-TC'),
(124, '12215 6KA2A', 'Y7009-21.2-TC'),
(125, '12215 6KA3A', 'Y7309-21.2-TC'),
(126, '12215 6KA3A', 'Y7009-21.2-TC'),
(127, '12215 6KA4A', 'Y7309-21.2-TC'),
(128, '12215 6KA4A', 'Y7009-21.2-TC'),
(129, '12215 6KA5A', 'Y7309-21.2-TC'),
(130, '12215 6KA5A', 'Y7009-21.2-TC'),
(131, '12215 6KA6A', 'Y7309-21.2-TC'),
(132, '12215 6KA6A', 'Y7009-21.2-TC'),
(133, '12215 6KA7A', 'Y7309-21.2-TC'),
(134, '12215 6KA7A', 'Y7009-21.2-TC'),
(135, '122154BC0A', 'Y7179-18.2-TC'),
(136, '122154BC0A', 'Y7128-18.2-TC'),
(137, '122154BC1A', 'Y7179-18.2-TC'),
(138, '122154BC1A', 'Y7128-18.2-TC'),
(139, '122154BC2A', 'Y7179-18.2-TC'),
(140, '122154BC2A', 'Y7128-18.2-TC'),
(141, '122154BC3A', 'Y7179-18.2-TC'),
(142, '122154BC3A', 'Y7128-18.2-TC'),
(143, '122154BC4A', 'Y7179-18.2-TC'),
(144, '122154BC4A', 'Y7128-18.2-TC'),
(145, '122154BC5A', 'Y7179-18.2-TC'),
(146, '122154BC5A', 'Y7128-18.2-TC'),
(147, '122154BC6A', 'Y7179-18.2-TC'),
(148, '122154BC6A', 'Y7128-18.2-TC'),
(149, '122154BC7A', 'Y7179-18.2-TC'),
(150, '122154BC7A', 'Y7128-18.2-TC'),
(151, '122154KH0A', 'Y7179-21.1-TC'),
(152, '122154KH0A', 'Y7128-21.1-TC'),
(153, '122154KH1A', 'Y7179-21.1-TC'),
(154, '122154KH1A', 'Y7128-21.1-TC'),
(155, '122154KH2A', 'Y7179-21.1-TC'),
(156, '122154KH2A', 'Y7128-21.1-TC'),
(157, '122154KH3A', 'Y7179-21.1-TC'),
(158, '122154KH3A', 'Y7128-21.1-TC'),
(159, '122154KH4A', 'Y7179-21.1-TC'),
(160, '122154KH4A', 'Y7128-21.1-TC'),
(161, '122154KH5A', 'Y7179-21.1-TC'),
(162, '122154KH5A', 'Y7128-21.1-TC'),
(163, '122154KH6A', 'Y7179-21.1-TC'),
(164, '122154KH6A', 'Y7128-21.1-TC'),
(165, '122154KH7A', 'Y7179-21.1-TC'),
(166, '122154KH7A', 'Y7128-21.1-TC'),
(167, '12215-5RB0A', 'Y7179-17.2-TC'),
(168, '12215-5RB0A', 'Y7128-17.2-TC'),
(169, '12215-5RB1A', 'Y7179-17.2-TC'),
(170, '12215-5RB1A', 'Y7128-17.2-TC'),
(171, '12215-5RB2A', 'Y7179-17.2-TC'),
(172, '12215-5RB2A', 'Y7128-17.2-TC'),
(173, '12215-5RB3A', 'Y7179-17.2-TC'),
(174, '12215-5RB3A', 'Y7128-17.2-TC'),
(175, '12215-5RB4A', 'Y7179-17.2-TC'),
(176, '12215-5RB4A', 'Y7128-17.2-TC'),
(177, '12215-5RB5A', 'Y7179-17.2-TC'),
(178, '12215-5RB5A', 'Y7128-17.2-TC'),
(179, '12223 6KA0A', 'Y7309-21.2-TC'),
(180, '12223 6KA0A', 'Y7009-21.2-TC'),
(181, '12223 6KA1A', 'Y7309-21.2-TC'),
(182, '12223 6KA1A', 'Y7009-21.2-TC'),
(183, '12223 6KA2A', 'Y7309-21.2-TC'),
(184, '12223 6KA2A', 'Y7009-21.2-TC'),
(185, '12223 6KA3A', 'Y7309-21.2-TC'),
(186, '12223 6KA3A', 'Y7009-21.2-TC'),
(187, '12223 6KA4A', 'Y7309-21.2-TC'),
(188, '12223 6KA4A', 'Y7009-21.2-TC'),
(189, '12223 6KA5A', 'Y7309-21.2-TC'),
(190, '12223 6KA5A', 'Y7009-21.2-TC'),
(191, '12223 6KA6A', 'Y7309-21.2-TC'),
(192, '12223 6KA6A', 'Y7009-21.2-TC'),
(193, '12223 6KA7A', 'Y7309-21.2-TC'),
(194, '12223 6KA7A', 'Y7009-21.2-TC'),
(195, '122234KH0A', 'Y7179-21.1-TC'),
(196, '122234KH0A', 'Y7128-21.1-TC'),
(197, '122234KH0B', 'Y7179-21.1-TC'),
(198, '122234KH0B', 'Y7128-21.1-TC'),
(199, '122234KH1A', 'Y7179-21.1-TC'),
(200, '122234KH1A', 'Y7128-21.1-TC'),
(201, '122234KH1B', 'Y7179-21.1-TC'),
(202, '122234KH1B', 'Y7128-21.1-TC'),
(203, '122234KH2A', 'Y7179-21.1-TC'),
(204, '122234KH2A', 'Y7128-21.1-TC'),
(205, '122234KH2B', 'Y7179-21.1-TC'),
(206, '122234KH2B', 'Y7128-21.1-TC'),
(207, '122234KH3A', 'Y7179-21.1-TC'),
(208, '122234KH3A', 'Y7128-21.1-TC'),
(209, '122234KH4A', 'Y7179-21.1-TC'),
(210, '122234KH4A', 'Y7128-21.1-TC'),
(211, '122234KH5A', 'Y7179-21.1-TC'),
(212, '122234KH5A', 'Y7128-21.1-TC'),
(213, '122234KH6A', 'Y7179-21.1-TC'),
(214, '122234KH6A', 'Y7128-21.1-TC'),
(215, '122234KH7A', 'Y7179-21.1-TC'),
(216, '122234KH7A', 'Y7128-21.1-TC'),
(217, '12223-5RB0A', 'Y7179-17.2-TC'),
(218, '12223-5RB0A', 'Y7128-17.2-TC'),
(219, '12223-5RB1A', 'Y7179-17.2-TC'),
(220, '12223-5RB1A', 'Y7128-17.2-TC'),
(221, '12223-5RB2A', 'Y7179-17.2-TC'),
(222, '12223-5RB2A', 'Y7128-17.2-TC'),
(223, '12223-5RB3A', 'Y7179-17.2-TC'),
(224, '12223-5RB3A', 'Y7128-17.2-TC'),
(225, '12223-5RB4A', 'Y7179-17.2-TC'),
(226, '12223-5RB4A', 'Y7128-17.2-TC'),
(227, '12223-5RB5A', 'Y7179-17.2-TC'),
(228, '12223-5RB5A', 'Y7128-17.2-TC'),
(229, '122474KH0A', 'Y7179-24.6-TC'),
(230, '122474KH0A', 'Y7128-24.6-TC'),
(231, '122474KH0B', 'Y7179-24.6-TC'),
(232, '122474KH0B', 'Y7128-24.6-TC'),
(233, '122474KH1A', 'Y7179-24.6-TC'),
(234, '122474KH1A', 'Y7128-24.6-TC'),
(235, '122474KH1B', 'Y7179-24.6-TC'),
(236, '122474KH1B', 'Y7128-21.1-TC'),
(237, '122474KH2A', 'Y7179-24.6-TC'),
(238, '122474KH2A', 'Y7128-24.6-TC'),
(239, '122474KH2B', 'Y7179-24.6-TC'),
(240, '122474KH2B', 'Y7128-21.1-TC'),
(241, '122474KH3A', 'Y7179-24.6-TC'),
(242, '122474KH3A', 'Y7128-24.6-TC'),
(243, '122474KH4A', 'Y7179-24.6-TC'),
(244, '122474KH4A', 'Y7128-24.6-TC'),
(245, '122474KH5A', 'Y7179-24.6-TC'),
(246, '122474KH5A', 'Y7128-24.6-TC'),
(247, '122474KH6A', 'Y7179-24.6-TC'),
(248, '122474KH6A', 'Y7128-24.6-TC'),
(249, '13211-59B-0031', 'C9447-14.6'),
(250, '13211-59B-0031', 'C9466-14.6'),
(251, '13211-5A2-A011-M1', 'C9444-16.5'),
(252, '13211-5A2-A011-M1', 'C9468-16.5'),
(253, '13211-64A-A011-M1', 'C9447-14.6'),
(254, '13211-64A-A011-M1', 'C9466-14.6'),
(255, '13211-6S9-A011-M1', 'C9447-15.8'),
(256, '13211-6S9-A011-M1', 'C9466-15.8'),
(257, '13211-R70-D011-M1', 'C9447-15.8'),
(258, '13211-R70-D011-M1', 'C9466-15.8'),
(259, '13211RNAA010', 'Y7185-16.0-TC'),
(260, '13211RNAA010', 'Y7006-16.0-TC'),
(261, '13212-59B-0031', 'C9447-14.6'),
(262, '13212-59B-0031', 'C9466-14.6'),
(263, '13212-5A2-A011-M1', 'C9444-16.5'),
(264, '13212-5A2-A011-M1', 'C9468-16.5'),
(265, '13212-64A-A011-M1', 'C9447-14.6'),
(266, '13212-64A-A011-M1', 'C9466-14.6'),
(267, '13212-6S9-A011-M1', 'C9447-15.8'),
(268, '13212-6S9-A011-M1', 'C9466-15.8'),
(269, '13212-R70-D011-M1', 'C9447-15.8'),
(270, '13212-R70-D011-M1', 'C9466-15.8'),
(271, '13212RNAA010', 'Y7185-16.0-TC'),
(272, '13212RNAA010', 'Y7006-16.0-TC'),
(273, '13213-59B-0031', 'C9447-14.6'),
(274, '13213-59B-0031', 'C9466-14.6'),
(275, '13213-5A2-A011-M1', 'C9444-16.5'),
(276, '13213-5A2-A011-M1', 'C9468-16.5'),
(277, '13213-64A-A011-M1', 'C9447-14.6'),
(278, '13213-64A-A011-M1', 'C9466-14.6'),
(279, '13213-6S9-A011-M1', 'C9447-15.8'),
(280, '13213-6S9-A011-M1', 'C9466-15.8'),
(281, '13213-R70-D011-M1', 'C9447-15.8'),
(282, '13213-R70-D011-M1', 'C9466-15.8'),
(283, '13213RNAA010', 'Y7185-16.0-TC'),
(284, '13213RNAA010', 'Y7006-16.0-TC'),
(285, '13214-59B-0031', 'C9447-14.6'),
(286, '13214-59B-0031', 'C9466-14.6'),
(287, '13214-5A2-A011-M1', 'C9444-16.5'),
(288, '13214-5A2-A011-M1', 'C9468-16.5'),
(289, '13214-64A-A011-M1', 'C9447-14.6'),
(290, '13214-64A-A011-M1', 'C9466-14.6'),
(291, '13214-6S9-A011-M1', 'C9447-15.8'),
(292, '13214-6S9-A011-M1', 'C9466-15.8'),
(293, '13214-R70-D011-M1', 'C9447-15.8'),
(294, '13214-R70-D011-M1', 'C9466-15.8'),
(295, '13214RNAA010', 'Y7185-16.0-TC'),
(296, '13214RNAA010', 'Y7006-16.0-TC'),
(297, '13215-59B-0031', 'C9447-14.6'),
(298, '13215-59B-0031', 'C9466-14.6'),
(299, '13215-5A2-A011-M1', 'C9444-16.5'),
(300, '13215-5A2-A011-M1', 'C9468-16.5'),
(301, '13215-64A-A011-M1', 'C9447-14.6'),
(302, '13215-64A-A011-M1', 'C9466-14.6'),
(303, '13215-6S9-A011-M1', 'C9447-15.8'),
(304, '13215-6S9-A011-M1', 'C9466-15.8'),
(305, '13215-R70-D011-M1', 'C9447-15.8'),
(306, '13215-R70-D011-M1', 'C9466-15.8'),
(307, '13215RNAA010', 'Y7185-16.0-TC'),
(308, '13215RNAA010', 'Y7006-16.0-TC'),
(309, '13216-59B-0031', 'C9447-14.6'),
(310, '13216-59B-0031', 'C9466-14.6'),
(311, '13216-5A2-A011-M1', 'C9444-16.5'),
(312, '13216-5A2-A011-M1', 'C9468-16.5'),
(313, '13216-64A-A011-M1', 'C9447-14.6'),
(314, '13216-64A-A011-M1', 'C9466-14.6'),
(315, '13216RNAA010', 'Y7185-16.0-TC'),
(316, '13216RNAA010', 'Y7006-16.0-TC'),
(317, '13217-59B-0031', 'C9447-14.6'),
(318, '13217-59B-0031', 'C9466-14.6'),
(319, '13217-5A2-A011-M1', 'C9444-16.5'),
(320, '13217-5A2-A011-M1', 'C9468-16.5'),
(321, '13217-64A-A011-M1', 'C9447-14.6'),
(322, '13217-64A-A011-M1', 'C9466-14.6'),
(323, '13218-5A2-A011-M1', 'C9444-16.5'),
(324, '13218-5A2-A011-M1', 'C9468-16.5'),
(325, '132185R00030', 'Y7185-14.7-TC'),
(326, '132185R00030', 'Y7006-14.7-TC'),
(327, '13322-5R0-0031', 'Y7179-18.1-TC'),
(328, '13322-5R0-0031', 'Y7128-18.1-TC'),
(329, '13322-5R0-0032', 'Y7179-18.1-TC'),
(330, '13322-5R0-0032', 'Y7128-18.1-TC'),
(331, '13323-5R0-0031', 'Y7179-18.1-TC'),
(332, '13323-5R0-0031', 'Y7128-18.1-TC'),
(333, '13323-5R0-0032', 'Y7179-18.1-TC'),
(334, '13323-5R0-0032', 'Y7128-18.1-TC'),
(335, '13324-5R0-0031', 'Y7179-18.1-TC'),
(336, '13324-5R0-0031', 'Y7128-18.1-TC'),
(337, '13324-5R0-0032', 'Y7179-18.1-TC'),
(338, '13324-5R0-0032', 'Y7128-18.1-TC'),
(339, '13325-5R0-0031', 'Y7179-18.1-TC'),
(340, '13325-5R0-0031', 'Y7128-18.1-TC'),
(341, '13325-5R0-0032', 'Y7179-18.1-TC'),
(342, '13325-5R0-0032', 'Y7128-18.1-TC'),
(343, '13326-5R0-0031', 'Y7179-18.1-TC'),
(344, '13326-5R0-0031', 'Y7128-18.1-TC'),
(345, '13326-5R0-0032', 'Y7179-18.1-TC'),
(346, '13326-5R0-0032', 'Y7128-18.1-TC'),
(347, '13327-5R0-0031', 'Y7179-18.1-TC'),
(348, '13327-5R0-0031', 'Y7128-18.1-TC'),
(349, '13327-5R0-0032', 'Y7179-18.1-TC'),
(350, '13327-5R0-0032', 'Y7128-18.1-TC'),
(351, '13342-5R0-0031', 'Y7179-18.1-TC'),
(352, '13342-5R0-0031', 'Y7128-18.1-TC'),
(353, '13342-5R0-0032', 'Y7179-18.1-TC'),
(354, '13342-5R0-0032', 'Y7128-18.1-TC'),
(355, '13343-5R0-0031', 'Y7179-18.1-TC'),
(356, '13343-5R0-0031', 'Y7128-18.1-TC'),
(357, '13343-5R0-0032', 'Y7179-18.1-TC'),
(358, '13343-5R0-0032', 'Y7128-18.1-TC'),
(359, '13344-5R0-0031', 'Y7179-18.1-TC'),
(360, '13344-5R0-0031', 'Y7128-18.1-TC'),
(361, '13344-5R0-0032', 'Y7179-18.1-TC'),
(362, '13344-5R0-0032', 'Y7128-18.1-TC'),
(363, '13345-5R0-0031', 'Y7179-18.1-TC'),
(364, '13345-5R0-0031', 'Y7128-18.1-TC'),
(365, '13345-5R0-0032', 'Y7179-18.1-TC'),
(366, '13345-5R0-0032', 'Y7128-18.1-TC'),
(367, '13346-5R0-0031', 'Y7179-18.1-TC'),
(368, '13346-5R0-0031', 'Y7128-18.1-TC'),
(369, '13346-5R0-0032', 'Y7179-18.1-TC'),
(370, '13346-5R0-0032', 'Y7128-18.1-TC'),
(371, '13347-5R0-0031', 'Y7179-18.1-TC'),
(372, '13347-5R0-0031', 'Y7128-18.1-TC'),
(373, '13347-5R0-0032', 'Y7179-18.1-TC'),
(374, '13347-5R0-0032', 'Y7128-18.1-TC'),
(375, '3S7G6211AA', 'Y7185-18.0-TC'),
(376, '3S7G6211AA', 'Y7006-18.0-TC'),
(377, '3S7G6211BA', 'Y7185-18.0-TC'),
(378, '3S7G6211BA', 'Y7006-18.0-TC'),
(379, '3S7G6211CA', 'Y7185-18.0-TC'),
(380, '3S7G6211CA', 'Y7006-18.0-TC'),
(381, '8E5G6211AA', 'Y7185-18.0-TC'),
(382, '8E5G6211AA', 'Y7006-18.0-TC'),
(383, '8E5G6211BA', 'Y7185-18.0-TC'),
(384, '8E5G6211BA', 'Y7006-18.0-TC'),
(385, '8E5G6211CA', 'Y7185-18.0-TC'),
(386, '8E5G6211CA', 'Y7006-18.0-TC'),
(387, '8E5G6K347AA', 'Y7185-56.0'),
(388, '8E5G6K347AA', 'Y7006-56.0'),
(389, '9E5G6333AA', 'Y7658-20.3-TC'),
(390, '9E5G6333AA', 'Y7610-20.3-TC'),
(391, '9E5G6333BA', 'Y7658-20.3-TC'),
(392, '9E5G6333BA', 'Y7610-20.3-TC'),
(393, '9E5G6333CA', 'Y7658-20.3-TC'),
(394, '9E5G6333CA', 'Y7610-20.3-TC'),
(395, '9E5G6333DA', 'Y7658-20.3-TC'),
(396, '9E5G6333DA', 'Y7610-20.3-TC'),
(397, '9E5G6333EA', 'Y7658-20.3-TC'),
(398, '9E5G6333EA', 'Y7610-20.3-TC'),
(399, '9E5G6333FA', 'Y7658-20.3-TC'),
(400, '9E5G6333FA', 'Y7610-20.3-TC'),
(401, '9E5G6333GA', 'Y7658-20.3-TC'),
(402, '9E5G6333GA', 'Y7610-20.3-TC'),
(403, '9E5G6A338AA', 'Y7658-20.3-TC'),
(404, '9E5G6A338AA', 'Y7610-20.3-TC'),
(405, '9E5G6A338BA', 'Y7658-20.3-TC'),
(406, '9E5G6A338BA', 'Y7610-20.3-TC'),
(407, '9E5G6A338CA', 'Y7658-20.3-TC'),
(408, '9E5G6A338CA', 'Y7610-20.3-TC'),
(409, '9E5G6A338DA', 'Y7658-20.3-TC'),
(410, '9E5G6A338DA', 'Y7610-20.3-TC'),
(411, '9E5G6A338EA', 'Y7658-20.3-TC'),
(412, '9E5G6A338EA', 'Y7610-20.3-TC'),
(413, '9E5G6A338FA', 'Y7658-20.3-TC'),
(414, '9E5G6A338FA', 'Y7610-20.3-TC'),
(415, '9E5G6A338GA', 'Y7658-20.3-TC'),
(416, '9E5G6A338GA', 'Y7610-20.3-TC'),
(417, 'CM5G-6211-DAA', 'Y7007-16.5-TC'),
(418, 'CM5G-6211-DAA', 'Y7010-16.5-TC'),
(419, 'CM5G-6211-DBA', 'Y7007-16.5-TC'),
(420, 'CM5G-6211-DBA', 'Y7010-16.5-TC'),
(421, 'CM5G-6211-DCA', 'Y7007-16.5-TC'),
(422, 'CM5G-6211-DCA', 'Y7010-16.5-TC'),
(423, 'CM5G-6333-EAA', 'Y7179-17.0-TC'),
(424, 'CM5G-6333-EAA', 'Y7128-17.0-TC'),
(425, 'CM5G-6333-EBA', 'Y7179-17.0-TC'),
(426, 'CM5G-6333-EBA', 'Y7128-17.0-TC'),
(427, 'CM5G-6333-ECA', 'Y7179-17.0-TC'),
(428, 'CM5G-6333-ECA', 'Y7128-17.0-TC'),
(429, 'CM5G-6A338-DAA', 'Y7179-17.0-TC'),
(430, 'CM5G-6A338-DAA', 'Y7128-17.0-TC'),
(431, 'CM5G-6A338-DBA', 'Y7179-17.0-TC'),
(432, 'CM5G-6A338-DBA', 'Y7128-17.0-TC'),
(433, 'CM5G-6A338-DCA', 'Y7179-17.0-TC'),
(434, 'CM5G-6A338-DCA', 'Y7128-17.0-TC'),
(435, 'CM5G-6K324-EAA', 'Y7179-18.2-TC'),
(436, 'CM5G-6K324-EAA', 'Y7128-18.2-TC'),
(437, 'CM5G-6K324-EBA', 'Y7179-18.2-TC'),
(438, 'CM5G-6K324-EBA', 'Y7128-18.2-TC'),
(439, 'CM5G-6K324-ECA', 'Y7179-18.2-TC'),
(440, 'CM5G-6K324-ECA', 'Y7128-18.2-TC'),
(441, 'CM5G-6K325-EAA', 'Y7179-18.2-TC'),
(442, 'CM5G-6K325-EAA', 'Y7128-18.2-TC'),
(443, 'CM5G-6K325-EBA', 'Y7179-18.2-TC'),
(444, 'CM5G-6K325-EBA', 'Y7128-18.2-TC'),
(445, 'CM5G-6K325-ECA', 'Y7179-18.2-TC'),
(446, 'CM5G-6K325-ECA', 'Y7128-18.2-TC'),
(447, 'EJ7E6211AA', 'Y7330-18.0-TC'),
(448, 'EJ7E6211AA', 'Y7035-18.0-TC'),
(449, 'EJ7E6211BA', 'Y7330-18.0-TC'),
(450, 'EJ7E6211BA', 'Y7035-18.0-TC'),
(451, 'EJ7E6211CA', 'Y7330-18.0-TC'),
(452, 'EJ7E6211CA', 'Y7035-18.0-TC'),
(453, 'EJ7E-6211-GB', 'Y7330-18.0-TC'),
(454, 'EJ7E-6211-GB', 'Y7035-18.0-TC'),
(455, 'EJ7E-6211-HB', 'Y7330-18.0-TC'),
(456, 'EJ7E-6211-HB', 'Y7035-18.0-TC'),
(457, 'EJ7E-6211-JB', 'Y7330-18.0-TC'),
(458, 'EJ7E-6211-JB', 'Y7035-18.0-TC'),
(459, 'JR3E-6211-FA', 'Y7179-22.0-TC'),
(460, 'JR3E-6211-FA', 'Y7128-22.0-TC'),
(461, 'K2GE-6333-AA', 'Y7658-20.3-TC'),
(462, 'K2GE-6333-AA', 'Y7610-20.3-TC'),
(463, 'K2GE-6333-BA', 'Y7658-20.3-TC'),
(464, 'K2GE-6333-BA', 'Y7610-20.3-TC'),
(465, 'K2GE-6333-CA', 'Y7658-20.3-TC'),
(466, 'K2GE-6333-CA', 'Y7610-20.3-TC'),
(467, 'K2GE-6A338-AA', 'Y7658-20.3-TC'),
(468, 'K2GE-6A338-AA', 'Y7610-20.3-TC'),
(469, 'K2GE-6A338-BA', 'Y7658-20.3-TC'),
(470, 'K2GE-6A338-BA', 'Y7610-20.3-TC'),
(471, 'K2GE-6A338-CA', 'Y7658-20.3-TC'),
(472, 'K2GE-6A338-CA', 'Y7610-20.3-TC'),
(473, 'P54G 11 225', 'Y7185-15.1-TC'),
(474, 'P54G 11 225', 'Y7006-15.1-TC'),
(475, 'P54G 11 226', 'Y7185-15.1-TC'),
(476, 'P54G 11 226', 'Y7006-15.1-TC'),
(477, 'P54G 11 227', 'Y7185-15.1-TC'),
(478, 'P54G 11 227', 'Y7006-15.1-TC'),
(479, 'P54G 11 351', 'Y7658-17.7-TC'),
(480, 'P54G 11 351', 'Y7610-17.7-TC'),
(481, 'P54G 11 352', 'Y7658-17.7-TC'),
(482, 'P54G 11 352', 'Y7610-17.7-TC'),
(483, 'P54G 11 353', 'Y7658-17.7-TC'),
(484, 'P54G 11 353', 'Y7610-17.7-TC'),
(485, 'P54G 11 354', 'Y7658-17.7-TC'),
(486, 'P54G 11 354', 'Y7610-17.7-TC'),
(487, 'P54G 11 355', 'Y7658-17.7-TC'),
(488, 'P54G 11 355', 'Y7610-17.7-TC'),
(489, 'P54G 11 356', 'Y7658-17.7-TC'),
(490, 'P54G 11 356', 'Y7610-17.7-TC'),
(491, 'P54G 11 357', 'Y7658-17.7-TC'),
(492, 'P54G 11 357', 'Y7610-17.7-TC'),
(493, 'P54G 11 851', 'Y7658-17.7-TC'),
(494, 'P54G 11 851', 'Y7610-17.7-TC'),
(495, 'P54G 11 852', 'Y7658-17.7-TC'),
(496, 'P54G 11 852', 'Y7610-17.7-TC'),
(497, 'P54G 11 853', 'Y7658-17.7-TC'),
(498, 'P54G 11 853', 'Y7610-17.7-TC'),
(499, 'P54G 11 854', 'Y7658-17.7-TC'),
(500, 'P54G 11 854', 'Y7610-17.7-TC'),
(501, 'P54G 11 855', 'Y7658-17.7-TC'),
(502, 'P54G 11 855', 'Y7610-17.7-TC'),
(503, 'P54G 11 856', 'Y7658-17.7-TC'),
(504, 'P54G 11 856', 'Y7610-17.7-TC'),
(505, 'P54G 11 857', 'Y7658-17.7-TC'),
(506, 'P54G 11 857', 'Y7610-17.7-TC'),
(507, 'PEDD11225', 'Y7185-17.0-TC'),
(508, 'PEDD11225', 'Y7006-17.0-TC'),
(509, 'PEDD11226', 'Y7185-17.0-TC'),
(510, 'PEDD11226', 'Y7006-17.0-TC'),
(511, 'PEDD11227', 'Y7185-17.0-TC'),
(512, 'PEDD11227', 'Y7006-17.0-TC'),
(513, 'PEDD11351', 'Y7658-19.4-TC'),
(514, 'PEDD11351', 'Y7610-19.4-TC'),
(515, 'PEDD11352', 'Y7658-19.4-TC'),
(516, 'PEDD11352', 'Y7610-19.4-TC'),
(517, 'PEDD11353', 'Y7658-19.4-TC'),
(518, 'PEDD11353', 'Y7610-19.4-TC'),
(519, 'PEDD11354', 'Y7658-19.4-TC'),
(520, 'PEDD11354', 'Y7610-19.4-TC'),
(521, 'PEDD11355', 'Y7658-19.4-TC'),
(522, 'PEDD11355', 'Y7610-19.4-TC'),
(523, 'PEDD11356', 'Y7658-19.4-TC'),
(524, 'PEDD11356', 'Y7610-19.4-TC'),
(525, 'PEDD11357', 'Y7658-19.4-TC'),
(526, 'PEDD11357', 'Y7610-19.4-TC'),
(527, 'PEDD11851', 'Y7658-19.4-TC'),
(528, 'PEDD11851', 'Y7610-19.4-TC'),
(529, 'PEDD11852', 'Y7658-19.4-TC'),
(530, 'PEDD11852', 'Y7610-19.4-TC'),
(531, 'PEDD11853', 'Y7658-19.4-TC'),
(532, 'PEDD11853', 'Y7610-19.4-TC'),
(533, 'PEDD11854', 'Y7658-19.4-TC'),
(534, 'PEDD11854', 'Y7610-19.4-TC'),
(535, 'PEDD11855', 'Y7658-19.4-TC'),
(536, 'PEDD11855', 'Y7610-19.4-TC'),
(537, 'PEDD11856', 'Y7658-19.4-TC'),
(538, 'PEDD11856', 'Y7610-19.4-TC'),
(539, 'PEDD11857', 'Y7658-19.4-TC'),
(540, 'PEDD11857', 'Y7610-19.4-TC'),
(541, 'PX13 11 225', 'Y7185-18.0-TC'),
(542, 'PX13 11 225', 'Y7006-18.0-TC'),
(543, 'PX13 11 226', 'Y7185-18.0-TC'),
(544, 'PX13 11 226', 'Y7006-18.0-TC'),
(545, 'PX13 11 227', 'Y7185-18.0-TC'),
(546, 'PX13 11 227', 'Y7006-18.0-TC'),
(547, 'PX13 11 351', 'Y7658-20.1-TC'),
(548, 'PX13 11 351', 'Y7610-20.1-TC'),
(549, 'PX13 11 352', 'Y7658-20.1-TC'),
(550, 'PX13 11 352', 'Y7610-20.1-TC'),
(551, 'PX13 11 353', 'Y7658-20.1-TC'),
(552, 'PX13 11 353', 'Y7610-20.1-TC'),
(553, 'PX13 11 354', 'Y7658-20.1-TC'),
(554, 'PX13 11 354', 'Y7610-20.1-TC'),
(555, 'PX13 11 355', 'Y7658-20.1-TC'),
(556, 'PX13 11 355', 'Y7610-20.1-TC'),
(557, 'PX13 11 356', 'Y7658-20.1-TC'),
(558, 'PX13 11 356', 'Y7610-20.1-TC'),
(559, 'PX13 11 357', 'Y7658-20.1-TC'),
(560, 'PX13 11 357', 'Y7610-20.1-TC'),
(561, 'PX13 11 851', 'Y7658-20.1-TC'),
(562, 'PX13 11 851', 'Y7610-20.1-TC'),
(563, 'PX13 11 852', 'Y7658-20.1-TC'),
(564, 'PX13 11 852', 'Y7610-20.1-TC'),
(565, 'PX13 11 853', 'Y7658-20.1-TC'),
(566, 'PX13 11 853', 'Y7610-20.1-TC'),
(567, 'PX13 11 854', 'Y7658-20.1-TC'),
(568, 'PX13 11 854', 'Y7610-20.1-TC'),
(569, 'PX13 11 855', 'Y7658-20.1-TC'),
(570, 'PX13 11 855', 'Y7610-20.1-TC'),
(571, 'PX13 11 856', 'Y7658-20.1-TC'),
(572, 'PX13 11 856', 'Y7610-20.1-TC'),
(573, 'PX13 11 857', 'Y7658-20.1-TC'),
(574, 'PX13 11 857', 'Y7610-20.1-TC');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_no_parte`
--

CREATE TABLE `catalogo_no_parte` (
  `id_no_parte` int(11) NOT NULL,
  `no_parte` varchar(30) NOT NULL,
  `cliente` varchar(60) NOT NULL,
  `blanking` int(11) NOT NULL,
  `forming` int(11) NOT NULL,
  `coining` int(11) NOT NULL,
  `chamfer` int(11) NOT NULL,
  `grinding` int(11) NOT NULL,
  `plating` int(11) NOT NULL,
  `assy` int(11) NOT NULL,
  `Bobina` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `catalogo_no_parte`
--

INSERT INTO `catalogo_no_parte` (`id_no_parte`, `no_parte`, `cliente`, `blanking`, `forming`, `coining`, `chamfer`, `grinding`, `plating`, `assy`, `Bobina`) VALUES
(1, '31405X420A', 'JATCO', 1, 1, 1, 1, 0, 0, 1, 0),
(2, '31405 28X0A', 'KITAGAWA', 1, 1, 1, 1, 0, 0, 1, 0),
(3, '334946-12050', 'AISHIN', 1, 1, 1, 1, 0, 0, 1, 0),
(4, '6A12561-A', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(5, '6A12561-1-PK2A', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(6, '6A12561-1-PK2B', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(7, '6A12561-1-PK2C', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(8, '7A16954-1-PK2A', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(9, '7A16954-1-PK2B', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(10, '7A16954-1-PK2C', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(11, '4A15900-2-PK2A', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(12, '5A14109-1-K2A', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(13, '5A14766-1-K2A', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(14, '5A14765-1-PK2A', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(15, '10101-90916-5A', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(16, '10241-90920-5B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(17, '10941-90911-3Z', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(18, 'A0101-90922-0B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(19, 'A0941-90907-0A', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(20, 'A0941-90907-0B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(21, 'A0941-90907-0C', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(22, 'JM5P-7K340-BA', 'LINAMAR', 1, 1, 1, 0, 0, 0, 1, 1),
(23, '62059-275-00-PF', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(24, '62059-275-21-SC', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(25, '6A159-275-1A-E', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(26, '6A259-275-7A-E', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(27, '6A259-275-71', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(28, '6A259-275-70', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(29, '62059-275-01-SC', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(30, '6A159-275-11', 'SHOWA', 1, 0, 0, 0, 0, 0, 1, 1),
(31, '31405-1XF0C', 'SUMITOMO DENKO', 1, 1, 1, 1, 1, 0, 1, 0),
(32, '95028-124', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 1),
(33, '95028-181', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 1),
(34, '95028-190', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 0),
(35, '95028-191', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 0),
(36, '95028-198', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 0),
(37, '95028-209', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 1),
(38, '95028-223', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 1),
(39, 'C49C5420021', 'HITACHI', 1, 1, 0, 1, 1, 0, 1, 0),
(40, '211870587-0000', 'LUK', 1, 1, 1, 0, 1, 0, 1, 1),
(41, 'L-08170-1018-02 000', 'LUK', 1, 1, 0, 1, 1, 0, 1, 0),
(42, 'JZF07-000310-A', 'TSUDA USA', 1, 0, 0, 0, 0, 0, 1, 1),
(43, '10101-90916-6A', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(44, 'HZ230685-0010', 'HAMADEN', 1, 0, 0, 0, 0, 0, 1, 1),
(45, 'L-08154-1376-00', 'TEKFOR MEXICO', 1, 0, 0, 0, 0, 0, 1, 1),
(46, 'L-08170-1048-00', 'LUK', 1, 1, 0, 1, 1, 0, 1, 1),
(47, '95028-237', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 1),
(48, '31405 28X0B', 'ESC', 1, 1, 1, 1, 1, 0, 1, 0),
(49, '334945-12020', 'AISIN AI', 1, 0, 0, 0, 0, 1, 1, 1),
(50, '334945-12010', 'AISIN AI', 1, 0, 0, 0, 0, 1, 1, 1),
(51, '334946-12030', 'AISIN AI', 1, 0, 0, 0, 0, 1, 1, 1),
(52, '334946-12020', 'AISIN AI', 1, 0, 0, 0, 0, 1, 1, 1),
(53, '307006', 'GABRIEL DE MEXICO', 1, 0, 0, 0, 0, 0, 1, 1),
(54, '327003', 'GABRIEL DE MEXICO', 1, 0, 0, 0, 0, 0, 1, 1),
(55, '6171-16K21-A-2', 'AISAN INDUSTRY', 0, 0, 0, 0, 0, 1, 1, 1),
(56, '10941-90911-4Z', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(57, '10241-90920-6B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(58, '311941', 'GABRIEL DE MEXICO', 1, 0, 0, 0, 0, 0, 1, 1),
(59, '375126', 'GABRIEL DE MEXICO', 1, 0, 0, 0, 0, 0, 1, 1),
(60, '078094305-0000', 'LUK', 1, 1, 0, 1, 1, 0, 1, 0),
(61, '5-195924-199', 'DENSO MEXICO SA de CV', 0, 0, 0, 0, 0, 1, 1, 1),
(62, 'JZF07-000310-B', 'NAGAKURA MEXICO', 1, 0, 0, 0, 0, 0, 1, 1),
(63, 'A1321-90912-00', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(64, 'A0101-90909-3B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(65, '211870595-0000', 'LUK', 1, 1, 1, 0, 1, 0, 1, 1),
(66, 'S0721', 'SOGEFI USA', 1, 0, 0, 0, 0, 1, 1, 1),
(67, '8A17911-1-PK2Z', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(68, '8A17911-1-PK2A', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(69, '8A17911-1-PK2B', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(70, '8A17911-1-PK2C', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(71, '8A17911-1-PK2D', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(72, '6A12561-1-PK2Z.', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(73, '6A12561-1-PK2A.', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(74, '6A12561-1-PK2B.', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(75, '6A12561-1-PK2C.', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(76, '6A12561-1-PK2D.', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(77, '2X6171-08250', 'AISHIN', 1, 0, 0, 0, 0, 1, 1, 1),
(78, '3130320009', 'BOSCH', 1, 0, 0, 0, 0, 0, 1, 1),
(79, '95028-181SN', 'TRICO', 1, 0, 0, 0, 0, 1, 1, 1),
(80, '5A14765-1-PK2ASG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(81, '5A14765-1-PK2BSG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(82, '5A14765-1-PK2CSG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(83, '5A14765-1-PK2DSG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(84, '5A14765-1-PK2ESG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(85, '6A12561-1-PK2ASG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(86, '6A12561-1-PK2BSG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(87, '6A12561-1-PK2CSG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(88, '6A12561-1-PK2DSG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(89, '6A12561-1-PK2ESG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(90, '3140550X5B-D1', 'BEYONZ MEXICANA SA de CV', 1, 1, 1, 1, 0, 0, 1, 0),
(91, '2X6176-08320', 'AISAN MEXICO', 1, 0, 0, 0, 0, 1, 1, 1),
(92, 'MX-195924-0730', 'DENSO MEXICO SA de CV', 1, 0, 0, 0, 0, 1, 1, 1),
(93, '6171-08250', 'AISAN MEXICO', 1, 0, 0, 0, 0, 1, 1, 1),
(94, '5A14765-1-PK2ZSG', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(95, '6176-08320', 'AISAN MEXICO', 1, 0, 0, 0, 0, 1, 1, 1),
(96, '10101-90916-6B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(97, '10101-90916-6C', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(98, '10101-90916-6D', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(99, '10101-90916-6Z', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(100, '6A12561-1-PK2D', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(101, '6A12561-1-PK2Z', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(102, '5A14765-1-PK2B', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(103, '5A14765-1-PK2C', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(104, '5A14765-1-PK2D', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(105, '5A14765-1-PK2Z', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(106, '7A16954-1-PK2D', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(107, '7A16954-1-PK2Z', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(108, '4A15900-2-PK2B', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(109, '4A15900-2-PK2C', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(110, '4A15900-2-PK2D', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(111, '4A15900-2-PK2Z', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(112, '10241-90920-6A', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(113, '10241-90920-6C', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(114, '10241-90920-6D', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(115, '10241-90920-6Z', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(116, 'A0101-90931-0Z', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(117, 'A0101-90931-0A', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(118, 'A0101-90931-0B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(119, 'A0101-90931-0C', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(120, 'A0101-90931-0D', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(121, '3203001', 'KOHLER CO', 1, 1, 1, 1, 0, 1, 1, 1),
(122, '22329304', 'BWI', 1, 0, 0, 0, 0, 0, 1, 1),
(123, 'C1013712', 'VALEO KAPEC SLP', 1, 1, 1, 1, 0, 0, 1, 0),
(124, '5A14765-1-PK2ASG.', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(125, '5A14765-1-PK2BSG.', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(126, '5A14765-1-PK2CSG.', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(127, '5A14765-1-PK2DSG.', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(128, '5A14765-1-PK2ZSG.', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(129, 'A0941-90910-01', 'PMG PENNSYLVANIA LCC', 1, 0, 0, 0, 0, 0, 1, 1),
(130, '20211-50402-04', 'KYB', 1, 0, 0, 0, 0, 1, 1, 1),
(131, '5A147651PK2AS1', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(132, '5A147651PK2ZS1', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(133, '5A147651PK2BS1', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(134, '5A147651PK2CS1', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(135, '5A147651PK2DS1', 'Hitachi', 1, 0, 0, 0, 0, 0, 1, 1),
(136, '8A14308-A-PK2', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(137, 'A0941-90913-0D', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(138, 'A0941-90913-0C', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(139, 'A0941-90913-0B', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(140, 'A0941-90913-0A', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(141, 'A0941-90913-0Z', 'KYB', 1, 0, 0, 0, 0, 0, 1, 1),
(142, '8A14307-A-PK2', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(143, '22328295', 'BWI', 1, 0, 0, 0, 0, 0, 1, 1),
(144, '8A17132-A-PK2', 'HITACHI', 0, 0, 0, 0, 0, 0, 1, 1),
(145, '8A16990-A-PK2', 'HITACHI', 0, 0, 0, 0, 0, 0, 1, 1),
(146, '8A16891-A-PK2', 'HITACHI', 0, 0, 0, 0, 0, 0, 1, 1),
(147, '8A17012-A-PK2', 'HITACHI', 0, 0, 0, 0, 0, 0, 1, 1),
(148, '8A16533-A-PK2', 'HITACHI', 0, 0, 0, 0, 0, 0, 1, 1),
(149, '8A14970-A-PK2', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(150, '8A17910-A-PK2', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(151, '8A17847-A-PK2', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(152, '8A17847-A-PK2.', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(153, '7A14698-A-PK2', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(154, '5A14765-1-PK2A-J7604', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(155, '5A14765-1-PK2B-J7604', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(156, '5A14765-1-PK2C-J7604', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(157, '5A14765-1-PK2D-J7604', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(158, '5A14765-1-PK2E-J7604', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1),
(159, '8A17069-A-PK2', 'HITACHI', 1, 0, 0, 0, 0, 0, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_rechazo`
--

CREATE TABLE `categoria_rechazo` (
  `id_categoria_rechazo` int(10) UNSIGNED NOT NULL,
  `categoria` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `categoria_rechazo`
--

INSERT INTO `categoria_rechazo` (`id_categoria_rechazo`, `categoria`) VALUES
(1, 'Bimetal'),
(2, 'Prensa'),
(3, 'Maquinado'),
(4, 'Platinado'),
(5, 'Empaque'),
(6, 'Rod Guide');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `causas_paro`
--

CREATE TABLE `causas_paro` (
  `idcausas_paro` int(10) UNSIGNED NOT NULL,
  `numero_causas_paro` int(11) NOT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  `categoria` varchar(30) DEFAULT NULL,
  `procesos_idproceso` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `causas_paro`
--

INSERT INTO `causas_paro` (`idcausas_paro`, `numero_causas_paro`, `descripcion`, `categoria`, `procesos_idproceso`) VALUES
(1, 1, 'Cambio de Modelo', 'Dandori', 1),
(2, 2, 'Recolector de Rebaba', 'Problema de Mantenimiento', 1),
(3, 3, 'Transportación(Holders)', 'Problema de Mantenimiento', 1),
(4, 4, 'Cambio de tamaño', 'Ajustes', 1),
(5, 7, 'Limpieza', 'Paro Planeado', 1),
(6, 8, 'Control de Calidad', 'Paro Planeado', 1),
(7, 9, 'Otros(especifique)', 'Dandori', 1),
(8, 14, 'Ajuste Inicial', 'Ajustes', 1),
(9, 15, 'Acabado de ancho', 'Problema de Mantenimiento', 1),
(10, 16, 'Perforación', 'Problema de Mantenimiento', 1),
(11, 17, 'Alivio de uña', 'Problema de Mantenimiento', 1),
(12, 18, 'Expulsión de uña', 'Problema de Mantenimiento', 1),
(13, 19, 'Canal de aceite', 'Problema de Mantenimiento', 1),
(14, 20, 'Chamfer de Orificio', 'Problema de Mantenimiento', 1),
(15, 21, 'Broach', 'Problema de Mantenimiento', 1),
(16, 22, 'Corte de Altura', 'Problema de Mantenimiento', 1),
(17, 23, 'Verificador de altura', 'Problema de Mantenimiento', 1),
(18, 25, 'M. Transferencia', 'Problema de Mantenimiento', 1),
(19, 26, 'Pulido de Orificio', 'Problema de Mantenimiento', 1),
(20, 28, 'Chamfer de Esquinas', 'Problema de Mantenimiento', 1),
(21, 29, 'Cambio de Navajas', 'Ajustes', 1),
(22, 30, 'Cambio de Banda', 'Problema de Mantenimiento', 1),
(23, 31, 'Espera de Navajas', 'Paro Planeado', 1),
(24, 32, 'Espera de Material', 'Paro Planeado', 1),
(25, 33, 'Ajuste de Navajas', 'Ajustes', 1),
(34, 34, 'Ajuste de Flujo', 'Ajustes', 1),
(35, 35, 'Inspección', 'Paro Planeado', 1),
(36, 36, 'Saludo Matutino', 'Paro Planeado', 1),
(37, 37, 'Espera de Canasta', 'Paro Planeado', 1),
(38, 38, 'Paros Planificados', 'Paro Planeado', 1),
(39, 39, 'Pulido de Espada', 'Problema de Mantenimiento', 1),
(40, 40, 'Ajuste de Espesor', 'Ajustes', 1),
(42, 42, 'Cambio de U/L', 'Ajustes', 1),
(43, 43, 'Boring', 'Problema de Mantenimiento', 1),
(46, 46, 'Autocheck', 'Problema de Mantenimiento', 1),
(48, 48, 'Contar NG', 'Paro Planeado', 1),
(49, 1, 'Ajuste Inicial', 'Libre', 6),
(50, 2, 'Prototipos', 'Libre', 6),
(51, 3, 'Revisión de Cortadores', 'Libre', 6),
(52, 4, 'Engrasado y Limpieza', 'Libre', 6),
(53, 5, 'Paro Planificado', 'Libre', 6),
(54, 6, 'Doble proceso', 'Libre', 6),
(55, 7, 'Mantenimiento', 'Libre', 6),
(56, 8, 'Espera de Espacio en Stock', 'Libre', 6),
(57, 9, 'Espera de Cortadores', 'Libre', 6),
(58, 10, 'Falla de Flujo', 'Libre', 6),
(59, 11, 'Descarga de Bimetal', 'Libre', 6),
(60, 12, 'Otros (Especifique)', 'Libre', 6),
(61, 1, 'Ajuste Inicial', 'Libre', 7),
(62, 2, 'Ajuste de Dimensiones', 'Libre', 7),
(63, 3, 'Cambio de Navajas', 'Libre', 7),
(64, 4, 'Engrasado y Limpieza', 'Libre', 7),
(65, 5, 'Paro Planificado', 'Libre', 7),
(66, 6, 'Espera de Material', 'Libre', 7),
(67, 7, 'Cambio de Material', 'Libre', 7),
(68, 8, 'Prototipos', 'Libre', 7),
(69, 9, 'Verificación de Inspección', 'Libre', 7),
(70, 10, 'Mantenimiento', 'Libre', 7),
(71, 11, 'Espera de Bobina', 'Libre', 7),
(72, 12, 'Otros (Especifique)', 'Libre', 7),
(73, 1, 'Ajuste Inicial', 'Libre', 2),
(89, 2, 'Ajuste de Apariencia', 'Libre', 2),
(90, 3, 'Ajuste de Sensor de Bad Mark', 'Libre', 2),
(91, 4, 'Ajuste de Flujo', 'Libre', 2),
(92, 5, 'Cambio de Sello', 'Libre', 2),
(93, 6, 'Paro Planificado', 'Libre', 2),
(94, 7, 'Espera de Material', 'Libre', 2),
(95, 8, 'Mantenimiento', 'Libre', 2),
(96, 9, 'Cambio de Material', 'Libre', 2),
(97, 10, 'Cambio de Herramental', 'Libre', 2),
(98, 11, 'Verificación de Inspección', 'Libre', 2),
(99, 12, 'Ajuste de Área de Contacto', 'Libre', 2),
(100, 13, 'Cambio de Navajas', 'Libre', 2),
(101, 14, 'Engrasado y Limpieza', 'Libre', 2),
(102, 15, 'Espera de Bandejas', 'Libre', 2),
(103, 16, 'Otros (Especifique)', 'Libre', 2),
(104, 1, 'Ajuste de Navajas', 'Libre', 5),
(105, 2, 'Ajuste de Sello', 'Libre', 5),
(106, 3, 'Ajuste en Coining', 'Libre', 5),
(107, 4, 'Dandori (Cambio modelo)', 'Libre', 5),
(117, 5, 'Ajuste reafilado de dado en corte', 'Libre', 5),
(118, 6, 'Ajustes por espesor', 'Libre', 5),
(119, 7, 'Apoyo a otra máquina', 'Libre', 5),
(120, 8, 'Cambio de Navaja', 'Libre', 5),
(121, 9, 'Defecto por prensa', 'Libre', 5),
(122, 10, 'Espera de piezas', 'Libre', 5),
(123, 11, 'Falla de desembobinador', 'Libre', 5),
(124, 12, 'Falla en niveladora', 'Libre', 5),
(125, 13, 'Falla en máquina (Especificar falla)', 'Libre', 5),
(126, 14, 'Falta de material', 'Libre', 5),
(127, 15, 'Hoja Azul ', 'Libre', 5),
(128, 16, 'Horario de comida', 'Libre', 5),
(129, 17, 'Horario de descanso', 'Libre', 5),
(130, 18, 'Material NG Chaflanes', 'Libre', 5),
(131, 19, 'Taiso (Inicio de Turno)', 'Libre', 5),
(132, 20, 'Nuevo Modelo', 'Libre', 5),
(133, 21, 'Paro por inspección', 'Libre', 5),
(134, 22, 'Piezas Sospechosas', 'Libre', 5),
(135, 23, 'Problemas de calidad', 'Libre', 5),
(136, 24, 'Pruebas de materiales', 'Libre', 5),
(137, 1, 'Cambio de Modelo', 'Libre', 4),
(138, 2, 'Control de Calidad', 'Libre', 4),
(139, 3, 'Limpieza', 'Libre', 4),
(140, 4, 'Ajuste Inicial', 'Libre', 4),
(141, 5, 'Espera de Material', 'Libre', 4),
(142, 6, 'Ajuste de Flujo', 'Libre', 4),
(143, 7, 'Inspección', 'Libre', 4),
(144, 8, 'Material Defectuoso', 'Libre', 4),
(145, 9, 'Junta y Taiso', 'Libre', 4),
(146, 10, 'Calibrar Extensión', 'Libre', 4),
(147, 11, 'Mantenimiento', 'Libre', 4),
(148, 12, 'Otros (Especifique)', 'Libre', 4),
(149, 1, 'Cambio de Modelo', 'Libre', 9),
(150, 2, 'Control de Calidad', 'Libre', 9),
(151, 3, 'Limpieza', 'Libre', 9),
(152, 4, 'Espera de Material', 'Libre', 9),
(153, 5, 'Taiso y Junta', 'Libre', 9),
(154, 6, 'Otros (Especifique)', 'Libre', 9),
(155, 1, 'Cambio de Modelo', 'Libre', 8),
(156, 2, 'Control de Calidad', 'Libre', 8),
(157, 3, 'Mantenimiento', 'Libre', 8),
(158, 4, 'Ajuste Inicial', 'Libre', 8),
(159, 5, 'Espera de Material', 'Libre', 8),
(160, 6, 'Ajuste de Flujo', 'Libre', 8),
(161, 7, 'Inspección', 'Libre', 8),
(162, 8, 'Ajuste de Espesor', 'Libre', 8),
(163, 9, 'Ajuste TESA', 'Libre', 8),
(164, 10, 'Calibrar Extensión', 'Libre', 8),
(165, 11, 'Limpieza', 'Libre', 8),
(166, 12, 'Taiso y Junta', 'Libre', 8),
(167, 13, 'Descanso', 'Libre', 8),
(168, 14, 'Otros (Especifique)', 'Libre', 8),
(169, 1, 'Preparación de Línea', 'Libre', 3),
(170, 2, 'Control de Calidad', 'Libre', 3),
(171, 3, 'Limpieza', 'Libre', 3),
(172, 4, 'Cambio de pretratamiento', 'Libre', 3),
(173, 5, 'Espera de Material', 'Libre', 3),
(174, 6, 'Cambio de Modelo', 'Libre', 3),
(175, 7, 'Espera por Temperatura', 'Libre', 3),
(176, 8, 'Ajuste de Espesor', 'Libre', 3),
(177, 9, 'Mantenimiento', 'Libre', 3),
(178, 10, 'Otros (Especifique)', 'Libre', 3),
(179, 1, 'Ajuste de Navajas', 'Libre', 10),
(180, 2, 'Ajuste de Sello', 'Libre', 10),
(181, 3, 'Ajuste en Coining', 'Libre', 10),
(182, 4, 'Dandori (Cambio modelo)', 'Libre', 10),
(183, 5, 'Ajuste reafilado de dado en corte', 'Libre', 10),
(184, 6, 'Ajustes por espesor', 'Libre', 10),
(185, 7, 'Apoyo a otra máquina', 'Libre', 10),
(186, 8, 'Cambio de Navaja', 'Libre', 10),
(187, 9, 'Defecto por prensa', 'Libre', 10),
(188, 10, 'Espera de piezas', 'Libre', 10),
(189, 11, 'Falla de desembobinador', 'Libre', 10),
(190, 12, 'Falla en niveladora', 'Libre', 10),
(191, 13, 'Falla en máquina (Especificar falla)', 'Libre', 10),
(192, 14, 'Falta de material', 'Libre', 10),
(193, 15, 'Hoja Azul ', 'Libre', 10),
(194, 16, 'Horario de comida', 'Libre', 10),
(195, 17, 'Horario de descanso', 'Libre', 10),
(196, 18, 'Material NG Chaflanes', 'Libre', 10),
(197, 19, 'Taiso (Inicio de Turno)', 'Libre', 10),
(198, 20, 'Nuevo Modelo', 'Libre', 10),
(199, 21, 'Paro por inspección', 'Libre', 10),
(200, 22, 'Piezas Sospechosas', 'Libre', 10),
(201, 23, 'Problemas de calidad', 'Libre', 10),
(202, 24, 'Pruebas de materiales', 'Libre', 10),
(203, 1, 'Ajuste de Navajas', 'Libre', 12),
(204, 2, 'Ajuste de Sello', 'Libre', 12),
(205, 3, 'Ajuste en Coining', 'Libre', 12),
(206, 4, 'Dandori (Cambio modelo)', 'Libre', 12),
(207, 5, 'Ajuste reafilado de dado en corte', 'Libre', 12),
(208, 6, 'Ajustes por espesor', 'Libre', 12),
(209, 7, 'Apoyo a otra máquina', 'Libre', 12),
(210, 8, 'Cambio de Navaja', 'Libre', 12),
(211, 9, 'Defecto por prensa', 'Libre', 12),
(212, 10, 'Espera de piezas', 'Libre', 12),
(213, 11, 'Falla de desembobinador', 'Libre', 12),
(214, 12, 'Falla en niveladora', 'Libre', 12),
(215, 13, 'Falla en máquina (Especificar falla)', 'Libre', 12),
(216, 14, 'Falta de material', 'Libre', 12),
(217, 15, 'Hoja Azul ', 'Libre', 12),
(218, 16, 'Horario de comida', 'Libre', 12),
(219, 17, 'Horario de descanso', 'Libre', 12),
(220, 18, 'Material NG Chaflanes', 'Libre', 12),
(221, 19, 'Taiso (Inicio de Turno)', 'Libre', 12),
(222, 20, 'Nuevo Modelo', 'Libre', 12),
(223, 21, 'Paro por inspección', 'Libre', 12),
(224, 22, 'Piezas Sospechosas', 'Libre', 12),
(225, 23, 'Problemas de calidad', 'Libre', 12),
(226, 24, 'Pruebas de materiales', 'Libre', 12),
(227, 1, 'Ajuste de Navajas', 'Libre', 13),
(228, 2, 'Ajuste de Sello', 'Libre', 13),
(229, 3, 'Ajuste en Coining', 'Libre', 13),
(230, 4, 'Dandori (Cambio modelo)', 'Libre', 13),
(231, 5, 'Ajuste reafilado de dado en corte', 'Libre', 13),
(232, 6, 'Ajustes por espesor', 'Libre', 13),
(233, 7, 'Apoyo a otra máquina', 'Libre', 13),
(234, 8, 'Cambio de Navaja', 'Libre', 13),
(235, 9, 'Defecto por prensa', 'Libre', 13),
(236, 10, 'Espera de piezas', 'Libre', 13),
(237, 11, 'Falla de desembobinador', 'Libre', 13),
(238, 12, 'Falla en niveladora', 'Libre', 13),
(239, 13, 'Falla en máquina (Especificar falla)', 'Libre', 13),
(240, 14, 'Falta de material', 'Libre', 13),
(241, 15, 'Hoja Azul ', 'Libre', 13),
(242, 16, 'Horario de comida', 'Libre', 13),
(243, 17, 'Horario de descanso', 'Libre', 13),
(244, 18, 'Material NG Chaflanes', 'Libre', 13),
(245, 19, 'Taiso (Inicio de Turno)', 'Libre', 13),
(246, 20, 'Nuevo Modelo', 'Libre', 13),
(247, 21, 'Paro por inspección', 'Libre', 13),
(248, 22, 'Piezas Sospechosas', 'Libre', 13),
(249, 23, 'Problemas de calidad', 'Libre', 13),
(250, 24, 'Pruebas de materiales', 'Libre', 13),
(251, 1, 'Ajuste de Navajas', 'Libre', 14),
(252, 2, 'Ajuste de Sello', 'Libre', 14),
(253, 3, 'Ajuste en Coining', 'Libre', 14),
(254, 4, 'Dandori (Cambio modelo)', 'Libre', 14),
(255, 5, 'Ajuste reafilado de dado en corte', 'Libre', 14),
(256, 6, 'Ajustes por espesor', 'Libre', 14),
(257, 7, 'Apoyo a otra máquina', 'Libre', 14),
(258, 8, 'Cambio de Navaja', 'Libre', 14),
(259, 9, 'Defecto por prensa', 'Libre', 14),
(260, 10, 'Espera de piezas', 'Libre', 14),
(261, 11, 'Falla de desembobinador', 'Libre', 14),
(262, 12, 'Falla en niveladora', 'Libre', 14),
(263, 13, 'Falla en máquina (Especificar falla)', 'Libre', 14),
(264, 14, 'Falta de material', 'Libre', 14),
(265, 15, 'Hoja Azul ', 'Libre', 14),
(266, 16, 'Horario de comida', 'Libre', 14),
(267, 17, 'Horario de descanso', 'Libre', 14),
(268, 18, 'Material NG Chaflanes', 'Libre', 14),
(269, 19, 'Taiso (Inicio de Turno)', 'Libre', 14),
(270, 20, 'Nuevo Modelo', 'Libre', 14),
(271, 21, 'Paro por inspección', 'Libre', 14),
(272, 22, 'Piezas Sospechosas', 'Libre', 14),
(273, 23, 'Problemas de calidad', 'Libre', 14),
(274, 24, 'Pruebas de materiales', 'Libre', 14),
(275, 1, 'Ajuste de Navajas', 'Libre', 15),
(276, 2, 'Ajuste de Sello', 'Libre', 15),
(277, 3, 'Ajuste en Coining', 'Libre', 15),
(278, 4, 'Dandori (Cambio modelo)', 'Libre', 15),
(279, 5, 'Ajuste reafilado de dado en corte', 'Libre', 15),
(280, 6, 'Ajustes por espesor', 'Libre', 15),
(281, 7, 'Apoyo a otra máquina', 'Libre', 15),
(282, 8, 'Cambio de Navaja', 'Libre', 15),
(283, 9, 'Defecto por prensa', 'Libre', 15),
(284, 10, 'Espera de piezas', 'Libre', 15),
(285, 11, 'Falla de desembobinador', 'Libre', 15),
(286, 12, 'Falla en niveladora', 'Libre', 15),
(287, 13, 'Falla en máquina (Especificar falla)', 'Libre', 15),
(288, 14, 'Falta de material', 'Libre', 15),
(289, 15, 'Hoja Azul ', 'Libre', 15),
(290, 16, 'Horario de comida', 'Libre', 15),
(291, 17, 'Horario de descanso', 'Libre', 15),
(292, 18, 'Material NG Chaflanes', 'Libre', 15),
(293, 19, 'Taiso (Inicio de Turno)', 'Libre', 15),
(294, 20, 'Nuevo Modelo', 'Libre', 15),
(295, 21, 'Paro por inspección', 'Libre', 15),
(296, 22, 'Piezas Sospechosas', 'Libre', 15),
(297, 23, 'Problemas de calidad', 'Libre', 15),
(298, 24, 'Pruebas de materiales', 'Libre', 15),
(299, 25, 'Otros', 'Libre', 10),
(300, 25, 'Otros', 'Libre', 12),
(301, 25, 'Otros', 'Libre', 13),
(302, 25, 'Otros', 'Libre', 14),
(303, 25, 'Otros', 'Libre', 15),
(304, 1, 'Preparación de Línea', 'Libre', 16),
(305, 2, 'Control de Calidad', 'Libre', 16),
(306, 3, 'Limpieza', 'Libre', 16),
(307, 4, 'Cambio de pretratamiento', 'Libre', 16),
(308, 5, 'Espera de Material', 'Libre', 16),
(309, 6, 'Cambio de Modelo', 'Libre', 16),
(310, 7, 'Espera por Temperatura', 'Libre', 16),
(311, 8, 'Ajuste de Espesor', 'Libre', 16),
(312, 9, 'Mantenimiento', 'Libre', 16),
(313, 10, 'Otros (Especifique)', 'Libre', 16),
(314, 1, 'Ajuste de Navajas', 'Libre', 11),
(315, 2, 'Ajuste de Sello', 'Libre', 11),
(316, 3, 'Ajuste en Coining', 'Libre', 11),
(317, 4, 'Dandori (Cambio modelo)', 'Libre', 11),
(318, 5, 'Ajuste reafilado de dado en corte', 'Libre', 11),
(319, 6, 'Ajustes por espesor', 'Libre', 11),
(320, 7, 'Apoyo a otra máquina', 'Libre', 11),
(321, 8, 'Cambio de Navaja', 'Libre', 11),
(322, 9, 'Defecto por prensa', 'Libre', 11),
(323, 10, 'Espera de piezas', 'Libre', 11),
(324, 11, 'Falla de desembobinador', 'Libre', 11),
(325, 12, 'Falla en niveladora', 'Libre', 11),
(326, 13, 'Falla en máquina (Especificar falla)', 'Libre', 11),
(327, 14, 'Falta de material', 'Libre', 11),
(328, 15, 'Hoja Azul ', 'Libre', 11),
(329, 16, 'Horario de comida', 'Libre', 11),
(330, 17, 'Horario de descanso', 'Libre', 11),
(331, 18, 'Material NG Chaflanes', 'Libre', 11),
(332, 19, 'Taiso (Inicio de Turno)', 'Libre', 11),
(333, 20, 'Nuevo Modelo', 'Libre', 11),
(334, 21, 'Paro por inspección', 'Libre', 11),
(335, 22, 'Piezas Sospechosas', 'Libre', 11),
(336, 23, 'Problemas de calidad', 'Libre', 11),
(337, 24, 'Pruebas de materiales', 'Libre', 11),
(338, 25, 'Otros', 'Libre', 11),
(339, 1, 'Ajuste de Navajas', 'Libre', 17),
(340, 2, 'Ajuste de Sello', 'Libre', 17),
(341, 3, 'Ajuste en Coining', 'Libre', 17),
(342, 4, 'Dandori (Cambio modelo)', 'Libre', 17),
(343, 5, 'Ajuste reafilado de dado en corte', 'Libre', 17),
(344, 6, 'Ajustes por espesor', 'Libre', 17),
(345, 7, 'Apoyo a otra máquina', 'Libre', 17),
(346, 8, 'Cambio de Navaja', 'Libre', 17),
(347, 9, 'Defecto por prensa', 'Libre', 17),
(348, 10, 'Espera de piezas', 'Libre', 17),
(349, 11, 'Falla de desembobinador', 'Libre', 17),
(350, 12, 'Falla en niveladora', 'Libre', 17),
(351, 13, 'Falla en máquina (Especificar falla)', 'Libre', 17),
(352, 14, 'Falta de material', 'Libre', 17),
(353, 15, 'Hoja Azul ', 'Libre', 17),
(354, 16, 'Horario de comida', 'Libre', 17),
(355, 17, 'Horario de descanso', 'Libre', 17),
(356, 18, 'Material NG Chaflanes', 'Libre', 17),
(357, 19, 'Taiso (Inicio de Turno)', 'Libre', 17),
(358, 20, 'Nuevo Modelo', 'Libre', 17),
(359, 21, 'Paro por inspección', 'Libre', 17),
(360, 22, 'Piezas Sospechosas', 'Libre', 17),
(361, 23, 'Problemas de calidad', 'Libre', 17),
(362, 24, 'Pruebas de materiales', 'Libre', 17),
(363, 25, 'Otros', 'Libre', 17);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cerradoordenes`
--

CREATE TABLE `cerradoordenes` (
  `id_cerrado` int(11) NOT NULL,
  `id_empleado` int(10) UNSIGNED NOT NULL,
  `hora_liberacion` varchar(20) NOT NULL,
  `id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `id_mog` int(11) NOT NULL,
  `tipo_liberacion` varchar(20) NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cerradoordenes`
--

INSERT INTO `cerradoordenes` (`id_cerrado`, `id_empleado`, `hora_liberacion`, `id_registro_rbp`, `id_mog`, `tipo_liberacion`, `fecha`) VALUES
(1, 755, '16:09:10', 31, 31, 'Operador', '2020-10-30'),
(2, 600, '16:09:10', 31, 31, 'Supervisor', '2020-10-30'),
(3, 758, '16:25:12', 32, 32, 'Operador', '2020-10-30'),
(4, 600, '16:25:13', 32, 32, 'Supervisor', '2020-10-30'),
(5, 758, '12:06:05', 34, 34, 'Operador', '2020-11-02'),
(6, 600, '12:06:05', 34, 34, 'Supervisor', '2020-11-02'),
(7, 745, '12:14:36', 33, 33, 'Operador', '2020-11-02'),
(8, 600, '12:14:36', 33, 33, 'Supervisor', '2020-11-02'),
(9, 756, '13:58:17', 37, 37, 'Operador', '2020-11-03'),
(10, 600, '13:58:17', 37, 37, 'Supervisor', '2020-11-03'),
(11, 745, '14:17:16', 40, 40, 'Operador', '2020-11-04'),
(12, 600, '14:17:17', 40, 40, 'Supervisor', '2020-11-04'),
(13, 145, '14:25:43', 41, 41, 'Operador', '2020-11-04'),
(14, 600, '14:25:43', 41, 41, 'Supervisor', '2020-11-04'),
(15, 149, '11:58:58', 43, 43, 'Operador', '2020-11-05'),
(16, 600, '11:58:58', 43, 43, 'Supervisor', '2020-11-05'),
(17, 746, '12:01:53', 44, 44, 'Operador', '2020-11-05'),
(18, 600, '12:01:53', 44, 44, 'Supervisor', '2020-11-05'),
(19, 745, '13:57:14', 46, 46, 'Operador', '2020-11-06'),
(20, 600, '13:57:14', 46, 46, 'Supervisor', '2020-11-06'),
(21, 745, '15:22:55', 45, 45, 'Operador', '2020-11-06'),
(22, 600, '15:22:55', 45, 45, 'Supervisor', '2020-11-06'),
(23, 739, '12:07:20', 47, 47, 'Operador', '2020-11-10'),
(24, 600, '12:07:20', 47, 47, 'Supervisor', '2020-11-10'),
(25, 739, '12:13:38', 48, 48, 'Operador', '2020-11-10'),
(26, 600, '12:13:38', 48, 48, 'Supervisor', '2020-11-10'),
(27, 739, '16:04:18', 51, 51, 'Operador', '2020-11-12'),
(28, 600, '16:04:18', 51, 51, 'Supervisor', '2020-11-12'),
(29, 739, '16:20:48', 52, 52, 'Operador', '2020-11-12'),
(30, 600, '16:20:48', 52, 52, 'Supervisor', '2020-11-12'),
(31, 740, '15:29:59', 53, 53, 'Operador', '2020-11-13'),
(32, 600, '15:30:00', 53, 53, 'Supervisor', '2020-11-13'),
(33, 741, '15:53:17', 54, 54, 'Operador', '2020-11-13'),
(34, 600, '15:53:17', 54, 54, 'Supervisor', '2020-11-13'),
(35, 752, '15:52:40', 56, 56, 'Operador', '2020-11-17'),
(36, 600, '15:52:40', 56, 56, 'Supervisor', '2020-11-17'),
(37, 751, '15:58:23', 57, 57, 'Operador', '2020-11-17'),
(38, 600, '15:58:23', 57, 57, 'Supervisor', '2020-11-17'),
(39, 145, '15:37:10', 59, 59, 'Operador', '2020-11-19'),
(40, 600, '15:37:10', 59, 59, 'Supervisor', '2020-11-19'),
(41, 145, '14:05:39', 60, 60, 'Operador', '2020-11-21'),
(42, 600, '14:05:39', 60, 60, 'Supervisor', '2020-11-21'),
(43, 148, '14:56:22', 61, 61, 'Operador', '2020-11-21'),
(44, 600, '14:56:22', 61, 61, 'Supervisor', '2020-11-21'),
(45, 739, '15:23:59', 62, 62, 'Operador', '2020-11-23'),
(46, 600, '15:23:59', 62, 62, 'Supervisor', '2020-11-23'),
(47, 149, '15:35:54', 63, 63, 'Operador', '2020-11-23'),
(48, 600, '15:35:54', 63, 63, 'Supervisor', '2020-11-23'),
(49, 753, '16:12:58', 64, 64, 'Operador', '2020-11-24'),
(50, 600, '16:12:58', 64, 64, 'Supervisor', '2020-11-24'),
(51, 756, '16:23:55', 65, 65, 'Operador', '2020-11-24'),
(52, 600, '16:23:55', 65, 65, 'Supervisor', '2020-11-24'),
(53, 169, '15:58:19', 66, 66, 'Operador', '2020-11-25'),
(54, 600, '15:58:19', 66, 66, 'Supervisor', '2020-11-25'),
(55, 170, '10:25:47', 69, 69, 'Operador', '2020-11-30'),
(56, 600, '10:25:47', 69, 69, 'Supervisor', '2020-11-30'),
(57, 148, '11:12:38', 68, 68, 'Operador', '2020-11-30'),
(58, 600, '11:12:38', 68, 68, 'Supervisor', '2020-11-30'),
(59, 755, '15:43:36', 70, 70, 'Operador', '2020-12-02'),
(60, 600, '15:43:36', 70, 70, 'Supervisor', '2020-12-02'),
(61, 755, '15:47:05', 71, 71, 'Operador', '2020-12-02'),
(62, 600, '15:47:05', 71, 71, 'Supervisor', '2020-12-02'),
(65, 716, '16:05', 31, 31, 'Aduana', '2020-12-02'),
(66, 727, '08:05', 32, 32, 'Aduana', '2020-12-07'),
(67, 727, '08:08', 33, 33, 'Aduana', '2020-12-07'),
(68, 727, '08:13', 34, 34, 'Aduana', '2020-12-07'),
(69, 727, '08:14', 40, 40, 'Aduana', '2020-12-07'),
(70, 745, '14:44:31', 72, 72, 'Operador', '2020-12-07'),
(71, 600, '14:44:31', 72, 72, 'Supervisor', '2020-12-07'),
(72, 745, '14:51:57', 73, 73, 'Operador', '2020-12-07'),
(73, 600, '14:51:57', 73, 73, 'Supervisor', '2020-12-07'),
(74, 148, '11:38:59', 75, 75, 'Operador', '2020-12-08'),
(75, 600, '11:38:59', 75, 75, 'Supervisor', '2020-12-08'),
(76, 743, '11:44:56', 76, 76, 'Operador', '2020-12-08'),
(77, 600, '11:44:56', 76, 76, 'Supervisor', '2020-12-08'),
(78, 743, '14:25:46', 77, 77, 'Operador', '2020-12-08'),
(79, 600, '14:25:46', 77, 77, 'Supervisor', '2020-12-08'),
(80, 691, '08:52', 77, 77, 'Aduana', '2020-12-09'),
(81, 691, '08:53', 75, 75, 'Aduana', '2020-12-09'),
(82, 691, '08:53', 76, 76, 'Aduana', '2020-12-09'),
(83, 691, '08:53', 72, 72, 'Aduana', '2020-12-09'),
(84, 691, '08:53', 57, 57, 'Aduana', '2020-12-09'),
(85, 691, '08:53', 63, 63, 'Aduana', '2020-12-09'),
(86, 691, '08:54', 61, 61, 'Aduana', '2020-12-09'),
(87, 691, '08:54', 71, 71, 'Aduana', '2020-12-09'),
(88, 691, '08:54', 70, 70, 'Aduana', '2020-12-09'),
(89, 691, '08:54', 64, 64, 'Aduana', '2020-12-09'),
(90, 691, '08:55', 60, 60, 'Aduana', '2020-12-09'),
(91, 691, '08:55', 62, 62, 'Aduana', '2020-12-09'),
(92, 691, '08:55', 59, 59, 'Aduana', '2020-12-09'),
(93, 691, '08:56', 52, 52, 'Aduana', '2020-12-09'),
(94, 691, '08:56', 73, 73, 'Aduana', '2020-12-09'),
(95, 691, '08:56', 44, 44, 'Aduana', '2020-12-09'),
(96, 691, '10:22', 53, 53, 'Aduana', '2020-12-09'),
(97, 691, '10:22', 43, 43, 'Aduana', '2020-12-09'),
(98, 691, '10:23', 37, 37, 'Aduana', '2020-12-09'),
(99, 691, '10:23', 41, 41, 'Aduana', '2020-12-09'),
(100, 691, '10:23', 45, 45, 'Aduana', '2020-12-09'),
(101, 691, '10:24', 46, 46, 'Aduana', '2020-12-09'),
(102, 691, '10:24', 51, 51, 'Aduana', '2020-12-09'),
(103, 691, '10:24', 48, 48, 'Aduana', '2020-12-09'),
(104, 691, '10:24', 47, 47, 'Aduana', '2020-12-09'),
(105, 691, '10:24', 56, 56, 'Aduana', '2020-12-09'),
(106, 747, '11:47:05', 78, 78, 'Operador', '2020-12-09'),
(107, 600, '11:47:05', 78, 78, 'Supervisor', '2020-12-09'),
(108, 755, '12:40:52', 79, 79, 'Operador', '2020-12-09'),
(109, 600, '12:40:52', 79, 79, 'Supervisor', '2020-12-09'),
(110, 691, '07:11', 78, 78, 'Aduana', '2020-12-10'),
(111, 691, '07:11', 79, 79, 'Aduana', '2020-12-10'),
(112, 691, '07:25', 54, 54, 'Aduana', '2020-12-10'),
(113, 739, '12:08:48', 81, 81, 'Operador', '2020-12-10'),
(114, 600, '12:08:48', 81, 81, 'Supervisor', '2020-12-10'),
(115, 691, '12:10', 81, 81, 'Aduana', '2020-12-10'),
(116, 739, '12:14:27', 80, 80, 'Operador', '2020-12-10'),
(117, 600, '12:14:28', 80, 80, 'Supervisor', '2020-12-10'),
(118, 727, '12:14', 80, 80, 'Aduana', '2020-12-10'),
(119, 740, '15:48:16', 82, 82, 'Operador', '2020-12-11'),
(120, 600, '15:48:16', 82, 82, 'Supervisor', '2020-12-11'),
(121, 744, '15:54:46', 83, 83, 'Operador', '2020-12-11'),
(122, 600, '15:54:46', 83, 83, 'Supervisor', '2020-12-11'),
(123, 664, '12:21', 83, 83, 'Aduana', '2020-12-16'),
(124, 664, '12:21', 82, 82, 'Aduana', '2020-12-16'),
(125, 145, '12:40:29', 84, 84, 'Operador', '2020-12-16'),
(126, 600, '12:40:29', 84, 84, 'Supervisor', '2020-12-16'),
(127, 664, '12:41', 84, 84, 'Aduana', '2020-12-16'),
(128, 740, '14:40:25', 85, 85, 'Operador', '2020-12-18'),
(129, 600, '14:40:25', 85, 85, 'Supervisor', '2020-12-18'),
(130, 746, '15:51:16', 86, 86, 'Operador', '2021-01-05'),
(131, 600, '15:51:16', 86, 86, 'Supervisor', '2021-01-05'),
(132, 664, '15:52', 86, 86, 'Aduana', '2021-01-05'),
(133, 757, '16:05:09', 87, 87, 'Operador', '2021-01-05'),
(134, 600, '16:05:09', 87, 87, 'Supervisor', '2021-01-05'),
(135, 727, '16:18', 87, 87, 'Aduana', '2021-01-05'),
(136, 738, '16:16:35', 88, 88, 'Operador', '2021-01-06'),
(137, 600, '16:16:35', 88, 88, 'Supervisor', '2021-01-06'),
(138, 727, '16:18', 88, 88, 'Aduana', '2021-01-06'),
(139, 747, '15:52:44', 89, 89, 'Operador', '2021-01-08'),
(140, 600, '15:52:44', 89, 89, 'Supervisor', '2021-01-08'),
(141, 739, '12:45:49', 90, 90, 'Operador', '2021-01-13'),
(142, 600, '12:45:49', 90, 90, 'Supervisor', '2021-01-13'),
(143, 656, '12:47', 90, 90, 'Aduana', '2021-01-13'),
(144, 738, '12:59:48', 91, 91, 'Operador', '2021-01-13'),
(145, 600, '12:59:48', 91, 91, 'Supervisor', '2021-01-13'),
(146, 656, '13:21', 91, 91, 'Aduana', '2021-01-13'),
(147, 742, '15:42:55', 92, 92, 'Operador', '2021-01-14'),
(148, 600, '15:42:55', 92, 92, 'Supervisor', '2021-01-14'),
(149, 656, '15:44', 92, 92, 'Aduana', '2021-01-14'),
(150, 174, '15:46:08', 93, 93, 'Operador', '2021-01-14'),
(151, 600, '15:46:08', 93, 93, 'Supervisor', '2021-01-14'),
(152, 656, '15:47', 93, 93, 'Aduana', '2021-01-14'),
(153, 738, '12:14:00', 94, 94, 'Operador', '2021-01-15'),
(154, 600, '12:14:00', 94, 94, 'Supervisor', '2021-01-15'),
(155, 755, '12:18:53', 95, 95, 'Operador', '2021-01-15'),
(156, 600, '12:18:53', 95, 95, 'Supervisor', '2021-01-15'),
(157, 656, '12:22', 94, 94, 'Aduana', '2021-01-15'),
(158, 656, '12:22', 95, 95, 'Aduana', '2021-01-15'),
(159, 753, '15:03:51', 96, 96, 'Operador', '2021-01-18'),
(160, 600, '15:03:52', 96, 96, 'Supervisor', '2021-01-18'),
(161, 756, '15:39:03', 97, 97, 'Operador', '2021-01-18'),
(162, 600, '15:39:03', 97, 97, 'Supervisor', '2021-01-18'),
(163, 656, '13:06', 97, 97, 'Aduana', '2021-01-19'),
(164, 656, '13:07', 85, 85, 'Aduana', '2021-01-19'),
(165, 656, '13:07', 89, 89, 'Aduana', '2021-01-19'),
(166, 656, '13:07', 96, 96, 'Aduana', '2021-01-19'),
(167, 740, '14:29:47', 99, 99, 'Operador', '2021-01-21'),
(168, 600, '14:29:47', 99, 99, 'Supervisor', '2021-01-21'),
(169, 656, '14:41', 99, 99, 'Aduana', '2021-01-21'),
(170, 740, '14:54:38', 100, 100, 'Operador', '2021-01-21'),
(171, 600, '14:54:39', 100, 100, 'Supervisor', '2021-01-21'),
(172, 755, '12:42:48', 101, 101, 'Operador', '2021-01-22'),
(173, 600, '12:42:48', 101, 101, 'Supervisor', '2021-01-22'),
(174, 743, '12:47:10', 102, 102, 'Operador', '2021-01-22'),
(175, 600, '12:47:10', 102, 102, 'Supervisor', '2021-01-22'),
(176, 656, '12:59', 100, 100, 'Aduana', '2021-01-22'),
(177, 656, '13:00', 101, 101, 'Aduana', '2021-01-22'),
(178, 656, '13:00', 102, 102, 'Aduana', '2021-01-22'),
(179, 746, '14:55:47', 104, 104, 'Operador', '2021-01-27'),
(180, 600, '14:55:47', 104, 104, 'Supervisor', '2021-01-27'),
(181, 745, '15:04:30', 103, 103, 'Operador', '2021-01-27'),
(182, 600, '15:04:30', 103, 103, 'Supervisor', '2021-01-27'),
(183, 744, '14:53:44', 105, 105, 'Operador', '2021-02-18'),
(184, 600, '14:53:44', 105, 105, 'Supervisor', '2021-02-18'),
(185, 747, '14:58:06', 106, 106, 'Operador', '2021-02-18'),
(186, 600, '14:58:06', 106, 106, 'Supervisor', '2021-02-18'),
(187, 656, '15:00', 103, 103, 'Aduana', '2021-02-18'),
(188, 656, '15:00', 104, 104, 'Aduana', '2021-02-18'),
(189, 656, '15:01', 105, 105, 'Aduana', '2021-02-18'),
(190, 656, '15:01', 106, 106, 'Aduana', '2021-02-18'),
(191, 148, '11:47:40', 107, 107, 'Operador', '2021-02-24'),
(192, 600, '11:47:40', 107, 107, 'Supervisor', '2021-02-24'),
(193, 656, '11:48', 107, 107, 'Aduana', '2021-02-24'),
(194, 745, '10:38:29', 108, 108, 'Operador', '2021-04-19'),
(195, 600, '10:38:29', 108, 108, 'Supervisor', '2021-04-19'),
(196, 745, '14:44:36', 112, 112, 'Operador', '2021-06-15'),
(197, 600, '14:44:36', 112, 112, 'Supervisor', '2021-06-15'),
(198, 738, '14:50:27', 111, 111, 'Operador', '2021-06-15'),
(199, 600, '14:50:27', 111, 111, 'Supervisor', '2021-06-15'),
(200, 745, '14:58:20', 110, 110, 'Operador', '2021-06-15'),
(201, 600, '14:58:20', 110, 110, 'Supervisor', '2021-06-15'),
(202, 745, '21:57:55', 114, 114, 'Operador', '2021-06-21'),
(203, 600, '21:57:55', 114, 114, 'Supervisor', '2021-06-21'),
(204, 746, '20:04:52', 115, 115, 'Operador', '2021-06-22'),
(205, 600, '20:04:52', 115, 115, 'Supervisor', '2021-06-22'),
(206, 737, '20:09:13', 116, 116, 'Operador', '2021-06-22'),
(207, 600, '20:09:13', 116, 116, 'Supervisor', '2021-06-22'),
(208, 741, '04:45:48', 117, 117, 'Operador', '2021-06-24'),
(209, 600, '04:45:48', 117, 117, 'Supervisor', '2021-06-24'),
(210, 757, '04:56:40', 118, 118, 'Operador', '2021-06-24'),
(211, 600, '04:56:40', 118, 118, 'Supervisor', '2021-06-24'),
(212, 753, '09:07:22', 119, 119, 'Operador', '2021-06-30'),
(213, 600, '09:07:22', 119, 119, 'Supervisor', '2021-06-30'),
(214, 758, '10:42:48', 120, 120, 'Operador', '2021-07-21'),
(215, 600, '10:42:49', 120, 120, 'Supervisor', '2021-07-21'),
(216, 748, '15:49:24', 121, 121, 'Operador', '2021-07-21'),
(217, 600, '15:49:24', 121, 121, 'Supervisor', '2021-07-21'),
(218, 756, '15:58:59', 122, 122, 'Operador', '2021-07-21'),
(219, 600, '15:58:59', 122, 122, 'Supervisor', '2021-07-21'),
(220, 737, '16:12:21', 123, 123, 'Operador', '2021-07-21'),
(221, 600, '16:12:21', 123, 123, 'Supervisor', '2021-07-21'),
(222, 755, '16:31:40', 124, 124, 'Operador', '2021-07-21'),
(223, 600, '16:31:41', 124, 124, 'Supervisor', '2021-07-21'),
(224, 737, '08:56:25', 125, 125, 'Operador', '2021-07-22'),
(225, 600, '08:56:25', 125, 125, 'Supervisor', '2021-07-22'),
(226, 746, '09:33:36', 126, 126, 'Operador', '2021-07-22'),
(227, 600, '09:33:36', 126, 126, 'Supervisor', '2021-07-22'),
(228, 66, '13:50:19', 128, 128, 'Operador', '2021-08-03'),
(229, 716, '15:31', 108, 108, 'Aduana', '2021-08-03'),
(231, 684, '15:36', 128, 128, 'Aduana', '2021-08-03'),
(232, 69, '12:46:32', 129, 129, 'Operador', '2021-08-04'),
(233, 2, '12:46:32', 129, 129, 'Supervisor', '2021-08-04'),
(234, 684, '13:28', 129, 129, 'Aduana', '2021-08-04'),
(235, 42, '12:33:34', 131, 131, 'Operador', '2021-08-06'),
(236, 2, '12:33:34', 131, 131, 'Supervisor', '2021-08-06'),
(237, 42, '00:48:37', 132, 132, 'Operador', '2021-08-10'),
(238, 2, '00:48:37', 132, 132, 'Supervisor', '2021-08-10'),
(239, 47, '20:38:58', 134, 134, 'Operador', '2021-08-10'),
(240, 2, '20:38:59', 134, 134, 'Supervisor', '2021-08-10'),
(241, 34, '13:24:54', 139, 139, 'Operador', '2021-08-18'),
(242, 2, '13:24:54', 139, 139, 'Supervisor', '2021-08-18'),
(243, 684, '18:35', 139, 139, 'Aduana', '2021-08-18'),
(244, 42, '11:26:23', 141, 141, 'Operador', '2021-08-19'),
(245, 2, '11:26:23', 141, 141, 'Supervisor', '2021-08-19'),
(246, 684, '12:00', 141, 141, 'Aduana', '2021-08-19'),
(247, 123, '10:26:54', 142, 142, 'Operador', '2021-08-20'),
(248, 2, '10:26:55', 142, 142, 'Supervisor', '2021-08-20'),
(249, 684, '11:07', 142, 142, 'Aduana', '2021-08-20'),
(250, 58, '14:10:25', 143, 143, 'Operador', '2021-08-20'),
(251, 2, '14:10:25', 143, 143, 'Supervisor', '2021-08-20'),
(252, 684, '14:14', 143, 143, 'Aduana', '2021-08-20'),
(253, 747, '15:09:51', 147, 147, 'Operador', '2021-08-24'),
(254, 600, '15:09:51', 147, 147, 'Supervisor', '2021-08-24'),
(255, 727, '15:11', 147, 147, 'Aduana', '2021-08-24'),
(256, 740, '15:40:06', 148, 148, 'Operador', '2021-08-24'),
(257, 600, '15:40:06', 148, 148, 'Supervisor', '2021-08-24'),
(258, 727, '15:42', 148, 148, 'Aduana', '2021-08-24'),
(259, 66, '02:24:23', 152, 152, 'Operador', '2021-08-26'),
(260, 2, '02:24:24', 152, 152, 'Supervisor', '2021-08-26'),
(261, 684, '02:54', 152, 152, 'Aduana', '2021-08-26'),
(262, 745, '09:08:33', 154, 154, 'Operador', '2021-08-26'),
(263, 600, '09:08:33', 154, 154, 'Supervisor', '2021-08-26'),
(264, 757, '09:15:42', 155, 155, 'Operador', '2021-08-26'),
(265, 600, '09:15:42', 155, 155, 'Supervisor', '2021-08-26'),
(266, 35, '06:28:58', 158, 158, 'Operador', '2021-08-27'),
(267, 2, '06:28:58', 158, 158, 'Supervisor', '2021-08-27'),
(268, 684, '06:29', 158, 158, 'Aduana', '2021-08-27'),
(269, 727, '08:17', 155, 155, 'Aduana', '2021-08-27'),
(270, 727, '08:18', 154, 154, 'Aduana', '2021-08-27'),
(271, 727, '08:26', 125, 125, 'Aduana', '2021-08-27'),
(272, 738, '08:33:54', 159, 159, 'Operador', '2021-08-27'),
(273, 600, '08:33:54', 159, 159, 'Supervisor', '2021-08-27'),
(274, 727, '08:35', 159, 159, 'Aduana', '2021-08-27'),
(275, 149, '09:58:33', 160, 160, 'Operador', '2021-08-27'),
(276, 600, '09:58:33', 160, 160, 'Supervisor', '2021-08-27'),
(277, 750, '12:30:13', 161, 161, 'Operador', '2021-08-27'),
(278, 600, '12:30:13', 161, 161, 'Supervisor', '2021-08-27'),
(279, 727, '12:31', 161, 161, 'Aduana', '2021-08-27'),
(280, 737, '08:49:40', 163, 163, 'Operador', '2021-08-30'),
(281, 600, '08:49:41', 163, 163, 'Supervisor', '2021-08-30'),
(282, 684, '18:12', 131, 131, 'Aduana', '2021-08-30'),
(283, 738, '18:30:25', 165, 165, 'Operador', '2021-08-30'),
(284, 600, '18:30:26', 165, 165, 'Supervisor', '2021-08-30'),
(285, 742, '18:36:44', 166, 166, 'Operador', '2021-08-30'),
(286, 600, '18:36:45', 166, 166, 'Supervisor', '2021-08-30'),
(287, 42, '16:05:43', 167, 167, 'Operador', '2021-08-31'),
(288, 2, '16:05:43', 167, 167, 'Supervisor', '2021-08-31'),
(289, 131, '07:58:52', 168, 168, 'Operador', '2021-09-02'),
(290, 2, '07:58:53', 168, 168, 'Supervisor', '2021-09-02'),
(291, 667, '08:01', 168, 168, 'Aduana', '2021-09-02'),
(292, 727, '08:03', 166, 166, 'Aduana', '2021-09-21'),
(293, 727, '08:12', 165, 165, 'Aduana', '2021-09-21'),
(294, 727, '08:13', 163, 163, 'Aduana', '2021-09-21'),
(295, 727, '08:15', 160, 160, 'Aduana', '2021-09-21'),
(296, 727, '08:23', 124, 124, 'Aduana', '2021-09-21'),
(297, 727, '08:24', 123, 123, 'Aduana', '2021-09-21'),
(298, 727, '08:35', 121, 121, 'Aduana', '2021-09-21'),
(299, 727, '08:40', 120, 120, 'Aduana', '2021-09-21'),
(300, 727, '08:42', 126, 126, 'Aduana', '2021-09-21'),
(301, 751, '15:22:20', 169, 169, 'Operador', '2021-09-21'),
(302, 600, '15:22:20', 169, 169, 'Supervisor', '2021-09-21'),
(303, 740, '16:29:30', 171, 171, 'Operador', '2021-09-21'),
(304, 600, '16:29:30', 171, 171, 'Supervisor', '2021-09-21'),
(305, 727, '16:33', 169, 169, 'Aduana', '2021-09-21'),
(306, 727, '16:34', 171, 171, 'Aduana', '2021-09-21'),
(307, 745, '14:33:11', 172, 172, 'Operador', '2021-09-22'),
(308, 600, '14:33:12', 172, 172, 'Supervisor', '2021-09-22'),
(309, 746, '15:15:10', 173, 173, 'Operador', '2021-09-22'),
(310, 600, '15:15:10', 173, 173, 'Supervisor', '2021-09-22'),
(311, 749, '14:25:42', 174, 174, 'Operador', '2021-09-23'),
(312, 600, '14:25:42', 174, 174, 'Supervisor', '2021-09-23'),
(313, 727, '23:39', 172, 172, 'Aduana', '2021-09-27'),
(314, 727, '23:39', 173, 173, 'Aduana', '2021-09-27'),
(315, 727, '23:39', 174, 174, 'Aduana', '2021-09-27'),
(316, 49, '12:06:07', 180, 180, 'Operador', '2021-09-29'),
(317, 2, '12:06:07', 180, 180, 'Supervisor', '2021-09-29');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coiling_material_a`
--

CREATE TABLE `coiling_material_a` (
  `id_coiling_mat` int(11) NOT NULL,
  `no_tira` int(11) NOT NULL,
  `metros` double NOT NULL,
  `dasprodcoi_iddasprodcoi` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coiling_material_s`
--

CREATE TABLE `coiling_material_s` (
  `id_coiling_mat` int(11) NOT NULL,
  `no_tira` int(11) NOT NULL,
  `metros` double NOT NULL,
  `dasprodcoi_iddasprodcoi` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `corriendoactualmente`
--

CREATE TABLE `corriendoactualmente` (
  `id_corriendo` int(11) NOT NULL,
  `linea` varchar(20) NOT NULL,
  `ordenActual` varchar(50) NOT NULL,
  `mogActual` varchar(50) NOT NULL,
  `hora_inicio` varchar(20) NOT NULL,
  `fecha` date NOT NULL,
  `activo` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `corriendoactualmente`
--

INSERT INTO `corriendoactualmente` (`id_corriendo`, `linea`, `ordenActual`, `mogActual`, `hora_inicio`, `fecha`, `activo`) VALUES
(1, 'TP03', 'PRS001526', 'MOG001676', '10:31', '2020-10-22', 1),
(2, 'TP03', 'PRS001526', 'MOG001676', '14:23', '2020-10-22', 1),
(3, 'TP05', 'PRS001527', 'MOG001677', '14:33', '2020-10-22', 1),
(4, 'TP06', 'PRS002356', 'MOG002711', '14:45', '2020-10-22', 1),
(5, 'TP01', 'PRS001478', 'MOG001622', '15:02', '2020-10-22', 0),
(6, 'TP11', 'PRS002358', 'MOG002713', '15:29', '2020-10-22', 1),
(7, 'TP11', 'PRS002329', 'MOG002684', '15:33', '2020-10-22', 1),
(8, 'TP08', 'PRS002821', 'MOG003254', '15:48', '2020-10-22', 1),
(9, 'TP09', 'PRS006974', 'MOG008374', '15:55', '2020-10-22', 1),
(10, 'TP01', 'PRS001346', 'MOG001485', '16:03', '2020-10-22', 1),
(11, 'TP01', 'PRS003624', 'MOG004139', '17:01', '2020-10-22', 1),
(12, 'TP02', 'PRS012456', 'MOG015754', '17:05', '2020-10-22', 0),
(13, 'TP03', 'PRS001478', 'MOG001622', '18:56', '2020-10-22', 0),
(14, 'TP03', 'PRS001689', 'MOG001852', '07:16:30', '2020-10-23', 1),
(15, 'TP02', 'PRS007874', 'MOG009622', '07:52:36', '2020-10-23', 1),
(16, 'TP02', 'PRS001245', 'MOG001375', '08:17:30', '2020-10-23', 0),
(17, 'TP03', 'PRS001246', 'MOG001376', '14:38:32', '2020-10-23', 1),
(18, 'TP07', 'PRS012349', 'MOG015579', '14:50:54', '2020-10-23', 1),
(19, 'TP06', 'PRS031712', 'MOG045435', '16:01:55', '2020-10-23', 1),
(20, 'TP01', 'PRS012456', 'MOG015754', '16:08:22', '2020-10-23', 0),
(21, 'TP02', 'PRS012456', 'MOG015754', '16:14:26', '2020-10-23', 0),
(22, 'TP04', 'PRS031665', 'MOG045385', '08:45:26', '2020-10-26', 1),
(23, 'TP04', 'PRS031665', 'MOG045385', '10:36:19', '2020-10-26', 1),
(24, 'TP01', 'PRS023587', 'MOG032388', '15:10:49', '2020-10-26', 1),
(25, 'TP11', 'PRS004752', 'MOG005542', '15:34:40', '2020-10-26', 1),
(26, 'TP11', 'PRS001425', 'MOG001569', '15:58:52', '2020-10-26', 1),
(27, 'TP01', 'PRS001478', 'MOG001622', '18:02:39', '2020-10-26', 0),
(28, 'TP01', 'PRS001478', 'MOG001622', '18:05:50', '2020-10-26', 0),
(29, 'TP41', 'PRS005896', 'MOG007041', '18:18:00', '2020-10-26', 0),
(30, 'TP06', 'PRS005896', 'MOG007041', '18:21:47', '2020-10-26', 0),
(31, 'TP41', 'PRS031635', 'MOG045312', '11:02:01', '2020-10-27', 1),
(32, 'TP41', 'PRS031499', 'MOG045101', '11:04:11', '2020-10-27', 1),
(33, 'TP41', 'PRS031635', 'MOG045312', '11:09:00', '2020-10-27', 1),
(34, 'TP41', 'PRS031635', 'MOG045312', '11:09:29', '2020-10-27', 1),
(35, 'TP41', 'PRS031635', 'MOG045312', '11:10:49', '2020-10-27', 1),
(36, 'TP41', 'PRS031635', 'MOG045312', '11:22:12', '2020-10-27', 1),
(37, 'TP41', 'PRS031635', 'MOG045312', '11:23:45', '2020-10-27', 1),
(38, 'TP11', 'PRS031679', 'MOG045402', '15:53:01', '2020-10-27', 1),
(39, 'TP11', 'PRS031683', 'MOG045406', '16:01:48', '2020-10-27', 1),
(40, 'TP11', 'PRS031707', 'MOG045430', '16:14:20', '2020-10-27', 1),
(41, 'TP08', 'PRS031730', 'MOG045455', '15:50:45', '2020-10-30', 0),
(42, 'TP08', 'PRS031730', 'MOG045455', '15:58:08', '2020-10-30', 0),
(43, 'TP08', 'PRS031730', 'MOG045455', '16:01:28', '2020-10-30', 0),
(44, 'TP08', 'PRS031794', 'MOG045549', '16:05:08', '2020-10-30', 0),
(45, 'TP08', 'PRS031794', 'MOG045549', '16:21:14', '2020-10-30', 0),
(46, 'TP08', 'PRS031795', 'MOG045550', '11:48:12', '2020-11-02', 0),
(47, 'TP08', 'PRS031792', 'MOG045547', '11:50:38', '2020-11-02', 0),
(48, 'TP08', 'PRS031795', 'MOG045550', '11:58:14', '2020-11-02', 0),
(49, 'TP01', 'PRS002366', 'MOG002721', '18:07:13', '2020-11-02', 1),
(50, 'TP03', 'PRS031790', 'MOG045545', '13:40:48', '2020-11-03', 1),
(51, 'TP03', 'PRS031791', 'MOG045546', '13:41:17', '2020-11-03', 0),
(52, 'TP03', 'PRS031791', 'MOG045546', '13:45:30', '2020-11-03', 0),
(53, 'TP03', 'PRS031790', 'MOG045545', '13:48:22', '2020-11-03', 1),
(54, 'TP03', 'PRS031791', 'MOG045546', '13:52:30', '2020-11-03', 0),
(55, 'TP03', 'PRS031790', 'MOG045545', '13:52:44', '2020-11-03', 1),
(56, 'TP03', 'PRS031791', 'MOG045546', '13:55:51', '2020-11-03', 0),
(57, 'TP03', 'PRS031691', 'MOG045414', '13:59:09', '2020-11-03', 1),
(58, 'TP01', 'PRS031769', 'MOG045518', '14:10:14', '2020-11-04', 1),
(59, 'TP01', 'PRS031797', 'MOG045552', '14:10:29', '2020-11-04', 0),
(60, 'TP01', 'PRS031772', 'MOG045521', '14:17:39', '2020-11-04', 0),
(61, 'TP01', 'PRS031769', 'MOG045518', '14:19:18', '2020-11-04', 1),
(62, 'TP01', 'PRS031772', 'MOG045521', '14:20:01', '2020-11-04', 0),
(63, 'TP01', 'PRS031769', 'MOG045518', '14:24:55', '2020-11-04', 1),
(64, 'TP03', 'PRS003698', 'MOG004222', '15:04:00', '2020-11-04', 1),
(65, 'TP05', 'PRS031696', 'MOG045419', '11:49:50', '2020-11-05', 0),
(66, 'TP05', 'PRS031361', 'MOG044882', '11:50:29', '2020-11-05', 0),
(67, 'TP05', 'PRS031361', 'MOG044882', '11:55:09', '2020-11-05', 0),
(68, 'TP01', 'PRS031851', 'MOG045614', '13:51:36', '2020-11-06', 0),
(69, 'TP01', 'PRS031853', 'MOG045616', '13:52:03', '2020-11-06', 0),
(70, 'TP01', 'PRS031851', 'MOG045614', '13:56:58', '2020-11-06', 0),
(71, 'TP05', 'PRS031904', 'MOG045737', '11:14:36', '2020-11-10', 0),
(72, 'TP05', 'PRS031904', 'MOG045737', '11:35:48', '2020-11-10', 0),
(73, 'TP05', 'PRS031905', 'MOG045738', '12:09:09', '2020-11-10', 0),
(74, 'TP01', 'PRS002356', 'MOG002711', '17:59:38', '2020-11-10', 1),
(75, 'TP03', 'PRS004989', 'MOG005867', '18:07:12', '2020-11-10', 1),
(76, 'TP04', 'PRS031835', 'MOG045598', '12:23:58', '2020-11-11', 1),
(77, 'TP05', 'PRS031953', 'MOG045812', '15:32:06', '2020-11-12', 0),
(78, 'TP05', 'PRS031953', 'MOG045812', '15:55:57', '2020-11-12', 0),
(79, 'TP05', 'PRS031967', 'MOG045857', '16:07:08', '2020-11-12', 0),
(80, 'TP05', 'PRS031967', 'MOG045857', '16:16:01', '2020-11-12', 0),
(81, 'TP07', 'PRS031959', 'MOG045818', '15:18:02', '2020-11-13', 0),
(82, 'TP07', 'PRS031826', 'MOG045589', '15:25:33', '2020-11-13', 0),
(83, 'TP07', 'PRS031959', 'MOG045818', '15:26:31', '2020-11-13', 0),
(84, 'TP07', 'PRS031826', 'MOG045589', '15:33:48', '2020-11-13', 0),
(85, 'TP07', 'PRS031826', 'MOG045589', '15:36:39', '2020-11-13', 0),
(86, 'TP07', 'PRS031826', 'MOG045589', '15:41:58', '2020-11-13', 0),
(87, 'TP07', 'PRS031826', 'MOG045589', '15:47:28', '2020-11-13', 0),
(88, 'TP11', 'PRS031650', 'MOG045353', '10:15:30', '2020-11-17', 1),
(89, 'TP10', 'PRS032011', 'MOG045901', '15:37:14', '2020-11-17', 0),
(90, 'TP10', 'PRS032011', 'MOG045901', '15:38:02', '2020-11-17', 0),
(91, 'TP10', 'PRS032011', 'MOG045901', '15:44:54', '2020-11-17', 0),
(92, 'TP10', 'PRS032012', 'MOG045902', '15:53:10', '2020-11-17', 0),
(93, 'TP10', 'PRS032051', 'MOG045975', '07:26:05', '2020-11-19', 1),
(94, 'TP01', 'PRS031999', 'MOG045889', '07:35:32', '2020-11-19', 0),
(95, 'TP10', 'PRS032051', 'MOG045975', '10:29:41', '2020-11-19', 1),
(96, 'TP01', 'PRS031999', 'MOG045889', '10:44:31', '2020-11-19', 0),
(97, 'TP10', 'PRS032051', 'MOG045975', '13:05:48', '2020-11-19', 1),
(98, 'TP10', 'PRS032051', 'MOG045975', '16:52:53', '2020-11-19', 1),
(99, 'TP01', 'PRS032083', 'MOG046037', '08:11:26', '2020-11-21', 0),
(100, 'TP41', 'PRS032077', 'MOG046031', '08:40:16', '2020-11-21', 0),
(101, 'TP01', 'PRS032083', 'MOG046037', '12:59:55', '2020-11-21', 0),
(102, 'TP01', 'PRS032083', 'MOG046037', '13:00:18', '2020-11-21', 0),
(103, 'TP01', 'PRS032083', 'MOG046037', '13:01:04', '2020-11-21', 0),
(104, 'TP41', 'PRS032054', 'MOG045995', '15:16:07', '2020-11-23', 0),
(105, 'TP41', 'PRS032075', 'MOG046029', '15:28:22', '2020-11-23', 0),
(106, 'TP03', 'PRS032136', 'MOG046134', '16:05:03', '2020-11-24', 0),
(107, 'TP03', 'PRS032162', 'MOG046164', '16:06:06', '2020-11-24', 0),
(108, 'TP03', 'PRS032162', 'MOG046164', '16:14:47', '2020-11-24', 0),
(109, 'TP10', 'PRS032105', 'MOG046079', '15:49:31', '2020-11-25', 0),
(110, 'TP10', 'PRS032105', 'MOG046079', '15:54:17', '2020-11-25', 0),
(111, 'TP10', 'PRS031834', 'MOG045597', '10:07:22', '2020-11-30', 1),
(112, 'TP02', 'PRS032258', 'MOG046293', '10:09:56', '2020-11-30', 0),
(113, 'TP02', 'PRS032182', 'MOG046187', '10:18:47', '2020-11-30', 0),
(114, 'TP02', 'PRS032258', 'MOG046293', '11:07:44', '2020-11-30', 0),
(115, 'TP09', 'PRS032339', 'MOG046416', '15:33:02', '2020-12-02', 0),
(116, 'TP09', 'PRS032295', 'MOG046344', '15:33:08', '2020-12-02', 0),
(117, 'TP09', 'PRS032339', 'MOG046416', '15:37:45', '2020-12-02', 0),
(118, 'TP09', 'PRS032295', 'MOG046344', '15:41:28', '2020-12-02', 0),
(119, 'TP04', 'PRS032309', 'MOG046386', '14:31:32', '2020-12-07', 0),
(120, 'TP04', 'PRS032309', 'MOG046386', '14:32:15', '2020-12-07', 0),
(121, 'TP04', 'PRS032346', 'MOG046425', '14:34:34', '2020-12-07', 0),
(122, 'TP04', 'PRS032346', 'MOG046425', '14:34:35', '2020-12-07', 0),
(123, 'TP04', 'PRS032309', 'MOG046386', '14:38:16', '2020-12-07', 0),
(124, 'TP04', 'PRS032346', 'MOG046425', '14:40:24', '2020-12-07', 0),
(125, 'TP01', 'PRS000555', 'MOG000609', '08:26:38', '2020-12-08', 1),
(126, 'TP01', 'PRS000555', 'MOG000609', '08:27:39', '2020-12-08', 1),
(127, 'TP41', 'PRS032359', 'MOG046449', '11:26:06', '2020-12-08', 0),
(128, 'TP41', 'PRS032359', 'MOG046449', '11:33:44', '2020-12-08', 0),
(129, 'TP41', 'PRS032345', 'MOG046424', '11:39:42', '2020-12-08', 0),
(130, 'TP41', 'PRS032344', 'MOG046423', '13:33:16', '2020-12-08', 0),
(131, 'TP41', 'PRS032344', 'MOG046423', '13:39:18', '2020-12-08', 0),
(132, 'TP09', 'PRS032326', 'MOG046403', '11:38:10', '2020-12-09', 0),
(133, 'TP09', 'PRS032326', 'MOG046403', '11:42:00', '2020-12-09', 0),
(134, 'TP09', 'PRS032394', 'MOG046493', '12:32:20', '2020-12-09', 0),
(135, 'TP09', 'PRS032394', 'MOG046493', '12:37:22', '2020-12-09', 0),
(136, 'TP05', 'PRS032378', 'MOG046477', '11:53:03', '2020-12-10', 0),
(137, 'TP05', 'PRS032379', 'MOG046478', '11:59:34', '2020-12-10', 0),
(138, 'TP05', 'PRS032378', 'MOG046477', '12:03:50', '2020-12-10', 0),
(139, 'TP05', 'PRS032379', 'MOG046478', '12:06:15', '2020-12-10', 0),
(140, 'TP02', 'PRS032269', 'MOG046318', '15:45:03', '2020-12-11', 0),
(141, 'TP02', 'PRS032384', 'MOG046483', '15:45:31', '2020-12-11', 0),
(142, 'TP01', 'PRS032384', 'MOG046483', '15:51:45', '2020-12-11', 0),
(143, 'TP04', 'PRS032411', 'MOG046526', '12:27:30', '2020-12-16', 0),
(144, 'TP04', 'PRS032411', 'MOG046526', '12:34:20', '2020-12-16', 0),
(145, 'TP41', 'PRS032543', 'MOG046762', '07:13:43', '2020-12-18', 0),
(146, 'TP41', 'PRS032543', 'MOG046762', '14:36:53', '2020-12-18', 0),
(147, 'TP05', 'PRS032527', 'MOG046727', '15:47:29', '2021-01-05', 0),
(148, 'TP10', 'PRS032561', 'MOG046780', '15:49:30', '2021-01-05', 0),
(149, 'TP10', 'PRS032561', 'MOG046780', '15:56:16', '2021-01-05', 0),
(150, 'TP05', 'PRS032554', 'MOG046773', '16:05:54', '2021-01-06', 0),
(151, 'TP05', 'PRS032554', 'MOG046773', '16:12:43', '2021-01-06', 0),
(152, 'TP09', 'PRS032586', 'MOG046855', '15:47:14', '2021-01-08', 0),
(153, 'TP09', 'PRS032586', 'MOG046855', '15:50:33', '2021-01-08', 0),
(154, 'TP41', 'PRS032654', 'MOG046953', '12:34:48', '2021-01-13', 0),
(155, 'TP41', 'PRS032654', 'MOG046953', '12:40:59', '2021-01-13', 0),
(156, 'TP41', 'PRS032682', 'MOG046998', '12:51:12', '2021-01-13', 0),
(157, 'TP41', 'PRS032682', 'MOG046998', '12:56:36', '2021-01-13', 0),
(158, 'TP03', 'PRS032557', 'MOG046776', '15:30:16', '2021-01-14', 0),
(159, 'TP02', 'PRS032611', 'MOG046894', '15:34:33', '2021-01-14', 0),
(160, 'TP03', 'PRS032557', 'MOG046776', '15:37:51', '2021-01-14', 0),
(161, 'TP01', 'PRS032659', 'MOG046958', '12:01:37', '2021-01-15', 0),
(162, 'TP01', 'PRS032659', 'MOG046958', '12:07:18', '2021-01-15', 0),
(163, 'TP03', 'PRS032501', 'MOG046698', '12:15:36', '2021-01-15', 0),
(164, 'TP01', 'PRS032629', 'MOG046912', '14:58:18', '2021-01-18', 0),
(165, 'TP02', 'PRS032666', 'MOG046965', '15:05:24', '2021-01-18', 0),
(166, 'TP02', 'PRS032666', 'MOG046965', '15:28:07', '2021-01-18', 0),
(167, 'TP02', 'PRS032666', 'MOG046965', '15:33:59', '2021-01-18', 0),
(168, 'TP09', 'PRS032739', 'MOG047095', '11:38:06', '2021-01-19', 1),
(169, 'TP11', 'PRS032784', 'MOG047141', '14:21:57', '2021-01-21', 0),
(170, 'TP11', 'PRS032784', 'MOG047141', '14:26:02', '2021-01-21', 0),
(171, 'TP11', 'PRS032811', 'MOG047182', '14:43:33', '2021-01-21', 0),
(172, 'TP11', 'PRS032811', 'MOG047182', '14:47:36', '2021-01-21', 0),
(173, 'TP11', 'PRS032811', 'MOG047182', '14:51:07', '2021-01-21', 0),
(174, 'TP09', 'PRS032765', 'MOG047121', '12:31:20', '2021-01-22', 0),
(175, 'TP09', 'PRS032765', 'MOG047121', '12:34:08', '2021-01-22', 0),
(176, 'TP10', 'PRS032823', 'MOG047194', '12:44:02', '2021-01-22', 0),
(177, 'TP04', 'PRS032726', 'MOG047074', '14:43:59', '2021-01-27', 0),
(178, 'TP02', 'PRS032810', 'MOG047181', '14:52:40', '2021-01-27', 0),
(179, 'TP04', 'PRS032726', 'MOG047074', '15:01:07', '2021-01-27', 0),
(180, 'TP09', 'PRS033273', 'MOG047960', '14:42:03', '2021-02-18', 0),
(181, 'TP09', 'PRS033273', 'MOG047960', '14:50:29', '2021-02-18', 0),
(182, 'TP02', 'PRS033245', 'MOG047910', '14:55:23', '2021-02-18', 0),
(183, 'TP11', 'PRS033378', 'MOG048158', '11:39:59', '2021-02-24', 0),
(184, 'TP11', 'PRS033378', 'MOG048158', '11:44:15', '2021-02-24', 0),
(185, 'TP01', 'PRS033984', 'MOG049163', '10:29:57', '2021-04-19', 0),
(186, 'TP01', 'PRS035180', 'MOG051089', '11:03:26', '2021-06-14', 1),
(187, 'TP01', 'PRS035180', 'MOG051089', '12:22:58', '2021-06-14', 1),
(188, 'TP01', 'PRS035180', 'MOG051089', '12:34:32', '2021-06-14', 1),
(189, 'TP01', 'PRS035180', 'MOG051089', '17:04:55', '2021-06-14', 1),
(190, 'TP02', 'PRS035325', 'MOG051273', '08:01:23', '2021-06-15', 0),
(191, 'TP05', 'PRS035245', 'MOG051162', '08:52:41', '2021-06-15', 0),
(192, 'TP09', 'PRS035306', 'MOG051244', '09:01:47', '2021-06-15', 0),
(193, 'TP02', 'PRS035306', 'MOG051244', '14:40:50', '2021-06-15', 0),
(194, 'TP08', 'PRS035245', 'MOG051162', '14:46:27', '2021-06-15', 0),
(195, 'TP02', 'PRS035325', 'MOG051273', '14:52:20', '2021-06-15', 0),
(196, 'TP08', 'PRS035334', 'MOG051286', '13:20:08', '2021-06-17', 1),
(197, 'TP01', 'PRS035446', 'MOG051422', '21:52:46', '2021-06-21', 0),
(198, 'TP04', 'PRS035563', 'MOG051573', '20:01:54', '2021-06-22', 0),
(199, 'TP05', 'PRS035544', 'MOG051542', '20:06:11', '2021-06-22', 0),
(200, 'TP02', 'PRS035584', 'MOG051609', '04:42:17', '2021-06-24', 0),
(201, 'TP03', 'PRS035565', 'MOG051577', '04:50:30', '2021-06-24', 0),
(202, 'TP03', 'PRS035565', 'MOG051577', '04:53:37', '2021-06-24', 0),
(203, 'TP11', 'PRS035547', 'MOG051547', '08:58:41', '2021-06-30', 0),
(204, 'TP11', 'PRS035547', 'MOG051547', '09:04:38', '2021-06-30', 0),
(205, 'TP10', 'PRS035976', 'MOG052297', '10:39:33', '2021-07-21', 0),
(206, 'TP09', 'PRS035936', 'MOG052213', '15:39:31', '2021-07-21', 0),
(207, 'TP09', 'PRS035936', 'MOG052213', '15:42:36', '2021-07-21', 0),
(208, 'TP01', 'PRS035789', 'MOG051984', '15:50:12', '2021-07-21', 0),
(209, 'TP01', 'PRS035789', 'MOG051984', '15:55:04', '2021-07-21', 0),
(210, 'TP02', 'PRS035950', 'MOG052246', '15:59:39', '2021-07-21', 0),
(211, 'TP02', 'PRS035950', 'MOG052246', '16:06:21', '2021-07-21', 0),
(212, 'TP02', 'PRS035950', 'MOG052246', '16:09:35', '2021-07-21', 0),
(213, 'TP10', 'PRS035934', 'MOG052211', '16:23:39', '2021-07-21', 0),
(214, 'TP10', 'PRS035934', 'MOG052211', '16:29:02', '2021-07-21', 0),
(215, 'TP10', 'PRS035985', 'MOG052306', '08:26:12', '2021-07-22', 0),
(216, 'TP10', 'PRS035985', 'MOG052306', '08:35:07', '2021-07-22', 0),
(217, 'TP08', 'PRS035909', 'MOG052170', '08:57:59', '2021-07-22', 0),
(218, 'TP08', 'PRS035909', 'MOG052170', '09:25:06', '2021-07-22', 0),
(219, 'TH01', 'HBL037124', 'MOG052330', '11:42:21', '2021-07-22', 1),
(220, 'TH09', 'HBL037124', 'MOG052330', '11:50:32', '2021-07-22', 1),
(221, 'TH08', 'HBL037304', 'MOG052638', '09:01:48', '2021-08-03', 0),
(222, 'TH02', 'HBL037304', 'MOG052638', '09:09:17', '2021-08-03', 0),
(223, 'TH02', 'HBL037304', 'MOG052638', '11:14:15', '2021-08-03', 0),
(224, 'TH02', 'HBL037336', 'MOG052681', '08:27:14', '2021-08-04', 0),
(225, 'TH13', 'HBL037336', 'MOG052681', '08:38:21', '2021-08-04', 0),
(226, 'TH13', 'HBL037336', 'MOG052681', '11:33:29', '2021-08-04', 0),
(227, 'TH17', 'HBL037205', 'MOG052482', '17:23:54', '2021-08-04', 1),
(228, 'TH17', 'HBL037205', 'MOG052482', '17:23:57', '2021-08-04', 1),
(229, 'TH02', 'HBL037010', 'MOG052163', '11:33:50', '2021-08-06', 0),
(230, 'TH02', 'HBL037010', 'MOG052163', '12:15:32', '2021-08-06', 0),
(231, 'TH02', 'HBL037010', 'MOG052163', '12:27:22', '2021-08-06', 0),
(232, 'TH12', 'HBL037321', 'MOG052659', '00:40:51', '2021-08-10', 0),
(233, 'TH12', 'HBL037321', 'MOG052659', '00:44:10', '2021-08-10', 0),
(234, 'TH03', 'HBL037382', 'MOG052738', '12:46:10', '2021-08-10', 1),
(235, 'TH11', 'HBL037417', 'MOG052793', '20:35:05', '2021-08-10', 0),
(236, 'TH13', 'HBL037423', 'MOG052801', '21:02:00', '2021-08-10', 1),
(237, 'TH13', 'HBL037423', 'MOG052801', '21:06:43', '2021-08-10', 1),
(238, 'TH02', 'HBL037186', 'MOG052450', '11:48:05', '2021-08-11', 1),
(239, 'TH04', 'HBL037435', 'MOG052818', '10:41:17', '2021-08-12', 1),
(240, 'TH04', 'HBL037435', 'MOG052818', '10:41:22', '2021-08-12', 1),
(241, 'TH04', 'HBL037435', 'MOG052818', '10:41:51', '2021-08-12', 1),
(242, 'TH04', 'HBL037435', 'MOG052818', '10:41:55', '2021-08-12', 1),
(243, 'TH04', 'HBL037435', 'MOG052818', '10:42:18', '2021-08-12', 1),
(244, 'TH41', 'HBL037394', 'MOG052757', '12:05:31', '2021-08-17', 1),
(245, 'TH03', 'HBL037454', 'MOG052848', '09:05:12', '2021-08-18', 0),
(246, 'TH03', 'HBL037454', 'MOG052848', '09:44:10', '2021-08-18', 0),
(247, 'TH03', 'HBL037454', 'MOG052848', '10:46:52', '2021-08-18', 0),
(248, 'TH03', 'HBL037454', 'MOG052848', '11:26:52', '2021-08-18', 0),
(249, 'TH03', 'HBL037454', 'MOG052848', '12:38:59', '2021-08-18', 0),
(250, 'TH03', 'HBL037454', 'MOG052848', '13:11:08', '2021-08-18', 0),
(251, 'TP02', 'PRS036305', 'MOG052821', '04:37:07', '2021-08-19', 1),
(252, 'TH10', 'HBL037021', 'MOG052174', '11:09:12', '2021-08-19', 0),
(253, 'TH06', 'HBL037516', 'MOG052997', '08:17:46', '2021-08-20', 0),
(254, 'TH06', 'HBL037516', 'MOG052997', '08:59:19', '2021-08-20', 0),
(255, 'TH06', 'HBL037516', 'MOG052997', '09:58:18', '2021-08-20', 0),
(256, 'TH06', 'HBL037516', 'MOG052997', '10:13:37', '2021-08-20', 0),
(257, 'TH17', 'HBL037426', 'MOG052806', '13:58:45', '2021-08-20', 0),
(258, 'TH17', 'HBL037426', 'MOG052806', '14:04:43', '2021-08-20', 0),
(259, 'TH17', 'HBL037426', 'MOG052806', '14:07:18', '2021-08-20', 0),
(260, 'TH12', 'HBL037557', 'MOG053049', '15:55:59', '2021-08-20', 1),
(261, 'TH12', 'HBL037557', 'MOG053049', '15:56:02', '2021-08-20', 1),
(262, 'TG01', 'PLT014274', 'MOG052277', '16:32:59', '2021-08-20', 1),
(263, 'TG01', 'PLT014292', 'MOG052319', '20:22:25', '2021-08-23', 1),
(264, 'TP41', 'PRS036419', 'MOG053044', '15:06:22', '2021-08-24', 0),
(265, 'TP41', 'PRS036521', 'MOG053187', '15:14:26', '2021-08-24', 0),
(266, 'TP07', 'PRS036521', 'MOG053187', '15:25:43', '2021-08-24', 0),
(267, 'TP11', 'PRS036159', 'MOG052601', '16:13:46', '2021-08-25', 1),
(268, 'TP11', 'PRS036159', 'MOG052601', '17:11:51', '2021-08-25', 1),
(269, 'TP11', 'PRS036159', 'MOG052601', '17:11:56', '2021-08-25', 1),
(270, 'TP11', 'PRS036159', 'MOG052601', '17:11:59', '2021-08-25', 1),
(271, 'TP11', 'PRS036159', 'MOG052601', '17:12:20', '2021-08-25', 1),
(272, 'TP11', 'PRS036159', 'MOG052601', '17:12:22', '2021-08-25', 1),
(273, 'TP11', 'PRS036159', 'MOG052601', '17:13:01', '2021-08-25', 1),
(274, 'TP11', 'PRS036159', 'MOG052601', '17:13:02', '2021-08-25', 1),
(275, 'TP11', 'PRS036159', 'MOG052601', '17:13:07', '2021-08-25', 1),
(276, 'TP11', 'PRS036159', 'MOG052601', '17:13:08', '2021-08-25', 1),
(277, 'TP05', 'PRS036504', 'MOG053162', '17:14:15', '2021-08-25', 1),
(278, 'TP09', 'PRS036448', 'MOG053076', '17:25:53', '2021-08-25', 1),
(279, 'TH05', 'HBL037505', 'MOG052984', '02:13:42', '2021-08-26', 0),
(280, 'TH05', 'HBL037505', 'MOG052984', '02:20:16', '2021-08-26', 0),
(281, 'TH14', 'HBL037634', 'MOG053159', '07:20:03', '2021-08-26', 1),
(282, 'TP11', 'PRS036535', 'MOG053222', '08:43:30', '2021-08-26', 0),
(283, 'TP11', 'PRS036535', 'MOG053222', '08:58:59', '2021-08-26', 0),
(284, 'TP01', 'PRS036582', 'MOG053290', '09:09:32', '2021-08-26', 0),
(285, 'TP08', 'PRS036454', 'MOG053094', '09:16:08', '2021-08-26', 1),
(286, 'TP08', 'PRS036454', 'MOG053094', '09:26:48', '2021-08-26', 1),
(287, 'TP08', 'PRS036454', 'MOG053094', '09:36:21', '2021-08-26', 1),
(288, 'TP08', 'PRS036454', 'MOG053094', '09:36:22', '2021-08-26', 1),
(289, 'TP08', 'PRS036454', 'MOG053094', '09:36:23', '2021-08-26', 1),
(290, 'TP08', 'PRS036454', 'MOG053094', '09:36:28', '2021-08-26', 1),
(291, 'TP08', 'PRS036454', 'MOG053094', '09:36:29', '2021-08-26', 1),
(292, 'TP08', 'PRS036454', 'MOG053094', '09:36:29', '2021-08-26', 1),
(293, 'TP08', 'PRS036454', 'MOG053094', '09:40:53', '2021-08-26', 1),
(294, 'TP08', 'PRS036454', 'MOG053094', '09:40:57', '2021-08-26', 1),
(295, 'TP08', 'PRS036454', 'MOG053094', '09:42:35', '2021-08-26', 1),
(296, 'TP08', 'PRS036454', 'MOG053094', '09:42:35', '2021-08-26', 1),
(297, 'TP08', 'PRS036454', 'MOG053094', '09:42:36', '2021-08-26', 1),
(298, 'TP08', 'PRS036454', 'MOG053094', '09:42:36', '2021-08-26', 1),
(299, 'TP08', 'PRS036454', 'MOG053094', '09:44:16', '2021-08-26', 1),
(300, 'TP08', 'PRS036454', 'MOG053094', '09:44:17', '2021-08-26', 1),
(301, 'TP08', 'PRS036454', 'MOG053094', '09:54:01', '2021-08-26', 1),
(302, 'TP08', 'PRS036454', 'MOG053094', '09:54:01', '2021-08-26', 1),
(303, 'TP08', 'PRS036454', 'MOG053094', '09:54:01', '2021-08-26', 1),
(304, 'TH10', 'HBL037293', 'MOG052604', '14:22:34', '2021-08-26', 1),
(305, 'TH10', 'HBL037293', 'MOG052604', '14:23:11', '2021-08-26', 1),
(306, 'TH10', 'HBL037293', 'MOG052604', '14:23:46', '2021-08-26', 1),
(307, 'TH05', 'HBL037586', 'MOG053093', '06:16:49', '2021-08-27', 0),
(308, 'TH05', 'HBL037586', 'MOG053093', '06:23:23', '2021-08-27', 0),
(309, 'TP08', 'PRS036059', 'MOG052457', '08:27:31', '2021-08-27', 0),
(310, 'TP08', 'PRS036059', 'MOG052457', '08:31:44', '2021-08-27', 0),
(311, 'TP41', 'PRS036529', 'MOG053209', '09:22:40', '2021-08-27', 0),
(312, 'TP41', 'PRS036529', 'MOG053209', '09:25:42', '2021-08-27', 0),
(313, 'TP41', 'PRS036529', 'MOG053209', '09:54:59', '2021-08-27', 0),
(314, 'TP01', 'PRS036581', 'MOG053289', '12:23:01', '2021-08-27', 0),
(315, 'TP10', 'PRS036537', 'MOG053224', '12:34:11', '2021-08-27', 1),
(316, 'TP10', 'PRS036537', 'MOG053224', '12:37:50', '2021-08-27', 1),
(317, 'TP10', 'PRS036537', 'MOG053224', '12:42:43', '2021-08-27', 1),
(318, 'TP10', 'PRS036537', 'MOG053224', '12:50:11', '2021-08-27', 1),
(319, 'TP10', 'PRS036537', 'MOG053224', '12:53:31', '2021-08-27', 1),
(320, 'TP04', 'PRS036669', 'MOG053432', '08:36:11', '2021-08-30', 0),
(321, 'TH12', 'HBL037736', 'MOG053318', '17:12:36', '2021-08-30', 1),
(322, 'TH12', 'HBL037736', 'MOG053318', '17:12:38', '2021-08-30', 1),
(323, 'TP04', 'PRS036670', 'MOG053433', '18:27:41', '2021-08-30', 0),
(324, 'TP41', 'PRS036548', 'MOG053235', '18:32:27', '2021-08-30', 0),
(325, 'TH07', 'HBL037373', 'MOG052729', '12:37:43', '2021-08-31', 0),
(326, 'TH07', 'HBL037373', 'MOG052729', '13:59:52', '2021-08-31', 0),
(327, 'TH07', 'HBL037373', 'MOG052729', '14:30:32', '2021-08-31', 0),
(328, 'TH07', 'HBL037373', 'MOG052729', '14:30:34', '2021-08-31', 0),
(329, 'TH02', 'HBL037294', 'MOG052605', '07:35:38', '2021-09-02', 0),
(330, 'TH02', 'HBL037294', 'MOG052605', '07:44:00', '2021-09-02', 0),
(331, 'TP09', 'PRS036885', 'MOG053843', '14:05:52', '2021-09-21', 0),
(332, 'TP09', 'PRS036885', 'MOG053843', '14:16:06', '2021-09-21', 0),
(333, 'TP08', 'PRS036958', 'MOG053988', '15:23:50', '2021-09-21', 1),
(334, 'TP08', 'PRS036958', 'MOG053988', '15:53:10', '2021-09-21', 1),
(335, 'TP08', 'PRS036958', 'MOG053988', '16:04:06', '2021-09-21', 1),
(336, 'TP03', 'PRS036898', 'MOG053873', '16:15:25', '2021-09-21', 0),
(337, 'TP03', 'PRS036898', 'MOG053873', '16:23:07', '2021-09-21', 0),
(338, 'TP01', 'PRS036689', 'MOG053546', '13:41:34', '2021-09-22', 0),
(339, 'TP01', 'PRS036689', 'MOG053546', '14:21:39', '2021-09-22', 0),
(340, 'TP04', 'PRS036865', 'MOG053811', '14:34:36', '2021-09-22', 0),
(341, 'TP04', 'PRS036865', 'MOG053811', '15:13:04', '2021-09-22', 0),
(342, 'TP09', 'PRS036980', 'MOG054042', '14:19:57', '2021-09-23', 0),
(343, 'TP09', 'PRS036578', 'MOG053286', '14:27:15', '2021-09-23', 1),
(344, 'TP05', 'PRS036848', 'MOG053786', '09:37:10', '2021-09-28', 1),
(345, 'TG01', 'PLT014591', 'MOG053585', '10:58:32', '2021-09-28', 1),
(346, 'TG01', 'PLT014591', 'MOG053585', '11:43:48', '2021-09-28', 1),
(347, 'TG01', 'PLT014591', 'MOG053585', '11:44:03', '2021-09-28', 1),
(348, 'TG01', 'PLT014591', 'MOG053585', '11:44:07', '2021-09-28', 1),
(349, 'TG01', 'PLT014591', 'MOG053585', '11:44:28', '2021-09-28', 1),
(350, 'TH42', 'HBL037885', 'MOG053620', '11:47:42', '2021-09-28', 1),
(351, 'TH42', 'HBL037885', 'MOG053620', '11:47:50', '2021-09-28', 1),
(352, 'TH12', 'HBL037911', 'MOG053677', '12:35:41', '2021-09-28', 1),
(353, 'TH12', 'HBL037911', 'MOG053677', '12:38:40', '2021-09-28', 1),
(354, 'TH12', 'HBL037911', 'MOG053677', '12:39:06', '2021-09-28', 1),
(355, 'TH12', 'HBL037911', 'MOG053677', '13:22:52', '2021-09-28', 1),
(356, 'TH12', 'HBL037911', 'MOG053677', '15:14:45', '2021-09-28', 1),
(357, 'TH17', 'HBL037762', 'MOG053361', '10:57:50', '2021-09-29', 0),
(358, 'TH17', 'HBL037762', 'MOG053361', '11:37:33', '2021-09-29', 0),
(359, 'TH17', 'HBL038344', 'MOG054432', '12:05:55', '2021-10-12', 1),
(360, 'TH17', 'HBL038344', 'MOG054432', '12:16:52', '2021-10-12', 1),
(361, 'TH17', 'HBL038344', 'MOG054432', '12:21:34', '2021-10-12', 1),
(362, 'th10', 'HBL014525', 'MOG017560', '10:49:01', '2022-05-19', 1),
(363, 'TH10', 'HBL005121', 'MOG005849', '12:25:20', '2022-05-19', 1),
(364, 'TH10', 'HBL015021', 'MOG018128', '11:53:18', '2022-05-20', 1),
(365, 'TH10', 'HBL014025', 'MOG017017', '07:15:02', '2022-05-23', 1),
(366, 'TH10', 'HBL022534', 'MOG029739', '13:27:15', '2022-05-23', 1),
(367, 'TH10', 'HBL011526', 'MOG014121', '12:31:09', '2022-05-24', 1),
(368, 'TG01', 'PLT010206', 'MOG036890', '16:21:31', '2022-05-24', 1),
(369, 'TG01', 'PLT010315', 'MOG037275', '16:24:17', '2022-05-24', 1),
(370, 'TG01', 'PLT010315', 'MOG037275', '16:26:00', '2022-05-24', 1),
(371, 'TH10', 'HBL015126', 'MOG018247', '09:24:01', '2022-05-25', 1),
(372, 'TH10', 'HBL010626', 'MOG013020', '11:29:44', '2022-05-25', 1),
(373, 'TH10', 'HBL010505', 'MOG012868', '12:23:33', '2022-05-25', 1),
(374, 'TH10', 'HBL002626', 'MOG002919', '13:47:20', '2022-05-25', 1),
(375, 'TH10', 'HBL002626', 'MOG002919', '13:52:15', '2022-05-25', 1),
(376, 'TH10', 'HBL002526', 'MOG002812', '13:52:36', '2022-05-25', 1),
(377, 'TH10', 'HBL009911', 'MOG012078', '09:41:43', '2022-05-26', 1),
(378, 'TH10', 'HBL001425', 'MOG001569', '09:52:47', '2022-05-26', 1),
(379, 'TH10', 'HBL001425', 'MOG001569', '10:13:13', '2022-05-26', 1),
(380, 'TH10', 'HBL011424', 'MOG014006', '10:18:33', '2022-05-26', 1),
(381, 'TH10', 'HBL001516', 'MOG001666', '13:30:16', '2022-05-26', 1),
(382, 'TH10', 'HBL016225', 'MOG019618', '08:40:06', '2022-05-30', 1),
(383, 'TH10', 'HBL016588', 'MOG020028', '09:29:39', '2022-05-30', 1),
(384, 'TH10', 'HBL012574', 'MOG015371', '09:46:16', '2022-05-30', 1),
(385, 'TH10', 'HBL014242', 'MOG017274', '10:27:48', '2022-05-30', 1),
(386, 'TH10', 'HBL010404', 'MOG012736', '11:39:46', '2022-05-30', 1),
(387, 'TH17', 'HBL011242', 'MOG013806', '13:57:54', '2022-05-30', 1),
(388, 'TH10', 'HBL021022', 'MOG027109', '14:10:24', '2022-05-30', 1),
(389, 'TH10', 'HBL024425', 'MOG032648', '14:20:21', '2022-05-30', 1),
(390, 'TH10', 'HBL012575', 'MOG015372', '16:35:12', '2022-05-30', 1),
(391, 'TB05', 'BHL010089', 'MOG059078', '08:10:30', '2022-06-14', 1),
(392, 'TH10', 'HBL010225', 'MOG012518', '10:56:03', '2022-06-14', 1),
(393, 'TP01', 'PRS048524', 'MOG073253', '11:00:03', '2024-01-10', 1),
(394, 'TP01', 'PRS048524', 'MOG073253', '11:34:32', '2024-01-10', 1),
(395, 'TP01', 'PRS048524', 'MOG073253', '11:34:34', '2024-01-10', 1),
(396, 'TP01', 'PRS048524', 'MOG073253', '11:34:37', '2024-01-10', 1),
(397, 'TP01', 'PRS048524', 'MOG073253', '11:34:37', '2024-01-10', 1),
(398, 'TP01', 'PRS048524', 'MOG073253', '11:34:38', '2024-01-10', 1),
(399, 'TP01', 'PRS048524', 'MOG073253', '11:34:38', '2024-01-10', 1),
(400, 'TP01', 'PRS048524', 'MOG073253', '11:34:38', '2024-01-10', 1),
(401, 'TP01', 'PRS048524', 'MOG073253', '11:34:43', '2024-01-10', 1),
(402, 'TP01', 'PRS048524', 'MOG073253', '11:34:43', '2024-01-10', 1),
(403, 'TP01', 'PRS048524', 'MOG073253', '11:34:46', '2024-01-10', 1),
(404, 'TP01', 'PRS048524', 'MOG073253', '11:34:46', '2024-01-10', 1),
(405, 'TP01', 'PRS048524', 'MOG073253', '11:34:46', '2024-01-10', 1),
(406, 'TP01', 'PRS048525', 'MOG073254', '11:35:07', '2024-01-10', 1),
(407, 'TH17', 'HBL050114', 'MOG073254', '11:48:31', '2024-01-10', 1),
(408, 'TP11', 'PRS048525', 'MOG073254', '12:20:28', '2024-01-17', 1),
(409, 'TP11', 'PRS048525', 'MOG073254', '12:20:47', '2024-01-17', 1),
(410, 'TP11', 'PRS036448', 'MOG053076', '12:22:48', '2024-01-17', 1),
(411, 'TP11', 'PRS036448', 'MOG053076', '12:27:07', '2024-01-17', 1),
(412, 'TP11', 'PRS036448', 'MOG053076', '14:27:07', '2024-01-17', 1),
(413, 'TP11', 'PRS036448', 'MOG053076', '14:41:34', '2024-01-17', 1),
(414, 'TP11', 'PRS036448', 'MOG053076', '14:58:20', '2024-01-17', 1),
(415, 'TP11', 'PRS048525', 'MOG073254', '14:36:11', '2024-02-12', 1),
(416, 'TP11', 'PRS048525', 'MOG073254', '14:36:29', '2024-02-12', 1),
(417, 'TP11', 'PRS048525', 'MOG073254', '14:38:29', '2024-02-12', 1),
(418, 'TP11', 'PRS048525', 'MOG073254', '14:39:59', '2024-02-12', 1),
(419, 'TP11', 'PRS048525', 'MOG073254', '14:42:51', '2024-02-12', 1),
(420, 'TP08', 'PRS054689', 'MOG083559', '11:51:38', '2025-02-20', 1),
(421, 'TP08', 'MOG083559', 'MOG083559', '07:20:06', '2025-02-21', 1),
(422, 'TP05', 'MOG083559', 'MOG083559', '07:21:23', '2025-02-21', 1),
(423, 'TP09', 'MOG083559', 'MOG083559', '07:56:22', '2025-02-21', 1),
(424, 'TP08', 'MOG083559', 'MOG083559', '08:40:30', '2025-02-21', 1),
(425, 'TP08', 'MOG083559', 'MOG083559', '08:42:17', '2025-02-21', 1),
(426, 'TP08', 'MOG083559', 'MOG083559', '08:43:43', '2025-02-21', 1),
(427, 'TP05', 'MOG083559', 'MOG083559', '08:52:02', '2025-02-21', 1),
(428, 'TP08', 'MOG083559', 'MOG083559', '09:46:01', '2025-02-21', 1),
(429, 'TP08', 'MOG083559', 'MOG083559', '09:51:00', '2025-02-21', 1),
(430, 'TP05', 'MOG083559', 'MOG083559', '10:00:05', '2025-02-21', 1),
(431, 'TP05', 'MOG083559', 'MOG083559', '10:01:25', '2025-02-21', 1),
(432, 'TP05', 'MOG083559', 'MOG083559', '10:40:40', '2025-02-21', 1),
(433, 'TP05', 'MOG083559', 'MOG083559', '10:45:08', '2025-02-21', 1),
(434, 'TP05', 'MOG083559', 'MOG083559', '10:54:56', '2025-02-21', 1),
(435, 'TP05', 'MOG083559', 'MOG083559', '14:16:12', '2025-02-21', 1),
(436, 'TP05', 'MOG083559', 'MOG083559', '14:17:11', '2025-02-21', 1),
(437, 'TP08', 'MOG083559', 'MOG083559', '14:22:14', '2025-02-21', 1),
(438, 'TP05', 'MOG083559', 'MOG083559', '14:24:01', '2025-02-21', 1),
(439, 'TP05', 'MOG083559', 'MOG083559', '14:26:39', '2025-02-21', 1),
(440, 'TP08', 'MOG083559', 'MOG083559', '07:09:48', '2025-02-24', 1),
(441, 'tp08', 'MOG083559', 'MOG083559', '08:42:19', '2025-02-24', 1),
(442, 'tp08', 'MOG083559', 'MOG083559', '08:46:06', '2025-02-24', 1),
(443, 'tp08', 'MOG083559', 'MOG083559', '08:47:35', '2025-02-24', 1),
(444, 'TP08', 'MOG083559', 'MOG083559', '08:50:03', '2025-02-24', 1),
(445, 'tp08', 'MOG083559', 'MOG083559', '08:52:12', '2025-02-24', 1),
(446, 'tp08', 'MOG083559', 'MOG083559', '09:03:20', '2025-02-24', 1),
(447, 'TP08', 'MOG083559', 'MOG083559', '09:11:58', '2025-02-24', 1),
(448, 'TP08', 'MOG083559', 'MOG083559', '10:03:42', '2025-02-24', 1),
(451, 'TH17', 'HBL056746', 'MOG083559', '10:24:21', '2025-02-24', 1),
(452, 'TH17', 'HBL056746', 'MOG083559', '10:38:28', '2025-02-24', 1),
(453, 'TP08', 'MOG083559', 'MOG083559', '10:42:28', '2025-02-24', 1),
(454, 'TH17', 'HBL056746', 'MOG083559', '10:43:56', '2025-02-24', 1),
(455, 'tp08', 'MOG083559', 'MOG083559', '10:52:43', '2025-02-24', 1),
(456, 'TP05', 'MOG083559', 'MOG083559', '10:54:01', '2025-02-24', 1),
(457, 'TP08', 'MOG083559', 'MOG083559', '10:57:01', '2025-02-24', 1),
(458, 'TH17', 'HBL056746', 'MOG083559', '11:06:23', '2025-02-24', 1),
(459, 'TH17', 'HBL056746', 'MOG083559', '11:17:13', '2025-02-24', 1),
(460, 'TH17', 'HBL056746', 'MOG083559', '11:25:55', '2025-02-24', 1),
(461, 'TH17', 'HBL056746', 'MOG083559', '11:49:14', '2025-02-24', 1),
(462, 'TP08', 'MOG083559', 'MOG083559', '11:57:06', '2025-02-24', 1),
(463, 'TP08', 'MOG083559', 'MOG083559', '11:59:19', '2025-02-24', 1),
(464, 'TP08', 'MOG083559', 'MOG083559', '11:59:20', '2025-02-24', 1),
(465, 'TP08', 'MOG083559', 'MOG083559', '11:59:20', '2025-02-24', 1),
(466, 'TP05', 'MOG083559', 'MOG083559', '12:01:54', '2025-02-24', 1),
(467, 'TH17', 'HBL056746', 'MOG083559', '12:03:32', '2025-02-24', 1),
(468, 'TP05', 'MOG083559', 'MOG083559', '12:03:37', '2025-02-24', 1),
(469, 'TP05', 'MOG083559', 'MOG083559', '12:04:47', '2025-02-24', 1),
(470, 'TH17', 'HBL056746', 'MOG083559', '12:06:36', '2025-02-24', 1),
(471, 'TH17', 'HBL056746', 'MOG083559', '12:09:53', '2025-02-24', 1),
(472, 'TH17', 'HBL056746', 'MOG083559', '12:10:38', '2025-02-24', 1),
(473, 'TH17', 'HBL056746', 'MOG083559', '12:11:43', '2025-02-24', 1),
(474, 'TP08', 'MOG083559', 'MOG083559', '12:16:29', '2025-02-24', 1),
(475, 'tp08', 'MOG083559', 'MOG083559', '12:18:48', '2025-02-24', 1),
(476, 'TP08', 'MOG083559', 'MOG083559', '12:20:07', '2025-02-24', 1),
(477, 'TP08', 'MOG083559', 'MOG083559', '12:21:24', '2025-02-24', 1),
(478, 'TH17', 'HBL056746', 'MOG083559', '12:29:13', '2025-02-24', 1),
(479, 'TH17', 'HBL056746', 'MOG083559', '12:30:08', '2025-02-24', 1),
(480, 'TH17', 'HBL056746', 'MOG083559', '12:33:07', '2025-02-24', 1),
(481, 'TH17', 'HBL056746', 'MOG083559', '12:34:16', '2025-02-24', 1),
(482, 'TH17', 'HBL056746', 'MOG083559', '12:35:07', '2025-02-24', 1),
(483, 'TH17', 'HBL056746', 'MOG083559', '12:38:04', '2025-02-24', 1),
(484, 'TH17', 'HBL056746', 'MOG083559', '12:38:49', '2025-02-24', 1),
(485, 'TH17', 'HBL056746', 'MOG083559', '13:47:20', '2025-02-24', 1),
(486, 'TP08', 'MOG083559', 'MOG083559', '14:03:56', '2025-02-24', 1),
(487, 'TP08', 'MOG083558', 'MOG083558', '14:04:31', '2025-02-24', 1),
(488, 'TP08', 'MOG083559', 'MOG083559', '14:05:01', '2025-02-24', 1),
(489, 'TP08', 'MOG083558', 'MOG083558', '14:05:03', '2025-02-24', 1),
(490, 'TP08', 'MOG083559', 'MOG083559', '14:05:52', '2025-02-24', 1),
(491, 'TH17', 'HBL056746', 'MOG083559', '14:23:46', '2025-02-24', 1),
(492, 'TH17', 'HBL056745', 'MOG083558', '14:23:53', '2025-02-24', 1),
(493, 'TP08', 'MOG083559', 'MOG083559', '07:14:51', '2025-02-25', 1),
(494, 'TP05', 'MOG083559', 'MOG083559', '07:20:45', '2025-02-25', 1),
(495, 'TH17', 'HBL056746', 'MOG083559', '07:33:04', '2025-02-25', 1),
(496, 'TH17', 'HBL056745', 'MOG083558', '07:33:07', '2025-02-25', 1),
(497, 'TH17', 'HBL056743', 'MOG083551', '07:33:09', '2025-02-25', 1),
(498, 'TH17', 'HBL056741', 'MOG083549', '07:33:11', '2025-02-25', 1),
(499, 'TH17', 'HBL056740', 'MOG083548', '07:33:13', '2025-02-25', 1),
(500, 'TH17', 'HBL056723', 'MOG083518', '07:33:14', '2025-02-25', 1),
(501, 'TP08', 'MOG083559', 'MOG083559', '07:34:49', '2025-02-25', 1),
(502, 'TP08', 'MOG083559', 'MOG083559', '08:30:44', '2025-02-25', 1),
(503, 'TP05', 'MOG083559', 'MOG083559', '08:48:30', '2025-02-25', 1),
(504, 'TH17', 'HBL056746', 'MOG083559', '08:59:03', '2025-02-25', 1),
(505, 'TP08', 'MOG083559', 'MOG083559', '07:23:55', '2025-02-26', 1),
(506, 'TP08', 'MOG083558', 'MOG083558', '07:36:00', '2025-02-26', 1),
(507, 'TP08', 'MOG083559', 'MOG083559', '07:36:07', '2025-02-26', 1),
(508, 'TH17', 'HBL056746', 'MOG083559', '08:10:16', '2025-02-26', 1),
(509, 'TH17', 'HBL056743', 'MOG083551', '08:10:19', '2025-02-26', 1),
(510, 'TH17', 'HBL056748', 'MOG083561', '08:10:20', '2025-02-26', 1),
(511, 'TH17', 'HBL056741', 'MOG083549', '08:10:21', '2025-02-26', 1),
(512, 'TH17', 'HBL056746', 'MOG083559', '09:15:55', '2025-02-26', 1),
(513, 'TH17', 'HBL056746', 'MOG083559', '11:08:39', '2025-02-26', 1),
(514, 'TH17', 'HBL056742', 'MOG083550', '11:08:41', '2025-02-26', 1),
(515, 'TH17', 'HBL056742', 'MOG083550', '11:08:55', '2025-02-26', 1),
(516, 'TP08', 'MOG083559', 'MOG083559', '07:24:19', '2025-02-27', 1),
(517, 'tp08', 'MOG083559', 'MOG083559', '07:27:05', '2025-02-27', 1),
(518, 'TP08', 'MOG083559', 'MOG083559', '07:32:46', '2025-02-27', 1),
(519, 'tp08', 'MOG083559', 'MOG083559', '07:34:35', '2025-02-27', 1),
(520, 'TP08', 'MOG083559', 'MOG083559', '07:43:36', '2025-02-27', 1),
(521, 'TP08', 'MOG083559', 'MOG083559', '07:44:45', '2025-02-27', 1),
(522, 'TP05', 'MOG083559', 'MOG083559', '07:46:28', '2025-02-27', 1),
(523, 'TP05', 'MOG083559', 'MOG083559', '07:47:43', '2025-02-27', 1),
(524, 'TP08', 'MOG083559', 'MOG083559', '07:48:50', '2025-02-27', 1),
(525, 'TP08', 'MOG083559', 'MOG083559', '08:00:20', '2025-02-27', 1),
(526, 'TP08', 'MOG083559', 'MOG083559', '08:02:37', '2025-02-27', 1),
(527, 'TP08', 'MOG083559', 'MOG083559', '08:04:04', '2025-02-27', 1),
(528, 'TP05', 'MOG083559', 'MOG083559', '08:16:30', '2025-02-27', 1),
(529, 'tp08', 'MOG083559', 'MOG083559', '08:31:27', '2025-02-27', 1),
(530, 'TP08', 'MOG083559', 'MOG083559', '08:33:01', '2025-02-27', 1),
(531, 'TP08', 'MOG083559', 'MOG083559', '08:33:30', '2025-02-27', 1),
(532, 'tp08', 'MOG083559', 'MOG083559', '08:35:00', '2025-02-27', 1),
(533, 'tp08', 'MOG083559', 'MOG083559', '08:37:13', '2025-02-27', 1),
(534, 'tp08', 'MOG083559', 'MOG083559', '08:38:42', '2025-02-27', 1),
(535, 'TP08', 'MOG083559', 'MOG083559', '08:40:22', '2025-02-27', 1),
(536, 'tp08', 'MOG083559', 'MOG083559', '08:44:58', '2025-02-27', 1),
(537, 'tp08', 'MOG083559', 'MOG083559', '08:50:23', '2025-02-27', 1),
(538, 'TP08', 'MOG083559', 'MOG083559', '09:15:03', '2025-02-27', 1),
(539, 'TH16', 'HBL056746', 'MOG083559', '10:25:18', '2025-02-27', 1),
(540, 'TP08', 'MOG083559', 'MOG083559', '10:52:07', '2025-02-27', 1),
(541, 'TH17', 'HBL056746', 'MOG083559', '11:06:36', '2025-02-27', 1),
(542, 'TH17', 'HBL056746', 'MOG083559', '11:17:27', '2025-02-27', 1),
(543, 'TP08', 'MOG083559', 'MOG083559', '11:41:09', '2025-02-27', 1),
(544, 'TH17', 'HBL056746', 'MOG083559', '11:42:16', '2025-02-27', 1),
(545, 'th17', 'HBL056746', 'MOG083559', '11:43:32', '2025-02-27', 1),
(546, 'tp08', 'MOG083559', 'MOG083559', '11:44:16', '2025-02-27', 1),
(547, 'TH17', 'HBL056746', 'MOG083559', '11:44:28', '2025-02-27', 1),
(548, 'tp08', 'MOG083559', 'MOG083559', '11:45:56', '2025-02-27', 1),
(549, 'TP08', 'MOG083559', 'MOG083559', '11:47:25', '2025-02-27', 1),
(550, 'TH17', 'HBL056746', 'MOG083559', '11:48:10', '2025-02-27', 1),
(551, 'TH17', 'HBL056746', 'MOG083559', '11:48:53', '2025-02-27', 1),
(552, 'TH17', 'HBL056746', 'MOG083559', '11:49:31', '2025-02-27', 1),
(553, 'tp08', 'MOG083559', 'MOG083559', '11:51:42', '2025-02-27', 1),
(554, 'TH17', 'HBL056746', 'MOG083559', '11:53:55', '2025-02-27', 1),
(555, 'TP08', 'MOG083559', 'MOG083559', '11:54:31', '2025-02-27', 1),
(556, 'TP05', 'MOG083559', 'MOG083559', '11:57:04', '2025-02-27', 1),
(557, 'TH17', 'HBL056746', 'MOG083559', '12:00:11', '2025-02-27', 1),
(558, 'TH17', 'HBL056746', 'MOG083559', '12:02:33', '2025-02-27', 1),
(559, 'TH17', 'HBL056746', 'MOG083559', '12:04:27', '2025-02-27', 1),
(560, 'TH17', 'HBL056746', 'MOG083559', '12:08:24', '2025-02-27', 1),
(561, 'TH17', 'HBL056746', 'MOG083559', '12:15:20', '2025-02-27', 1),
(562, 'TH17', 'HBL056746', 'MOG083559', '12:20:37', '2025-02-27', 1),
(563, 'TH17', 'HBL056746', 'MOG083559', '12:23:20', '2025-02-27', 1),
(564, 'TH17', 'HBL056746', 'MOG083559', '12:43:17', '2025-02-27', 1),
(565, 'TH17', 'HBL056746', 'MOG083559', '12:46:16', '2025-02-27', 1),
(566, 'TH17', 'HBL056746', 'MOG083559', '12:47:25', '2025-02-27', 1),
(567, 'TH17', 'HBL056746', 'MOG083559', '13:40:50', '2025-02-27', 1),
(568, 'TP08', 'MOG083559', 'MOG083559', '13:52:55', '2025-02-27', 1),
(569, 'tp07', 'MOG083559', 'MOG083559', '14:00:38', '2025-02-27', 1),
(570, 'TP08', 'MOG083559', 'MOG083559', '14:10:08', '2025-02-27', 1),
(571, 'TH17', 'HBL056746', 'MOG083559', '14:22:48', '2025-02-27', 1),
(572, 'TH17', 'HBL056746', 'MOG083559', '14:24:02', '2025-02-27', 1),
(573, 'TH17', 'HBL056746', 'MOG083559', '14:27:13', '2025-02-27', 1),
(574, 'TH17', 'HBL056746', 'MOG083559', '06:55:08', '2025-02-28', 1),
(575, 'TH17', 'HBL056746', 'MOG083559', '07:12:01', '2025-02-28', 1),
(576, 'TH17', 'HBL056746', 'MOG083559', '07:14:12', '2025-02-28', 1),
(577, 'TH17', 'HBL056746', 'MOG083559', '07:20:15', '2025-02-28', 1),
(578, 'TH17', 'HBL056746', 'MOG083559', '07:24:38', '2025-02-28', 1),
(579, 'TH17', 'HBL056746', 'MOG083559', '07:30:57', '2025-02-28', 1),
(580, 'TH17', 'HBL056746', 'MOG083559', '07:32:46', '2025-02-28', 1),
(581, 'TP08', 'MOG083559', 'MOG083559', '07:33:15', '2025-02-28', 1),
(582, 'TH17', 'HBL056746', 'MOG083559', '07:35:02', '2025-02-28', 1),
(583, 'TP08', 'MOG083559', 'MOG083559', '07:41:01', '2025-02-28', 1),
(584, 'TH17', 'HBL056746', 'MOG083559', '07:43:56', '2025-02-28', 1),
(585, 'TH17', 'HBL056746', 'MOG083559', '07:49:27', '2025-02-28', 1),
(586, 'TP08', 'MOG083559', 'MOG083559', '07:50:28', '2025-02-28', 1),
(587, 'TH17', 'HBL056746', 'MOG083559', '07:51:33', '2025-02-28', 1),
(588, 'TH17', 'HBL056746', 'MOG083559', '07:52:30', '2025-02-28', 1),
(589, 'TH17', 'HBL056746', 'MOG083559', '07:53:20', '2025-02-28', 1),
(590, 'TH17', 'HBL056746', 'MOG083559', '07:55:24', '2025-02-28', 1),
(591, 'TP08', 'MOG083559', 'MOG083559', '07:57:18', '2025-02-28', 1),
(592, 'TH17', 'HBL056746', 'MOG083559', '07:57:58', '2025-02-28', 1),
(593, 'TP08', 'MOG083559', 'MOG083559', '07:59:29', '2025-02-28', 1),
(594, 'TH17', 'HBL056746', 'MOG083559', '08:00:03', '2025-02-28', 1),
(595, 'TP08', 'MOG083559', 'MOG083559', '08:01:31', '2025-02-28', 1),
(596, 'TP08', 'MOG083559', 'MOG083559', '08:13:14', '2025-02-28', 1),
(597, 'TH17', 'HBL056746', 'MOG083559', '08:16:27', '2025-02-28', 1),
(598, 'TP08', 'MOG083559', 'MOG083559', '08:17:18', '2025-02-28', 1),
(599, 'TH17', 'HBL056746', 'MOG083559', '08:22:22', '2025-02-28', 1),
(600, 'TP08', 'MOG083559', 'MOG083559', '08:22:50', '2025-02-28', 1),
(601, 'TP08', 'MOG083559', 'MOG083559', '08:28:10', '2025-02-28', 1),
(602, 'TP08', 'MOG083559', 'MOG083559', '08:31:00', '2025-02-28', 1),
(603, 'TH17', 'HBL056746', 'MOG083559', '08:34:28', '2025-02-28', 1),
(604, 'TH17', 'HBL056746', 'MOG083559', '08:36:06', '2025-02-28', 1),
(605, 'TH17', 'HBL056746', 'MOG083559', '08:54:11', '2025-02-28', 1),
(606, 'TH17', 'HBL056746', 'MOG083559', '08:57:06', '2025-02-28', 1),
(607, 'TP08', 'MOG083559', 'MOG083559', '09:11:36', '2025-02-28', 1),
(608, 'TP05', 'MOG083559', 'MOG083559', '09:14:17', '2025-02-28', 1),
(609, 'TP08', 'MOG083559', 'MOG083559', '10:14:20', '2025-02-28', 1),
(610, 'TP08', 'MOG083559', 'MOG083559', '10:31:03', '2025-02-28', 1),
(611, 'TP08', 'MOG083559', 'MOG083559', '10:39:55', '2025-02-28', 1),
(612, 'TP08', 'MOG083559', 'MOG083559', '10:41:35', '2025-02-28', 1),
(613, 'TP08', 'MOG083559', 'MOG083559', '10:46:33', '2025-02-28', 1),
(614, 'TP08', 'MOG083559', 'MOG083559', '10:53:50', '2025-02-28', 1),
(615, 'TP08', 'MOG083559', 'MOG083559', '10:59:13', '2025-02-28', 1),
(616, 'TP08', 'MOG083559', 'MOG083559', '11:02:19', '2025-02-28', 1),
(617, 'TP08', 'MOG083559', 'MOG083559', '11:07:37', '2025-02-28', 1),
(618, 'TP08', 'MOG083559', 'MOG083559', '11:15:58', '2025-02-28', 1),
(619, 'TP08', 'MOG083559', 'MOG083559', '11:18:12', '2025-02-28', 1),
(620, 'TP08', 'MOG083559', 'MOG083559', '11:32:23', '2025-02-28', 1),
(621, 'TP08', 'MOG083559', 'MOG083559', '12:05:21', '2025-02-28', 1),
(622, 'TP08', 'MOG083559', 'MOG083559', '12:08:48', '2025-02-28', 1),
(623, 'TP05', 'MOG083559', 'MOG083559', '12:10:32', '2025-02-28', 1),
(624, 'TP08', 'MOG083559', 'MOG083559', '14:25:51', '2025-02-28', 1),
(625, 'TP08', 'MOG083559', 'MOG083559', '07:15:25', '2025-03-03', 1),
(626, 'TP08', 'MOG083559', 'MOG083559', '07:33:09', '2025-03-03', 1),
(627, 'TH17', 'HBL056746', 'MOG083559', '08:16:52', '2025-03-03', 1),
(628, 'TH17', 'HBL056746', 'MOG083559', '08:22:57', '2025-03-03', 1),
(629, 'TH17', 'HBL056746', 'MOG083559', '08:25:32', '2025-03-03', 1),
(630, 'TH17', 'HBL056746', 'MOG083559', '08:27:34', '2025-03-03', 1),
(631, 'TH17', 'HBL056746', 'MOG083559', '08:29:59', '2025-03-03', 1),
(632, 'TH17', 'HBL056746', 'MOG083559', '09:01:01', '2025-03-03', 1),
(633, 'TH17', 'HBL056746', 'MOG083559', '09:11:09', '2025-03-03', 1),
(634, 'TH17', 'HBL056746', 'MOG083559', '09:11:09', '2025-03-03', 1),
(635, 'TH17', 'HBL056746', 'MOG083559', '09:13:11', '2025-03-03', 1),
(636, 'TH17', 'HBL056746', 'MOG083559', '09:15:45', '2025-03-03', 1),
(637, 'TP08', 'MOG083559', 'MOG083559', '09:23:13', '2025-03-03', 1),
(638, 'TP08', 'MOG083559', 'MOG083559', '10:02:32', '2025-03-03', 1),
(639, 'TP08', 'MOG083559', 'MOG083559', '10:07:21', '2025-03-03', 1),
(640, 'TP08', 'MOG083559', 'MOG083559', '10:08:29', '2025-03-03', 1),
(641, 'TP08', 'MOG083559', 'MOG083559', '10:16:10', '2025-03-03', 1),
(642, 'TP08', 'MOG083559', 'MOG083559', '10:20:30', '2025-03-03', 1),
(643, 'TP08', 'MOG083559', 'MOG083559', '10:35:48', '2025-03-03', 1),
(644, 'TP08', 'MOG083559', 'MOG083559', '10:36:57', '2025-03-03', 1),
(645, 'TP08', 'MOG083559', 'MOG083559', '10:39:03', '2025-03-03', 1),
(646, 'TP08', 'MOG083559', 'MOG083559', '10:48:54', '2025-03-03', 1),
(647, 'TP08', 'MOG083559', 'MOG083559', '10:58:34', '2025-03-03', 1),
(648, 'TP08', 'MOG083559', 'MOG083559', '11:19:14', '2025-03-03', 1),
(649, 'TH17', 'HBL056746', 'MOG083559', '11:20:39', '2025-03-03', 1),
(650, 'TH17', 'HBL056746', 'MOG083559', '11:23:02', '2025-03-03', 1),
(651, 'TP08', 'MOG083559', 'MOG083559', '11:25:48', '2025-03-03', 1),
(652, 'TH17', 'HBL056746', 'MOG083559', '11:30:18', '2025-03-03', 1),
(653, 'TP08', 'MOG083559', 'MOG083559', '11:39:21', '2025-03-03', 1),
(654, 'TH17', 'HBL056746', 'MOG083559', '11:44:07', '2025-03-03', 1),
(655, 'TH17', 'HBL056746', 'MOG083559', '11:45:26', '2025-03-03', 1),
(656, 'TH17', 'HBL056746', 'MOG083559', '11:47:05', '2025-03-03', 1),
(657, 'TH17', 'HBL056746', 'MOG083559', '11:49:27', '2025-03-03', 1),
(658, 'TP08', 'MOG083559', 'MOG083559', '11:51:07', '2025-03-03', 1),
(659, 'TH17', 'HBL056746', 'MOG083559', '11:52:26', '2025-03-03', 1),
(660, 'TP08', 'MOG083559', 'MOG083559', '11:56:24', '2025-03-03', 1),
(661, 'TH17', 'HBL056746', 'MOG083559', '11:56:32', '2025-03-03', 1),
(662, 'TP08', 'MOG083559', 'MOG083559', '12:01:18', '2025-03-03', 1),
(663, 'TH17', 'HBL056746', 'MOG083559', '12:01:33', '2025-03-03', 1),
(664, 'TH17', 'HBL056746', 'MOG083559', '12:03:59', '2025-03-03', 1),
(665, 'TH17', 'HBL056746', 'MOG083559', '12:06:23', '2025-03-03', 1),
(666, 'TH17', 'HBL056746', 'MOG083559', '12:11:01', '2025-03-03', 1),
(667, 'TP08', 'MOG083559', 'MOG083559', '12:14:27', '2025-03-03', 1),
(668, 'TP08', 'MOG083559', 'MOG083559', '12:16:55', '2025-03-03', 1),
(669, 'TH17', 'HBL056746', 'MOG083559', '12:22:46', '2025-03-03', 1),
(670, 'TP08', 'MOG083559', 'MOG083559', '12:23:20', '2025-03-03', 1),
(671, 'TP08', 'MOG083559', 'MOG083559', '12:25:31', '2025-03-03', 1),
(672, 'TP08', 'MOG083559', 'MOG083559', '12:28:49', '2025-03-03', 1),
(673, 'TP08', 'MOG083559', 'MOG083559', '12:30:12', '2025-03-03', 1),
(674, 'TH17', 'HBL056746', 'MOG083559', '13:38:19', '2025-03-03', 1),
(675, 'TH17', 'HBL056746', 'MOG083559', '13:41:16', '2025-03-03', 1),
(676, 'TH17', 'HBL056746', 'MOG083559', '13:43:15', '2025-03-03', 1),
(677, 'TH17', 'HBL056746', 'MOG083559', '13:44:43', '2025-03-03', 1),
(678, 'TH17', 'HBL056746', 'MOG083559', '13:48:43', '2025-03-03', 1),
(679, 'TH17', 'HBL056746', 'MOG083559', '13:48:44', '2025-03-03', 1),
(680, 'TH17', 'HBL056746', 'MOG083559', '14:16:27', '2025-03-03', 1),
(681, 'TH17', 'HBL056746', 'MOG083559', '14:17:59', '2025-03-03', 1),
(682, 'TH17', 'HBL056746', 'MOG083559', '14:20:59', '2025-03-03', 1),
(683, 'TH17', 'HBL056746', 'MOG083559', '07:14:51', '2025-03-04', 1),
(684, 'TH17', 'HBL056746', 'MOG083559', '07:50:08', '2025-03-04', 1),
(685, 'TH17', 'HBL056746', 'MOG083559', '07:50:08', '2025-03-04', 1),
(686, 'TH17', 'HBL056746', 'MOG083559', '08:00:40', '2025-03-04', 1),
(687, 'TH17', 'HBL056746', 'MOG083559', '08:05:28', '2025-03-04', 1),
(688, 'TH17', 'HBL056746', 'MOG083559', '08:15:24', '2025-03-04', 1),
(689, 'TH17', 'HBL056746', 'MOG083559', '08:17:27', '2025-03-04', 1),
(690, 'TH17', 'HBL056746', 'MOG083559', '08:21:39', '2025-03-04', 1),
(691, 'TH17', 'HBL056746', 'MOG083559', '08:22:33', '2025-03-04', 1),
(692, 'TH17', 'HBL056746', 'MOG083559', '08:29:49', '2025-03-04', 1),
(693, 'TH17', 'HBL056746', 'MOG083559', '08:31:15', '2025-03-04', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das`
--

CREATE TABLE `das` (
  `id_das` int(10) NOT NULL,
  `linea` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `empleado_id_empleado` int(10) UNSIGNED NOT NULL,
  `id_keeper` int(10) UNSIGNED NOT NULL,
  `id_inspector` int(10) UNSIGNED DEFAULT NULL,
  `fecha` date NOT NULL,
  `activaSupervisor` tinyint(1) NOT NULL,
  `activaOperador` tinyint(1) NOT NULL,
  `proceso` varchar(20) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `das`
--

INSERT INTO `das` (`id_das`, `linea`, `empleado_id_empleado`, `id_keeper`, `id_inspector`, `fecha`, `activaSupervisor`, `activaOperador`, `proceso`) VALUES
(1, 'TP08', 737, 737, 737, '2020-10-30', 1, 1, 'PRENSA'),
(2, 'TP08', 737, 737, 737, '2020-10-30', 1, 0, 'PRENSA'),
(3, 'TP08', 758, 758, 758, '2020-10-30', 1, 1, 'PRENSA'),
(4, 'TP08', 758, 758, 758, '2020-10-30', 1, 0, 'PRENSA'),
(5, 'TP08', 758, 758, 758, '2020-11-02', 1, 1, 'PRENSA'),
(6, 'TP08', 745, 745, 745, '2020-11-02', 1, 1, 'PRENSA'),
(7, 'TP08', 745, 745, 745, '2020-11-02', 1, 1, 'PRENSA'),
(8, 'TP08', 758, 758, 758, '2020-11-02', 1, 1, 'PRENSA'),
(9, 'TP08', 745, 745, 745, '2020-11-02', 1, 1, 'PRENSA'),
(10, 'TP08', 745, 745, 745, '2020-11-02', 1, 1, 'PRENSA'),
(11, 'TP01', 600, 600, 600, '2020-11-02', 1, 1, 'PRENSA'),
(12, 'TP03', 753, 753, 753, '2020-11-03', 1, 1, 'PRENSA'),
(13, 'TP03', 753, 753, 753, '2020-11-03', 1, 1, 'PRENSA'),
(14, 'TP03', 753, 753, 753, '2020-11-03', 1, 1, 'PRENSA'),
(15, 'TP03', 753, 753, 753, '2020-11-03', 1, 0, 'PRENSA'),
(16, 'TP03', 756, 756, 756, '2020-11-03', 1, 1, 'PRENSA'),
(17, 'TP03', 756, 756, 756, '2020-11-03', 1, 1, 'PRENSA'),
(18, 'TP03', 756, 756, 756, '2020-11-03', 1, 1, 'PRENSA'),
(19, 'TP01', 745, 745, 745, '2020-11-04', 1, 1, 'PRENSA'),
(20, 'TP01', 145, 145, 145, '2020-11-04', 1, 1, 'PRENSA'),
(21, 'TP01', 145, 145, 145, '2020-11-04', 1, 0, 'PRENSA'),
(22, 'TP01', 745, 745, 745, '2020-11-04', 1, 1, 'PRENSA'),
(23, 'TP01', 145, 145, 145, '2020-11-04', 1, 1, 'PRENSA'),
(24, 'TP01', 145, 145, 145, '2020-11-04', 1, 1, 'PRENSA'),
(25, 'TP03', 742, 742, 742, '2020-11-04', 1, 1, 'PRENSA'),
(26, 'TP03', 742, 742, 742, '2020-11-04', 1, 1, 'PRENSA'),
(27, 'TP05', 746, 746, 746, '2020-11-05', 1, 1, 'PRENSA'),
(28, 'TP05', 746, 746, 746, '2020-11-05', 1, 1, 'PRENSA'),
(29, 'TP05', 149, 149, 149, '2020-11-05', 1, 1, 'PRENSA'),
(30, 'TP05', 149, 149, 149, '2020-11-05', 1, 1, 'PRENSA'),
(31, 'TP01', 745, 745, 745, '2020-11-06', 1, 1, 'PRENSA'),
(32, 'TP01', 745, 745, 745, '2020-11-06', 1, 1, 'PRENSA'),
(33, 'TP01', 145, 145, 145, '2020-11-06', 1, 1, 'PRENSA'),
(34, 'TP01', 145, 145, 145, '2020-11-06', 1, 1, 'PRENSA'),
(35, 'TP05', 149, 149, 149, '2020-11-10', 1, 1, 'PRENSA'),
(36, 'TP05', 149, 149, 149, '2020-11-10', 1, 1, 'PRENSA'),
(37, 'TP05', 739, 739, 739, '2020-11-10', 1, 1, 'PRENSA'),
(38, 'TP05', 739, 739, 739, '2020-11-10', 1, 0, 'PRENSA'),
(39, 'TP01', 145, 145, 145, '2020-11-10', 1, 1, 'PRENSA'),
(40, 'TP01', 145, 145, 145, '2020-11-10', 1, 1, 'PRENSA'),
(41, 'TP03', 174, 174, 174, '2020-11-10', 1, 1, 'PRENSA'),
(42, 'TP03', 174, 174, 174, '2020-11-10', 1, 1, 'PRENSA'),
(43, 'TP04', 737, 737, 737, '2020-11-11', 1, 1, 'PRENSA'),
(44, 'TP04', 737, 737, 737, '2020-11-11', 1, 1, 'PRENSA'),
(45, 'TP05', 749, 749, 749, '2020-11-12', 1, 1, 'PRENSA'),
(46, 'TP05', 749, 749, 749, '2020-11-12', 1, 0, 'PRENSA'),
(47, 'TP07', 734, 734, 734, '2020-11-13', 1, 1, 'PRENSA'),
(48, 'TP07', 734, 734, 734, '2020-11-13', 1, 1, 'PRENSA'),
(49, 'TP07', 740, 740, 740, '2020-11-13', 1, 1, 'PRENSA'),
(50, 'TP07', 740, 740, 740, '2020-11-13', 1, 1, 'PRENSA'),
(51, 'TP10', 752, 752, 752, '2020-11-17', 1, 1, 'PRENSA'),
(52, 'TP10', 752, 752, 752, '2020-11-17', 1, 1, 'PRENSA'),
(53, 'TP10', 746, 746, 746, '2020-11-19', 1, 1, 'PRENSA'),
(54, 'TP01', 145, 145, 145, '2020-11-19', 1, 1, 'PRENSA'),
(55, 'TP10', 746, 746, 746, '2020-11-19', 1, 1, 'PRENSA'),
(56, 'TP01', 145, 145, 145, '2020-11-19', 1, 1, 'PRENSA'),
(57, 'TP01', 149, 149, 149, '2020-11-21', 1, 1, 'PRENSA'),
(58, 'TP01', 149, 149, 149, '2020-11-21', 1, 1, 'PRENSA'),
(59, 'TP41', 148, 148, 148, '2020-11-21', 1, 1, 'PRENSA'),
(60, 'TP41', 148, 148, 148, '2020-11-21', 1, 1, 'PRENSA'),
(61, 'TP01', 145, 145, 145, '2020-11-21', 1, 1, 'PRENSA'),
(62, 'TP01', 145, 145, 145, '2020-11-21', 1, 1, 'PRENSA'),
(63, 'TP41', 739, 739, 739, '2020-11-23', 1, 1, 'PRENSA'),
(64, 'TP41', 739, 739, 739, '2020-11-23', 1, 0, 'PRENSA'),
(65, 'TP41', 148, 148, 148, '2020-11-23', 1, 1, 'PRENSA'),
(66, 'TP41', 148, 148, 148, '2020-11-23', 1, 1, 'PRENSA'),
(67, 'TP03', 753, 753, 753, '2020-11-24', 1, 1, 'PRENSA'),
(68, 'TP03', 753, 753, 753, '2020-11-24', 1, 0, 'PRENSA'),
(69, 'TP03', 756, 756, 756, '2020-11-24', 1, 1, 'PRENSA'),
(70, 'TP03', 756, 756, 756, '2020-11-24', 1, 0, 'PRENSA'),
(71, 'TP10', 169, 169, 169, '2020-11-25', 1, 1, 'PRENSA'),
(72, 'TP10', 169, 169, 169, '2020-11-25', 1, 1, 'PRENSA'),
(73, 'TP02', 170, 170, 170, '2020-11-30', 1, 1, 'PRENSA'),
(74, 'TP02', 170, 170, 170, '2020-11-30', 1, 1, 'PRENSA'),
(75, 'TP02', 148, 148, 148, '2020-11-30', 1, 1, 'PRENSA'),
(76, 'TP02', 148, 148, 148, '2020-11-30', 1, 1, 'PRENSA'),
(77, 'TP09', 755, 755, 755, '2020-12-02', 1, 1, 'PRENSA'),
(78, 'TP09', 755, 755, 755, '2020-12-02', 1, 1, 'PRENSA'),
(79, 'TP09', 747, 747, 747, '2020-12-02', 1, 1, 'PRENSA'),
(80, 'TP09', 747, 747, 747, '2020-12-02', 1, 1, 'PRENSA'),
(81, 'TP04', 745, 745, 745, '2020-12-07', 1, 1, 'PRENSA'),
(82, 'TP04', 745, 745, 745, '2020-12-07', 1, 0, 'PRENSA'),
(83, 'TP04', 748, 748, 748, '2020-12-07', 1, 1, 'PRENSA'),
(84, 'TP04', 748, 748, 748, '2020-12-07', 1, 0, 'PRENSA'),
(85, 'TP01', 170, 170, 170, '2020-12-08', 1, 1, 'PRENSA'),
(86, 'TP41', 752, 752, 752, '2020-12-08', 1, 1, 'PRENSA'),
(87, 'TP41', 752, 752, 752, '2020-12-08', 1, 0, 'PRENSA'),
(88, 'TP41', 738, 738, 738, '2020-12-08', 1, 1, 'PRENSA'),
(89, 'TP41', 738, 738, 738, '2020-12-08', 1, 0, 'PRENSA'),
(90, 'TP09', 755, 755, 755, '2020-12-09', 1, 1, 'PRENSA'),
(91, 'TP09', 755, 755, 755, '2020-12-09', 1, 0, 'PRENSA'),
(92, 'TP09', 747, 747, 747, '2020-12-09', 1, 1, 'PRENSA'),
(93, 'TP09', 747, 747, 747, '2020-12-09', 1, 1, 'PRENSA'),
(94, 'TP09', 755, 755, 755, '2020-12-09', 1, 1, 'PRENSA'),
(95, 'TP09', 755, 755, 755, '2020-12-09', 1, 1, 'PRENSA'),
(96, 'TP05', 739, 739, 739, '2020-12-10', 1, 1, 'PRENSA'),
(97, 'TP05', 739, 739, 739, '2020-12-10', 1, 0, 'PRENSA'),
(98, 'TP05', 739, 739, 739, '2020-12-10', 1, 1, 'PRENSA'),
(99, 'TP05', 739, 739, 739, '2020-12-10', 1, 0, 'PRENSA'),
(100, 'TP02', 740, 740, 740, '2020-12-11', 1, 1, 'PRENSA'),
(101, 'TP02', 740, 740, 740, '2020-12-11', 1, 1, 'PRENSA'),
(102, 'TP02', 740, 740, 740, '2020-12-11', 1, 0, 'PRENSA'),
(103, 'TP02', 740, 740, 740, '2020-12-11', 1, 0, 'PRENSA'),
(104, 'TP04', 738, 738, 738, '2020-12-16', 1, 1, 'PRENSA'),
(105, 'TP04', 738, 738, 738, '2020-12-16', 1, 0, 'PRENSA'),
(106, 'TP41', 740, 740, 740, '2020-12-18', 1, 1, 'PRENSA'),
(107, 'TP41', 740, 740, 740, '2020-12-18', 1, 0, 'PRENSA'),
(108, 'TP05', 746, 746, 746, '2021-01-05', 1, 1, 'PRENSA'),
(109, 'TP05', 746, 746, 746, '2021-01-05', 1, 1, 'PRENSA'),
(110, 'TP10', 757, 757, 757, '2021-01-05', 1, 1, 'PRENSA'),
(111, 'TP10', 757, 757, 757, '2021-01-05', 1, 0, 'PRENSA'),
(112, 'TP05', 738, 738, 738, '2021-01-06', 1, 1, 'PRENSA'),
(113, 'TP05', 738, 738, 738, '2021-01-06', 1, 0, 'PRENSA'),
(114, 'TP09', 747, 747, 747, '2021-01-08', 1, 1, 'PRENSA'),
(115, 'TP09', 747, 747, 747, '2021-01-08', 1, 0, 'PRENSA'),
(117, 'TP41', 738, 738, 738, '2021-01-13', 1, 0, 'PRENSA'),
(119, 'TP41', 738, 738, 738, '2021-01-13', 1, 0, 'PRENSA'),
(120, 'TP03', 742, 742, 742, '2021-01-14', 1, 1, 'PRENSA'),
(121, 'TP03', 742, 742, 742, '2021-01-14', 1, 0, 'PRENSA'),
(122, 'TP02', 174, 174, 174, '2021-01-14', 1, 1, 'PRENSA'),
(123, 'TP02', 174, 174, 174, '2021-01-14', 1, 0, 'PRENSA'),
(124, 'TP01', 746, 746, 746, '2021-01-15', 1, 1, 'PRENSA'),
(125, 'TP01', 746, 746, 746, '2021-01-15', 1, 0, 'PRENSA'),
(126, 'TP03', 755, 755, 755, '2021-01-15', 1, 1, 'PRENSA'),
(127, 'TP03', 755, 755, 755, '2021-01-15', 1, 0, 'PRENSA'),
(128, 'TP01', 753, 753, 753, '2021-01-18', 1, 1, 'PRENSA'),
(129, 'TP01', 753, 753, 753, '2021-01-18', 1, 0, 'PRENSA'),
(130, 'TP02', 174, 174, 174, '2021-01-18', 1, 1, 'PRENSA'),
(131, 'TP02', 174, 174, 174, '2021-01-18', 1, 0, 'PRENSA'),
(132, 'TP11', 740, 740, 740, '2021-01-21', 1, 1, 'PRENSA'),
(133, 'TP11', 740, 740, 740, '2021-01-21', 1, 0, 'PRENSA'),
(134, 'TP11', 735, 735, 735, '2021-01-21', 1, 1, 'PRENSA'),
(135, 'TP11', 735, 735, 735, '2021-01-21', 1, 0, 'PRENSA'),
(136, 'TP09', 755, 755, 755, '2021-01-22', 1, 1, 'PRENSA'),
(137, 'TP09', 755, 755, 755, '2021-01-22', 1, 0, 'PRENSA'),
(138, 'TP10', 743, 743, 743, '2021-01-22', 1, 1, 'PRENSA'),
(139, 'TP10', 743, 743, 743, '2021-01-22', 1, 0, 'PRENSA'),
(140, 'TP02', 746, 746, 746, '2021-01-27', 1, 1, 'PRENSA'),
(141, 'TP02', 746, 746, 746, '2021-01-27', 1, 0, 'PRENSA'),
(142, 'TP04', 740, 740, 740, '2021-01-27', 1, 1, 'PRENSA'),
(143, 'TP04', 740, 740, 740, '2021-01-27', 1, 0, 'PRENSA'),
(144, 'TP09', 756, 756, 756, '2021-02-18', 1, 1, 'PRENSA'),
(145, 'TP09', 756, 756, 756, '2021-02-18', 1, 0, 'PRENSA'),
(146, 'TP02', 747, 747, 747, '2021-02-18', 1, 1, 'PRENSA'),
(147, 'TP02', 747, 747, 747, '2021-02-18', 1, 0, 'PRENSA'),
(148, 'TP11', 740, 740, 740, '2021-02-24', 1, 1, 'PRENSA'),
(149, 'TP11', 740, 740, 740, '2021-02-24', 1, 0, 'PRENSA'),
(150, 'TP01', 745, 745, 745, '2021-04-19', 1, 1, 'PRENSA'),
(151, 'TP01', 745, 745, 745, '2021-04-19', 1, 0, 'PRENSA'),
(152, 'TP01', 169, 169, 169, '2021-06-14', 1, 1, 'PRENSA'),
(153, 'TP01', 169, 169, 169, '2021-06-14', 1, 1, 'PRENSA'),
(154, 'TP01', 169, 169, 169, '2021-06-14', 1, 1, 'PRENSA'),
(155, 'TP01', 169, 169, 169, '2021-06-14', 1, 1, 'PRENSA'),
(156, 'TP05', 758, 758, 758, '2021-06-15', 1, 1, 'PRENSA'),
(157, 'TP05', 758, 758, 758, '2021-06-15', 1, 1, 'PRENSA'),
(158, 'TP02', 745, 745, 745, '2021-06-15', 1, 1, 'PRENSA'),
(159, 'TP02', 745, 745, 745, '2021-06-15', 1, 1, 'PRENSA'),
(160, 'TP02', 745, 745, 745, '2021-06-15', 1, 1, 'PRENSA'),
(161, 'TP02', 745, 745, 745, '2021-06-15', 1, 0, 'PRENSA'),
(162, 'TP08', 738, 738, 738, '2021-06-17', 1, 1, 'PRENSA'),
(163, 'TP01', 745, 745, 745, '2021-06-21', 1, 1, 'PRENSA'),
(164, 'TP01', 745, 745, 745, '2021-06-21', 1, 0, 'PRENSA'),
(165, 'TP04', 748, 746, 746, '2021-06-22', 1, 1, 'PRENSA'),
(166, 'TP04', 746, 746, 746, '2021-06-22', 1, 0, 'PRENSA'),
(167, 'TP02', 741, 741, 741, '2021-06-24', 1, 1, 'PRENSA'),
(168, 'TP02', 741, 741, 741, '2021-06-24', 1, 0, 'PRENSA'),
(169, 'TP11', 753, 753, 753, '2021-06-30', 1, 1, 'PRENSA'),
(170, 'TP11', 753, 753, 753, '2021-06-30', 1, 0, 'PRENSA'),
(171, 'TP10', 758, 758, 758, '2021-07-21', 1, 1, 'PRENSA'),
(172, 'TP10', 758, 758, 758, '2021-07-21', 1, 0, 'PRENSA'),
(173, 'TP10', 758, 758, 758, '2021-07-22', 1, 1, 'PRENSA'),
(174, 'TP10', 758, 758, 758, '2021-07-22', 1, 0, 'PRENSA'),
(175, 'TH02', 66, 66, 66, '2021-08-03', 1, 0, 'MAQUINADO'),
(176, 'TH13', 69, 69, 69, '2021-08-04', 1, 0, 'MAQUINADO'),
(177, 'TH02', 42, 42, 42, '2021-08-06', 1, 0, 'MAQUINADO'),
(178, 'TH12', 35, 35, 35, '2021-08-10', 1, 1, 'MAQUINADO'),
(179, 'TH11', 47, 49, 49, '2021-08-10', 1, 0, 'MAQUINADO'),
(180, 'TH13', 27, 27, 27, '2021-08-10', 1, 1, 'MAQUINADO'),
(181, 'TH03', 131, 34, 131, '2021-08-18', 1, 1, 'MAQUINADO'),
(182, 'TH03', 131, 34, 34, '2021-08-18', 1, 0, 'MAQUINADO'),
(183, 'TP02', 756, 756, 756, '2021-08-19', 1, 1, 'PRENSA'),
(184, 'TP02', 756, 756, 756, '2021-08-19', 1, 1, 'PRENSA'),
(185, 'TH10', 42, 42, 42, '2021-08-19', 1, 1, 'MAQUINADO'),
(186, 'TH06', 37, 122, 122, '2021-08-20', 1, 1, 'MAQUINADO'),
(187, 'TH06', 122, 122, 122, '2021-08-20', 1, 0, 'MAQUINADO'),
(188, 'TH06', 123, 66, 122, '2021-08-20', 1, 1, 'MAQUINADO'),
(189, 'TH17', 58, 66, 30, '2021-08-20', 1, 1, 'MAQUINADO'),
(190, 'TH12', 66, 66, 122, '2021-08-20', 1, 1, 'MAQUINADO'),
(191, 'TP41', 747, 747, 747, '2021-08-24', 1, 0, 'PRENSA'),
(192, 'TP41', 741, 741, 741, '2021-08-24', 1, 0, 'PRENSA'),
(193, 'TP11', 745, 745, 745, '2021-08-25', 1, 1, 'PRENSA'),
(194, 'TP11', 745, 745, 745, '2021-08-25', 1, 1, 'PRENSA'),
(195, 'TP05', 174, 148, 148, '2021-08-25', 1, 1, 'PRENSA'),
(196, 'TP05', 148, 148, 148, '2021-08-25', 1, 1, 'PRENSA'),
(197, 'TH05', 66, 66, 66, '2021-08-26', 1, 0, 'MAQUINADO'),
(198, 'TH14', 69, 69, 69, '2021-08-26', 1, 1, 'MAQUINADO'),
(199, 'TP11', 745, 745, 745, '2021-08-26', 1, 1, 'PRENSA'),
(200, 'TP11', 745, 745, 745, '2021-08-26', 1, 0, 'PRENSA'),
(201, 'TP01', 757, 757, 757, '2021-08-26', 1, 0, 'PRENSA'),
(202, 'TP08', 170, 170, 170, '2021-08-26', 1, 1, 'PRENSA'),
(203, 'TH10', 131, 53, 131, '2021-08-26', 1, 1, 'MAQUINADO'),
(204, 'TH10', 131, 53, 131, '2021-08-26', 1, 1, 'MAQUINADO'),
(205, 'TH05', 35, 42, 35, '2021-08-27', 1, 0, 'MAQUINADO'),
(206, 'TP08', 738, 756, 756, '2021-08-27', 1, 1, 'PRENSA'),
(207, 'TP08', 756, 756, 756, '2021-08-27', 1, 0, 'PRENSA'),
(208, 'TP41', 148, 148, 148, '2021-08-27', 1, 0, 'PRENSA'),
(209, 'TP01', 750, 750, 750, '2021-08-27', 1, 0, 'PRENSA'),
(210, 'TP10', 752, 752, 752, '2021-08-27', 1, 1, 'PRENSA'),
(211, 'TP04', 737, 743, 743, '2021-08-30', 1, 1, 'PRENSA'),
(212, 'TP04', 743, 743, 743, '2021-08-30', 1, 0, 'PRENSA'),
(213, 'TP04', 744, 744, 744, '2021-08-30', 1, 0, 'PRENSA'),
(214, 'TP41', 740, 740, 740, '2021-08-30', 1, 0, 'PRENSA'),
(215, 'TH07', 42, 42, 48, '2021-08-31', 1, 0, 'MAQUINADO'),
(216, 'TH02', 131, 27, 131, '2021-09-02', 1, 0, 'MAQUINADO'),
(217, 'TP09', 174, 755, 755, '2021-09-21', 1, 1, 'PRENSA'),
(218, 'TP09', 755, 755, 755, '2021-09-21', 1, 0, 'PRENSA'),
(219, 'TP08', 735, 735, 735, '2021-09-21', 1, 0, 'PRENSA'),
(220, 'TP03', 735, 735, 735, '2021-09-21', 1, 0, 'PRENSA'),
(221, 'TP03', 751, 751, 751, '2021-09-21', 1, 1, 'PRENSA'),
(222, 'TP01', 755, 753, 753, '2021-09-22', 1, 1, 'PRENSA'),
(223, 'TP01', 753, 753, 753, '2021-09-22', 1, 0, 'PRENSA'),
(224, 'TP04', 174, 174, 174, '2021-09-22', 1, 0, 'PRENSA'),
(225, 'TP09', 746, 746, 746, '2021-09-23', 1, 1, 'PRENSA'),
(226, 'TP09', 746, 746, 746, '2021-09-23', 1, 1, 'PRENSA'),
(227, 'TH42', 37, 2, 37, '2021-09-28', 1, 1, 'MAQUINADO'),
(228, 'TP05', 752, 752, 752, '2021-09-28', 1, 1, 'PRENSA'),
(229, 'TP05', 752, 752, 752, '2021-09-28', 1, 1, 'PRENSA'),
(230, 'TH17', 122, 36, 129, '2021-09-29', 1, 0, 'MAQUINADO'),
(231, 'TH17', 31, 36, 48, '2021-09-29', 1, 0, 'MAQUINADO'),
(232, 'TH17', 58, 2, 58, '2021-10-12', 1, 1, 'MAQUINADO'),
(233, 'TH17', 58, 2, 58, '2021-10-12', 1, 1, 'MAQUINADO'),
(234, 'TH17', 58, 2, 58, '2021-10-12', 1, 1, 'MAQUINADO'),
(235, 'th10', 28, 45, 54, '2022-05-19', 1, 1, 'MAQUINADO'),
(236, 'TH10', 28, 28, 28, '2022-05-19', 1, 1, 'MAQUINADO'),
(237, 'TH10', 28, 45, 45, '2022-05-20', 1, 1, 'MAQUINADO'),
(238, 'TH10', 45, 45, 45, '2022-05-23', 1, 1, 'MAQUINADO'),
(239, 'TH10', 34, 34, 34, '2022-05-23', 1, 1, 'MAQUINADO'),
(240, 'TG01', 701, 701, 701, '2022-05-24', 1, 1, 'T. SUPERFICIES'),
(241, 'TH10', 28, 28, 28, '2022-05-25', 1, 1, 'MAQUINADO'),
(242, 'TH10', 28, 28, 28, '2022-05-25', 1, 1, 'MAQUINADO'),
(243, 'TH10', 28, 28, 28, '2022-05-25', 1, 1, 'MAQUINADO'),
(244, 'TH10', 28, 28, 28, '2022-05-26', 1, 1, 'MAQUINADO'),
(245, 'TH10', 28, 28, 28, '2022-05-26', 1, 1, 'MAQUINADO'),
(246, 'TH10', 47, 47, 47, '2022-05-26', 1, 1, 'MAQUINADO'),
(247, 'TH10', 28, 28, 28, '2022-05-26', 1, 1, 'MAQUINADO'),
(248, 'TH10', 28, 28, 28, '2022-05-30', 1, 1, 'MAQUINADO'),
(249, 'TH10', 35, 35, 35, '2022-05-30', 1, 1, 'MAQUINADO'),
(250, 'TH10', 17, 17, 17, '2022-05-30', 1, 1, 'MAQUINADO'),
(251, 'TH10', 25, 25, 25, '2022-05-30', 1, 1, 'MAQUINADO'),
(252, 'TH17', 25, 25, 25, '2022-05-30', 1, 1, 'MAQUINADO'),
(253, 'TH10', 19, 19, 19, '2022-05-30', 1, 1, 'MAQUINADO'),
(254, 'TH10', 18, 18, 18, '2022-06-14', 1, 1, 'MAQUINADO'),
(255, 'TP01', 600, 600, 600, '2024-01-10', 1, 1, 'PRENSA'),
(256, 'TP01', 600, 600, 600, '2024-01-10', 1, 1, 'PRENSA'),
(257, 'TH17', 31, 2, 2, '2024-01-10', 1, 1, 'MAQUINADO'),
(258, 'TH17', 31, 2, 2, '2024-01-10', 1, 0, 'MAQUINADO'),
(259, 'TP11', 735, 735, 735, '2024-01-17', 1, 1, 'PRENSA'),
(260, 'TP11', 735, 735, 735, '2024-01-17', 1, 1, 'PRENSA'),
(261, 'TP11', 600, 600, 600, '2024-01-17', 1, 1, 'PRENSA'),
(262, 'TP11', 735, 735, 735, '2024-01-17', 1, 1, 'PRENSA'),
(263, 'TP11', 734, 734, 734, '2024-01-17', 1, 1, 'PRENSA'),
(264, 'TP11', 600, 600, 600, '2024-02-12', 1, 1, 'PRENSA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_coiling_totales`
--

CREATE TABLE `das_coiling_totales` (
  `id_dascolitot` int(11) NOT NULL,
  `total_mts_aceptados` double NOT NULL,
  `total_mts_procesados` double NOT NULL,
  `total_bobina` int(11) NOT NULL,
  `totalScrap` double NOT NULL,
  `das_iddas` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_produccion`
--

CREATE TABLE `das_produccion` (
  `id_dasproduccion` int(11) NOT NULL,
  `modelo` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `estandar` varchar(15) COLLATE utf8_spanish_ci NOT NULL,
  `lote` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `inicio_cm` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fin_cm` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `inicio_tp` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fin_tp` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `das_ida_das` int(10) NOT NULL,
  `mog_idmog` int(11) NOT NULL,
  `piezasProcesadas_id_piezadprocesada` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `das_produccion`
--

INSERT INTO `das_produccion` (`id_dasproduccion`, `modelo`, `estandar`, `lote`, `inicio_cm`, `fin_cm`, `inicio_tp`, `fin_tp`, `das_ida_das`, `mog_idmog`, `piezasProcesadas_id_piezadprocesada`) VALUES
(1, 'GMET4H0 MNL STD2', 'STD 2', '1D', NULL, NULL, '11:14:15', '13:47', 175, 128, NULL),
(2, 'COYOTE C/R L STD', 'STD', 'TIH', NULL, NULL, '08:38:23', '10:18', 176, 129, NULL),
(3, 'COYOTE C/R L STD', 'STD', '', NULL, NULL, '11:33:30', '12:45', 176, 129, NULL),
(4, 'AP2 TC C/R STD C', 'STD C', 'TIG2C', NULL, NULL, '11:33:52', '11:56', 177, 131, NULL),
(5, 'AP2 TC C/R STD C', 'STD C', 'TI', NULL, NULL, '12:15:34', '12:26', 177, 131, NULL),
(6, 'AP2 TC C/R STD C', 'STD C', 'TI', NULL, NULL, '12:27:24', '12:33', 177, 131, NULL),
(7, 'I4 C/R STD 2', 'STD 2', 'THI', NULL, NULL, '00:40:51', '00:43', 178, 132, NULL),
(8, 'I4 C/R STD 2', 'STD 2', 'THI', NULL, NULL, '00:40:51', '00:43', 178, 132, NULL),
(9, 'I4 C/R STD 2', 'STD 2', '', NULL, NULL, '00:44:11', '00:47', 178, 132, NULL),
(10, 'PENTASTARCR STD3', 'STD 3', 'UY', NULL, NULL, '20:35:07', '20:38', 179, 134, NULL),
(11, 'COYOTE C/R U STD', 'STD', 'GH', NULL, NULL, '21:02:01', '21:06', 180, 135, NULL),
(12, 'COYOTE C/R U STD', 'STD', '', NULL, NULL, '21:06:44', '21:11', 180, 135, NULL),
(13, 'TR C/R STD 2', 'STD 2', 'T1H', NULL, NULL, '09:05:12', '09:43', 181, 139, NULL),
(14, 'TR C/R STD 2', 'STD 2', 'T1H', NULL, NULL, '12:39:00', '13:10', 182, 139, NULL),
(15, 'TR C/R STD 2', 'STD 2', 'T1H', NULL, NULL, '12:39:00', '13:10', 182, 139, NULL),
(16, 'TR C/R STD 2', 'STD 2', 'T1H', NULL, NULL, '13:11:08', '13:23', 182, 139, NULL),
(17, 'NP0 M/N U STD D', 'STD D', 'T1GI', NULL, NULL, '11:09:13', '11:25', 185, 141, NULL),
(18, 'DRAGON M/NL STD2', 'STD 2', '1FG', NULL, NULL, '09:58:18', '10:04', 187, 142, NULL),
(19, 'DRAGON M/NL STD2', 'STD 2', 'T1HC', NULL, NULL, '10:13:38', '10:23', 188, 142, NULL),
(20, '6TA M/N U STD3', 'STD 3', '', NULL, NULL, '13:58:46', '14:04', 189, 143, NULL),
(21, '6TA M/N U STD3', 'STD 3', '', NULL, NULL, '13:58:46', '14:04', 189, 143, NULL),
(22, '6TA M/N U STD3', 'STD 3', '566', NULL, NULL, '14:04:43', '14:06', 189, 143, NULL),
(23, '6TA M/N U STD3', 'STD 3', '', NULL, NULL, '13:58:46', '14:04', 189, 143, NULL),
(24, '6TA M/N U STD3', 'STD 3', '566', NULL, NULL, '14:04:43', '14:06', 189, 143, NULL),
(25, '6TA M/N U STD3', 'STD 3', '665', NULL, NULL, '14:07:19', '14:09', 189, 143, NULL),
(26, 'I4 C/R STD 2', 'STD 2', '', NULL, NULL, '15:56:03', '15:58', 190, 144, NULL),
(27, 'GTDI M/N L', 'STD 4', 'T1HT0', NULL, NULL, '02:13:42', '02:19', 197, 152, 230),
(29, 'GTDI M/N L', 'STD 4', 'T1H', NULL, NULL, '02:20:16', '02:23', 197, 152, 231),
(30, '5R0(AP2)C/RSTDG', 'STD G', 'T1H', NULL, NULL, '14:23:47', '14:35', 204, 157, NULL),
(31, '5R0(AP2)C/RSTDG', 'STD G', 'T1H', NULL, NULL, '14:23:47', '14:35', 204, 157, NULL),
(32, '5R0(AP2)C/RSTDG', 'STD G', 'T1H', NULL, NULL, '14:23:47', '14:35', 204, 157, NULL),
(33, '5R0(AP2)C/RSTDG', 'STD G', 'T1H', NULL, NULL, '14:23:47', '14:35', 204, 157, NULL),
(34, '5R0(AP2)C/RSTDG', 'STD G', 'T1H', NULL, NULL, '14:23:47', '14:35', 204, 157, NULL),
(35, 'GTDI M/N L', 'STD 4', '678', NULL, NULL, '06:16:49', '06:22', 205, 158, NULL),
(36, 'GTDI M/N L', 'STD 4', '678', NULL, NULL, '06:16:49', '06:22', 205, 158, NULL),
(37, 'GTDI M/N L', 'STD 4', '67', NULL, NULL, '06:23:24', '06:28', 205, 158, NULL),
(38, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(39, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(40, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(41, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(42, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(43, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(44, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(45, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(46, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(47, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(48, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(49, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(50, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(51, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(52, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(53, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(54, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(55, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(56, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(57, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(58, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(59, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(60, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(61, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(62, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(63, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(64, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(65, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(66, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(67, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(68, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(69, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(70, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(71, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(72, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(73, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(74, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(75, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(76, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(77, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(78, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(79, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(80, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(81, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(82, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(83, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(84, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(85, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(86, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(87, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(88, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(89, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(90, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(91, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(92, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(93, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(94, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(95, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(96, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(97, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(98, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(99, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(100, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(101, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(102, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(103, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(104, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(105, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(106, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(107, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(108, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(109, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(110, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(111, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(112, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(113, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(114, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(115, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(116, 'NP0 C/R STD C', 'STD C', 'TIF', NULL, NULL, '12:37:43', '13:58', 215, 167, NULL),
(117, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '13:59:52', '14:29', 215, 167, NULL),
(118, 'NP0 C/R STD C', 'STD C', 'T1HI', NULL, NULL, '14:30:35', '15:57', 215, 167, NULL),
(119, '', '', '', NULL, NULL, '14:30:35', '16:04', 215, 167, NULL),
(120, '5R0(AP2)C/R STDC', 'STD C', 'THI1', NULL, NULL, '07:35:39', '07:43', 216, 168, NULL),
(121, '5R0(AP2)C/R STDC', 'STD C', 'THI1', NULL, NULL, '07:35:39', '07:43', 216, 168, NULL),
(122, '5R0(AP2)C/R STDC', 'STD C', 'THI1', NULL, NULL, '07:44:03', '07:56', 216, 168, NULL),
(123, '130Y/133Y M/NU5', 'STD 5', 'TI1', NULL, NULL, '10:57:51', '11:11:28', 230, 180, 294),
(124, '130Y/133Y M/NU5', 'STD 5', 'TI1', NULL, NULL, '11:37:33', '12:04:10', 231, 180, 295),
(125, 'ZV9 M/N U STD 3', 'STD 3', 'T1J', NULL, NULL, '12:21:36', '12:44:10', 234, 181, 296),
(126, 'HEMI 5.7L C/R', 'STD', '5HF', NULL, NULL, '07:15:02', '07:18:54', 238, 186, 297),
(127, 'AP4 M/N L STD D', 'STD D', 'JYJY', NULL, NULL, '13:27:15', '13:30:44', 239, 187, 298),
(128, 'I4GTDI C/RL STD2', 'STD 2', 'EG', NULL, NULL, '09:24:01', '09:25:08', 241, 191, 300),
(129, '5J6 (NP0 M/N U)', 'STD B', 'EG1', NULL, NULL, '11:29:44', '11:34:40', 242, 192, 301),
(130, 'AP4 C/R STD D', 'STD D', 'EG5', NULL, NULL, '13:47:20', '13:49:58', 243, 194, 302),
(131, 'ZV5K4 C/R STD 1', 'STD 1', ' EWGE', NULL, NULL, '09:41:43', '09:44:11', 244, 196, 303),
(132, 'RNA (NP4)', 'STD D', '33', NULL, NULL, '09:52:47', '09:54:25', 245, 24, 304),
(133, 'HEMI 5.7L C/R', 'STD', 'DGF', NULL, NULL, '10:18:33', '10:21:03', 246, 197, 305),
(134, 'I4 M/N U', 'STD 5', 'DE3', NULL, NULL, '13:30:17', '13:36:27', 247, 198, 306),
(135, 'HEMI 5.7L C/R', 'STD', 'e64', NULL, NULL, '08:40:06', '09:25:11', 248, 199, 307),
(136, 'ZH2 M/N L STD 6', 'STD 6', 'EGE', NULL, NULL, '10:27:49', '10:40:01', 250, 203, 308),
(137, 'GTDI C/R', 'STD 2', 'E534', NULL, NULL, '11:39:46', '11:54:24', 251, 204, 309),
(138, 'NP0 C/R STD C', 'STD C', 'E43', NULL, NULL, '13:57:54', '13:59:01', 252, 205, 310),
(139, 'ZV5K4 C/R STD 1', 'STD 1', '4ED', NULL, NULL, '14:10:24', '14:13:31', 253, 206, 311),
(140, 'AP4 M/N L STD D', 'STD D', '', NULL, NULL, '14:20:21', '14:27:14', 253, 207, 312),
(141, 'I4 C/R STD 1', 'STD 1', 'EFE', NULL, NULL, '10:56:03', '11:00:21', 254, 210, 313);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_produ_empamesas`
--

CREATE TABLE `das_produ_empamesas` (
  `id_proEmpMes` int(11) NOT NULL,
  `lote` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `ini_tu` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `fin_tu` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `cant_proce` int(11) NOT NULL,
  `pza_buenas` int(11) NOT NULL,
  `pza_Sort` int(11) NOT NULL,
  `Sobrante_Final` int(11) NOT NULL,
  `mog_idmog` int(11) NOT NULL,
  `das_iddas` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_prod_bgprensa`
--

CREATE TABLE `das_prod_bgprensa` (
  `id_dasprodbgp` int(11) NOT NULL,
  `no_mat` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `anch_mat` double NOT NULL,
  `num_lot_mat` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `metros` double NOT NULL,
  `lote` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `ini_cm` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `fin_cm` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ini_tp` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `fin_tp` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `pcs_pro` int(11) NOT NULL,
  `pcs_buenas` int(11) NOT NULL,
  `pcs_scrap` int(11) NOT NULL,
  `pcs_bm` int(11) NOT NULL,
  `das_id_das` int(10) NOT NULL,
  `mog_idmog` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_prod_bgproceso`
--

CREATE TABLE `das_prod_bgproceso` (
  `id_dasprodbgproc` int(11) NOT NULL,
  `num_parteFin` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `material_bush` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `material_rg` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `material_sr` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `lote_bush` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `lote_rg` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `lote_sr` varchar(25) COLLATE utf8_spanish_ci DEFAULT NULL,
  `ini_cm` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `fin_cm` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ini_tp` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `fin_tp` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `pcs_pro_bush` int(11) NOT NULL,
  `pcs_buen_bush` int(11) NOT NULL,
  `scrap_bush` int(11) NOT NULL,
  `pcs_pro_rg` int(11) NOT NULL,
  `pcs_buen_rg` int(11) NOT NULL,
  `scrp_rg` int(11) NOT NULL,
  `pcs_pro_sr` int(11) DEFAULT NULL,
  `pcs_buen_sr` int(11) DEFAULT NULL,
  `scrap_sr` int(11) DEFAULT NULL,
  `mog_idmog` int(11) NOT NULL,
  `das_iddas` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_prod_coi`
--

CREATE TABLE `das_prod_coi` (
  `id_dasprodcoi` int(11) NOT NULL,
  `ordencoil_idordencoil` int(11) NOT NULL,
  `totalProd` double NOT NULL,
  `scrap` double NOT NULL,
  `aj_in` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `aj_fin` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `pro_in` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `pro_fin` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `doble_bobina` tinyint(1) NOT NULL,
  `cant_tira1` int(11) NOT NULL,
  `cant_tira2` int(11) NOT NULL,
  `das_idas` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_prod_empmaq`
--

CREATE TABLE `das_prod_empmaq` (
  `das_prod_emp_id` int(11) NOT NULL,
  `lote` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ini_cm` varchar(20) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fin_cm` varchar(20) COLLATE utf8_spanish_ci DEFAULT NULL,
  `ini_tp` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `fin_tp` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `scrap_sorting` int(11) NOT NULL,
  `scrap_maquina` int(11) NOT NULL,
  `das_iddas` int(10) NOT NULL,
  `mog_idmog` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_prod_grading`
--

CREATE TABLE `das_prod_grading` (
  `id_prodGrad` int(11) NOT NULL,
  `lote` varchar(20) NOT NULL DEFAULT '0',
  `in_cm` varchar(20) DEFAULT '0',
  `fin_cm` varchar(20) DEFAULT '0',
  `in_tp` varchar(20) DEFAULT '0',
  `fin_tp` varchar(20) DEFAULT '0',
  `sorting` int(11) DEFAULT 0,
  `maquina` int(11) DEFAULT 0,
  `mog_idmog` int(11) DEFAULT 0,
  `das_idas` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_prod_plat`
--

CREATE TABLE `das_prod_plat` (
  `das_prod_plat_id` int(11) NOT NULL,
  `mog_idmog` int(11) NOT NULL,
  `lote` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `ini_tur` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `fin_tur` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `razon1` int(11) DEFAULT NULL,
  `razon2` int(11) DEFAULT NULL,
  `razon3` int(11) DEFAULT NULL,
  `razon4` int(11) DEFAULT NULL,
  `razon5` int(11) DEFAULT NULL,
  `razon6` int(11) DEFAULT NULL,
  `razon7` int(11) DEFAULT NULL,
  `razon8` int(11) DEFAULT NULL,
  `razon9` int(11) DEFAULT NULL,
  `das_id_das` int(10) NOT NULL,
  `piezasProcesadas_id_piezadprocesada` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_prod_pren`
--

CREATE TABLE `das_prod_pren` (
  `id_daspropren` int(11) NOT NULL,
  `material` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `corte_coiling` double NOT NULL,
  `lot_material` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `metros` double NOT NULL,
  `inicio_cm` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fin_cm` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `inicio_tp` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fin_tp` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `centroEstampado` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL,
  `extremo` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL,
  `sello` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL,
  `pzasTotales` int(11) NOT NULL,
  `bm_kg` double NOT NULL,
  `pcs_bm` int(11) NOT NULL,
  `pza_ok` int(11) NOT NULL,
  `ng_kg` double NOT NULL,
  `pcs_ng` int(11) NOT NULL,
  `das_iddas` int(10) NOT NULL,
  `mog_idmog` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `das_prod_pren`
--

INSERT INTO `das_prod_pren` (`id_daspropren`, `material`, `corte_coiling`, `lot_material`, `metros`, `inicio_cm`, `fin_cm`, `inicio_tp`, `fin_tp`, `centroEstampado`, `extremo`, `sello`, `pzasTotales`, `bm_kg`, `pcs_bm`, `pza_ok`, `ng_kg`, `pcs_ng`, `das_iddas`, `mog_idmog`) VALUES
(1, 'Y7007', 18, 'A2020701-31', 0, NULL, NULL, '15:50:45', '15:55', NULL, NULL, '', 6829, 4.5, 274, 6525, 0.5, 30, 2, 31),
(2, 'Y7007', 18, 'A2020701-31', 0, NULL, NULL, '15:50:45', '15:55', NULL, NULL, '', 6555, 4.5, 274, 6525, 0.5, 30, 2, 31),
(3, 'Y7007', 18, 'A2020601-31', 0, NULL, NULL, '15:58:08', '16:00', NULL, NULL, '', 2025, 0, 0, 2025, 0, 0, 2, 31),
(4, 'Y7007', 18, 'A2020701-31', 0, NULL, NULL, '15:50:45', '15:55', NULL, NULL, '', 6555, 4.5, 274, 6525, 0.5, 30, 2, 31),
(5, 'Y7007', 18, 'A2020601-31', 0, NULL, NULL, '15:58:08', '16:00', NULL, NULL, '', 2025, 0, 0, 2025, 0, 0, 2, 31),
(6, 'Y7007', 18, 'A2020601-31', 0, NULL, NULL, '16:01:28', '16:07', NULL, NULL, '', 11505, 8, 487, 11475, 0.5, 30, 2, 31),
(7, 'Y7007', 18, 'A2020601-31', 0, NULL, NULL, '16:05:09', '16:11', NULL, NULL, '', 4335, 1, 60, 4275, 0, 0, 4, 32),
(8, 'Y7007', 18, 'L2211902-31', 0, '', '', '16:21:14', '16:24', NULL, NULL, '', 15768, 9, 548, 15750, 0.3, 18, 4, 32),
(9, 'Y2356', 26.36, 'E22365214-31', 0, NULL, NULL, '18:07:14', '18:09', NULL, NULL, '', 7060, 32, 1842, 4950, 3, 268, 11, 35),
(10, 'Y7902', 20.9, 'A2379101-31', 0, NULL, NULL, '13:41:17', '13:44', NULL, NULL, '', 4905, 4, 141, 4750, 0, 14, 13, 37),
(11, 'Y7902', 20.9, 'B2058801-31', 0, NULL, NULL, '13:40:49', '13:47', NULL, NULL, '', 5168, 6.5, 230, 4918, 0, 20, 15, 36),
(12, 'Y7902', 20.9, 'A2349401-31', 0, '', '', '13:48:23', '13:51', NULL, NULL, '', 11079, 21.5, 762, 10247, 1, 70, 15, 36),
(13, 'Y7904', 21, 'D2197201-31', 0, NULL, NULL, '14:10:15', '14:13', NULL, NULL, '', 5785, 1.5, 69, 5670, 0.5, 46, 21, 39),
(14, 'Y7904', 21, 'E2027801-31', 0, '', '', '14:19:18', '14:23', NULL, NULL, '', 6553, 3.5, 161, 6300, 1, 92, 21, 39),
(15, 'C9447', 14.6, 'G9005702-31', 0, NULL, NULL, '11:50:29', '11:54', NULL, NULL, '', 16444, 11, 1023, 15400, 0, 21, 28, 44),
(16, 'C9447', 14.6, 'G9005702-31', 0, NULL, NULL, '11:50:29', '11:54', NULL, NULL, '', 5719, 11, 1023, 4675, 0, 21, 28, 44),
(17, 'Y7904', 21, 'E2027701-31', 0, NULL, NULL, '13:51:36', '13:56', NULL, NULL, '', 12943, 8, 368, 12552, 0.5, 23, 34, 45),
(18, 'Y7904', 21, 'E2185401-31', 0, NULL, NULL, '13:52:03', '13:55', NULL, NULL, '', 22344, 14, 645, 21630, 1.5, 69, 32, 46),
(19, 'Y7904', 21, 'E2027701-31', 0, NULL, NULL, '13:51:36', '13:56', NULL, NULL, '', 8161, 8, 368, 7770, 0.5, 23, 34, 45),
(20, 'Y7904', 21, 'E2185401-31', 0, NULL, NULL, '13:56:58', '15:17', NULL, NULL, '', 8066, 4, 184, 7770, 1.2, 57, 34, 45),
(21, 'C9447', 14.6, 'G9006201-31', 0, NULL, NULL, '11:14:36', '11:26', NULL, NULL, '', 16430, 14.5, 1348, 14850, 2.5, 232, 36, 47),
(22, 'C9447', 14.6, 'F9066202-31', 0, NULL, NULL, '11:35:48', '12:01', NULL, NULL, '', 5768, 5, 465, 5225, 0.45, 78, 36, 47),
(23, 'C9447', 14.6, 'F9066202-31', 0, NULL, NULL, '12:09:09', '12:13', NULL, NULL, '', 21144, 11, 1023, 20075, 0.5, 46, 38, 48),
(24, 'C9447', 14.6, 'F9112502-31', 0, NULL, NULL, '15:32:06', '15:44', NULL, NULL, '', 16985, 19, 1767, 15125, 1, 93, 46, 51),
(25, 'C9447', 14.6, 'F9112502-31', 0, NULL, NULL, '15:32:06', '15:44', NULL, NULL, '', 6810, 19, 1767, 4950, 1, 93, 46, 51),
(26, 'C9447', 14.6, 'F9112502-31', 0, NULL, NULL, '15:55:58', '16:03', NULL, NULL, '', 5554, 6, 558, 4950, 0.5, 46, 46, 51),
(27, 'C9447', 14.6, 'F9112502-31', 0, NULL, NULL, '15:32:06', '15:44', NULL, NULL, '', 16985, 19, 1767, 15125, 1, 93, 46, 51),
(28, 'C9447', 14.6, 'F9112502-31', 0, NULL, NULL, '15:55:58', '16:03', NULL, NULL, '', 5554, 6, 558, 4950, 0.5, 46, 46, 51),
(29, 'C9447', 14.6, 'F9112502-31', 0, NULL, NULL, '16:07:08', '16:15', NULL, NULL, '', 13443, 13.5, 1255, 12100, 0.95, 88, 46, 52),
(30, 'C9447', 14.6, 'F9112502-31', 0, NULL, NULL, '16:07:08', '16:15', NULL, NULL, '', 9318, 13.5, 1255, 7975, 0.95, 88, 46, 52),
(31, 'C9447', 14.6, 'F9112404-31', 0, '', '', '16:16:02', '16:20', NULL, NULL, '', 8713, 7.5, 697, 7975, 0.45, 41, 46, 52),
(32, 'Y7904', 18.6, 'D2290601-31', 0, NULL, NULL, '15:18:03', '15:23', NULL, NULL, '', 14330, 0.5, 25, 14280, 0.5, 25, 48, 53),
(33, 'Y7904', 18.6, 'D2290601-31', 0, NULL, NULL, '15:18:03', '15:23', NULL, NULL, '', 5930, 0.5, 25, 5880, 0.5, 25, 48, 53),
(34, 'Y7904', 18.6, 'D2290601-31', 0, NULL, NULL, '15:26:31', '15:28', NULL, NULL, '', 6060, 3, 155, 5880, 0.5, 25, 48, 53),
(35, 'Y7904', 18.6, 'C2184801-32', 0, NULL, NULL, '15:25:34', '15:33', NULL, NULL, '', 2413, 2, 103, 2310, 0, 0, 50, 54),
(36, 'Y7904', 18.6, 'C2184801-32', 0, NULL, NULL, '15:25:34', '15:33', NULL, NULL, '', 2833, 2, 103, 2730, 0, 0, 50, 54),
(37, 'Y7904', 18.6, 'C2184901-32', 0, NULL, NULL, '15:33:48', '15:36', NULL, NULL, '', 4329, 2.5, 129, 4200, 0, 0, 50, 54),
(38, 'Y7904', 18.6, 'C2184801-32', 0, NULL, NULL, '15:25:34', '15:33', NULL, NULL, '', 2203, 2, 103, 2100, 0, 0, 50, 54),
(39, 'Y7904', 18.6, 'C2184901-32', 0, NULL, NULL, '15:33:48', '15:36', NULL, NULL, '', 8949, 2.5, 129, 8820, 0, 0, 50, 54),
(40, 'Y7904', 18.6, 'D2290701-31', 0, NULL, NULL, '15:36:39', '15:41', NULL, NULL, '', 4303, 2, 103, 4200, 0, 0, 50, 54),
(41, 'Y7904', 18.6, 'C2184801-32', 0, NULL, NULL, '15:25:34', '15:33', NULL, NULL, '', 2413, 2, 103, 2310, 0, 0, 50, 54),
(42, 'Y7904', 18.6, 'C2184901-32', 0, NULL, NULL, '15:33:48', '15:36', NULL, NULL, '', 2859, 2.5, 129, 2730, 0, 0, 50, 54),
(43, 'Y7904', 18.6, 'D2290701-31', 0, NULL, NULL, '15:36:39', '15:41', NULL, NULL, '', 4303, 2, 103, 4200, 0, 0, 50, 54),
(44, 'Y7904', 18.6, 'D2290701-31', 0, NULL, NULL, '15:41:59', '15:46', NULL, NULL, '', 2176, 1, 51, 2100, 0.5, 25, 50, 54),
(45, 'Y7904', 18.6, 'C2184801-32', 0, NULL, NULL, '15:25:34', '15:33', NULL, NULL, '', 2413, 2, 103, 2310, 0, 0, 50, 54),
(46, 'Y7904', 18.6, 'C2184901-32', 0, NULL, NULL, '15:33:48', '15:36', NULL, NULL, '', 2859, 2.5, 129, 2730, 0, 0, 50, 54),
(47, 'Y7904', 18.6, 'D2290701-31', 0, NULL, NULL, '15:36:39', '15:41', NULL, NULL, '', 4303, 2, 103, 4200, 0, 0, 50, 54),
(48, 'Y7904', 18.6, 'D2290701-31', 0, NULL, NULL, '15:41:59', '15:46', NULL, NULL, '', 2176, 1, 51, 2100, 0.5, 25, 50, 54),
(49, 'Y7904', 18.6, 'D2290601-31', 0, NULL, NULL, '15:47:28', '15:51', NULL, NULL, '', 8855, 0.5, 25, 8820, 0.2, 10, 50, 54),
(50, 'Y7910', 21, 'H2235001-31', 0, NULL, NULL, '15:38:03', '15:44', NULL, NULL, '', 4596, 2.5, 115, 4458, 0.5, 23, 52, 56),
(51, 'Y7910', 21, 'H2260901-31', 0, NULL, NULL, '15:44:55', '15:52', NULL, NULL, '', 16210, 8.5, 391, 15750, 1.5, 69, 52, 56),
(52, 'Y7910', 21, 'H2260901-31', 0, NULL, NULL, '15:44:55', '15:52', NULL, NULL, '', 16210, 8.5, 391, 15750, 1.5, 69, 52, 56),
(53, 'C9447', 15.8, 'G9079801-31', 0, NULL, NULL, '07:35:33', '10:25', NULL, NULL, '', 5252, 4.5, 252, 5000, 0, 0, 56, 59),
(54, 'Y7910', 21, 'H2234901-31', 0, NULL, NULL, '07:26:05', '10:28', NULL, NULL, '', 6786, 5, 230, 6510, 1, 46, 55, 58),
(55, 'Y7910', 21, 'H2234901-31', 0, NULL, NULL, '07:26:05', '10:28', NULL, NULL, '', 6786, 5, 230, 6510, 1, 46, 55, 58),
(56, 'Y7910', 21, 'H2261001-31', 0, NULL, NULL, '10:29:41', '13:04', NULL, NULL, '', 11268, 2.5, 115, 11130, 0.5, 23, 55, 58),
(57, 'C9447', 15.8, 'G9079801-31', 0, NULL, NULL, '07:35:33', '10:25', NULL, NULL, '', 15654, 4.5, 252, 15402, 0, 0, 56, 59),
(58, 'C9447', 15.8, 'G9079802-31', 0, NULL, NULL, '10:44:32', '15:36', NULL, NULL, '', 15879, 8, 449, 15402, 0.5, 28, 56, 59),
(59, 'C9451', 19.4, 'H9093202-31', 1234, NULL, NULL, '08:11:27', '12:53:11', NULL, NULL, '', 10115, 29, 614, 9480, 1, 21, 58, 60),
(60, 'C9451', 19.4, 'H9092601-31', 0, NULL, NULL, '13:01:04', '14:05', NULL, NULL, '', 11200, 65.5, 1387, 9686, 6, 127, 62, 60),
(61, 'Y7185', 20.8, 'G2354001-31', 0, NULL, NULL, '08:40:17', '14:55', NULL, NULL, '', 22951, 6, 638, 22260, 0.5, 53, 60, 61),
(62, 'Y7185', 22.9, 'H2048201-31', 0, NULL, NULL, '15:16:08', '15:21', NULL, NULL, '', 20197, 7, 648, 19522, 0.3, 27, 64, 62),
(63, 'Y7185', 22.9, 'H2091201-31', 0, NULL, NULL, '15:28:23', '15:32', NULL, NULL, '', 21033, 4.5, 416, 20580, 0.4, 37, 66, 63),
(64, 'Y7901', 18, 'I2370301-31', 0, NULL, NULL, '16:05:04', '16:09', NULL, NULL, '', 20664, 9, 548, 20025, 1.5, 91, 68, 64),
(65, 'Y7901', 8, 'I2370301-31', 0, NULL, NULL, '16:06:08', '16:12', NULL, NULL, '', 14817, 8.5, 518, 14275, 0.4, 24, 70, 65),
(66, 'Y7901', 18, 'H2152301-31', 0, '', '', '16:14:49', '16:18', NULL, NULL, '', 3250, 6.5, 396, 2830, 0.4, 24, 70, 65),
(67, 'Y7907', 22, 'G2337301-31', 0, NULL, NULL, '15:49:31', '15:53', NULL, NULL, '', 16921, 18, 666, 16200, 1.5, 55, 72, 66),
(68, 'Y7907', 22, 'G2337301-31', 0, NULL, NULL, '15:49:31', '15:53', NULL, NULL, '', 16921, 18, 666, 16200, 1.5, 55, 72, 66),
(69, 'Y7904', 18.6, 'I2041101-31', 0, NULL, NULL, '10:18:48', '10:22', NULL, NULL, '', 21118, 16, 829, 20160, 2.5, 129, 74, 69),
(70, 'Y7179', 18.1, 'H2281001-31', 0, NULL, NULL, '10:09:56', '11:06', NULL, NULL, '', 15942, 12, 588, 15330, 0.5, 24, 76, 68),
(71, 'Y7179', 18.1, 'H2281001-31', 0, NULL, NULL, '11:07:44', '11:12', NULL, NULL, '', 4991, 3, 147, 4830, 0.3, 14, 76, 68),
(73, 'Y7179', 18.1, 'I2317001-31', 1468, NULL, NULL, '15:33:08', '15:38', NULL, NULL, '', 18522, 17, 833, 17640, 1, 49, 80, 71),
(74, 'Y7179', 18.1, 'I2058901-31', 1528, NULL, NULL, '15:33:02', '15:37', NULL, NULL, '', 19265, 24, 1176, 18060, 0.6, 29, 78, 70),
(75, 'Y7179', 18.1, 'I2059001-31', 166, NULL, NULL, '15:37:46', '15:41', NULL, NULL, '', 2100, 0, 0, 2100, 0, 0, 78, 70),
(77, 'Y7179', 18.1, 'I2317001-31', 398, NULL, NULL, '15:41:28', '15:45', NULL, NULL, '', 5017, 5, 245, 4772, 0, 0, 80, 71),
(78, 'Y7007', 20, 'B2091101-31', 894, NULL, NULL, '14:32:15', '14:37', NULL, NULL, '', 11653, 10, 628, 11000, 0.4, 25, 82, 72),
(79, 'Y7007', 16, 'B2016201-31', 1145, NULL, NULL, '14:34:35', '14:39', NULL, NULL, '', 16662, 10, 869, 15750, 0.5, 43, 84, 73),
(80, 'Y7007', 20, 'B2091101-31', 894, NULL, NULL, '14:32:15', '14:37', NULL, NULL, '', 9653, 10, 628, 9000, 0.4, 25, 82, 72),
(81, 'Y7007', 20, 'F2183830-31', 818, NULL, NULL, '14:38:16', '14:42', NULL, NULL, '', 10663, 26, 1635, 9000, 0.46, 28, 82, 72),
(82, 'Y7007', 16, 'B2016201-31', 299, NULL, NULL, '14:40:25', '14:44', NULL, NULL, '', 4362, 1, 86, 4250, 0.3, 26, 84, 73),
(86, 'Y7185', 22.9, 'I2213601-31', 74, NULL, NULL, '11:26:07', '11:29', NULL, NULL, '', 1680, 0, 0, 1680, 0, 0, 87, 75),
(87, 'Y7185', 22.9, 'I2213601-31', 867, '', '', '11:33:44', '11:38', NULL, NULL, '', 19594, 7, 648, 18900, 0.5, 46, 87, 75),
(88, 'Y7185', 22.9, 'I2213601-31', 916, '', '', '11:39:42', '11:44', NULL, NULL, '', 20714, 4.5, 416, 20160, 1.5, 138, 87, 76),
(89, 'Y7185', 22.9, 'I2213701-31', 731, NULL, NULL, '13:33:16', '13:38', NULL, NULL, '', 16542, 6, 555, 15960, 0.3, 27, 89, 77),
(90, 'Y7185', 22.9, 'I2213701-31', 731, NULL, NULL, '13:33:16', '13:38', NULL, NULL, '', 6267, 6, 555, 5685, 0.3, 27, 89, 77),
(91, 'Y7185', 22.9, 'I2213701-31', 264, NULL, NULL, '13:39:18', '14:25', NULL, NULL, '', 5978, 3, 277, 5685, 0.183, 16, 89, 77),
(92, 'Y7904', 18.6, 'I2287201-31', 1309, NULL, NULL, '11:38:10', '11:41', NULL, NULL, '', 14315, 8.5, 440, 13860, 0.3, 15, 91, 78),
(93, 'Y7904', 18.6, 'I2287201-31', 1309, NULL, NULL, '11:38:10', '11:41', NULL, NULL, '', 6755, 8.5, 440, 6300, 0.3, 15, 91, 78),
(94, 'Y7904', 18.6, 'I2287201-31', 616, NULL, NULL, '11:42:01', '11:46', NULL, NULL, '', 6739, 8, 414, 6300, 0.5, 25, 91, 78),
(95, 'Y7904', 18.6, 'I2164501-31', 1603, NULL, NULL, '12:32:21', '12:36', NULL, NULL, '', 17524, 13, 673, 16800, 1, 51, 93, 79),
(96, 'Y7904', 18.6, 'I2164501-31', 470, NULL, NULL, '12:37:22', '12:40', NULL, NULL, '', 5138, 3.5, 181, 4932, 0.5, 25, 95, 79),
(97, 'C9447', 14.6, 'H9001301-31', 421, NULL, NULL, '11:53:03', '12:02', NULL, NULL, '', 6239, 1.5, 139, 5635, 5, 465, 97, 80),
(98, 'C9447', 14.6, 'H9001301-31', 466, NULL, NULL, '11:59:34', '12:05', NULL, NULL, '', 6866, 3.5, 325, 6523, 0.2, 18, 99, 81),
(99, 'C9447', 14.6, 'H9088302-31', 699, '', '', '12:06:15', '12:08', NULL, NULL, '', 14215, 4, 372, 13750, 1, 93, 99, 81),
(100, 'C9447', 14.6, 'H9001301-31', 1050, NULL, NULL, '12:03:50', '12:13', NULL, NULL, '', 15574, 10, 995, 14440, 1.5, 139, 97, 80),
(101, 'Y7179', 17.2, 'J2170401-31', 1752, NULL, NULL, '15:45:03', '15:48', NULL, NULL, '', 20919, 17.5, 870, 20000, 1, 49, 102, 82),
(102, 'Y7658', 20.3, 'G2206501-31', 1448, NULL, NULL, '15:45:31', '15:50', NULL, NULL, '', 16098, 28.5, 863, 15129, 3.5, 106, 103, 83),
(103, 'Y7658', 20.3, 'G2206501-31', 1448, NULL, NULL, '15:45:31', '15:50', NULL, NULL, '', 5869, 28.5, 863, 4900, 3.5, 106, 103, 83),
(104, 'Y7658', 20.3, 'G2137201-31', 468, '', '', '15:51:45', '15:54', NULL, NULL, '', 5202, 9, 272, 4900, 1, 30, 103, 83),
(106, 'C9447', 14.6, 'H9001302-31', 1266, NULL, NULL, '12:27:31', '12:32', NULL, NULL, '', 4145, 14, 1302, 2750, 1, 93, 105, 84),
(107, 'C9447', 14.6, 'H9001302-31', 193, NULL, NULL, '12:34:20', '12:38', NULL, NULL, '', 2843, 1, 93, 2750, 0, 0, 105, 84),
(108, 'Y7185', 20.8, 'D2243701-31', 908, NULL, NULL, '15:06:22', '15:09', 'YU', '', '', 21254, 10, 1063, 20160, 0.3, 31, 191, 147),
(109, 'Y7904', 21, 'D2154101-31', 1761, '', '', '15:14:27', '15:22', 'XN', '', '', 19198, 15, 691, 18480, 0.6, 27, 192, 148),
(110, 'Y7904', 21, 'D227380121-31', 197, '', '', '15:25:43', '15:39', 'XT', '', '', 2155, 1, 46, 2100, 0.2, 9, 192, 148),
(111, 'Y7904', 18.8, 'C2564301-32', 255, NULL, NULL, '17:14:16', '17:24', 'WB', '', '', 2791, 1, 51, 2730, 0.2, 10, 196, 150),
(112, 'Y7185', 14.7, 'J2491901-31', 535, NULL, NULL, '08:43:31', '08:57', 'N', '', '', 7794, 6.5, 625, 7150, 0.2, 19, 200, 154),
(113, 'Y7185', 14.7, 'C2490301-31', 487, NULL, NULL, '08:59:00', '09:07', 'Q', '', '', 7086, 2, 192, 6875, 0.2, 19, 200, 154),
(114, 'Y7306', 21.2, 'D2527301-31', 628, '', '', '09:09:33', '09:15', '0', '', '', 6231, 9, 213, 6000, 0.8, 18, 201, 155),
(115, 'Y7658', 20.3, 'E2074401-31', 1022, '', '', '09:16:08', '09:24', '0', '', '', 11369, 16.5, 501, 10850, 0.6, 18, 202, 156),
(116, 'Y7658', 20.3, 'E2439801-31', 702, NULL, NULL, '08:27:31', '08:31', 'L', '', '', 7802, 9, 272, 7500, 1, 30, 207, 159),
(117, 'Y7658', 20.3, 'E2439701-31', 0, NULL, NULL, '08:31:45', '08:33', 'P', '', '', 12827, 7, 212, 12600, 0.5, 15, 207, 159),
(118, 'Y7185', 22.9, 'D2405601-31', 671, '', '', '09:22:40', '09:25', 'WC', '', '', 15180, 5, 462, 14700, 0.2, 18, 208, 160),
(119, 'Y7185', 22.9, 'D2406001-31', 93, '', '', '09:25:42', '09:54', 'WF', '', '', 2109, 0, 0, 2100, 0.1, 9, 208, 160),
(120, 'Y7185', 22.9, 'D2406001-31', 163, '', '', '09:55:00', '09:58', 'WF', '', '', 3693, 3.5, 324, 3360, 0.1, 9, 208, 160),
(121, 'Y7306', 21.2, 'STD 5', 786, '', '', '', '12:28', '5', '', '', 7183, 13, 308, 6840, 1.5, 35, 209, 161),
(122, 'Y7904', 21, 'D2191701-31', 539, '', '', '12:34:12', '12:37', 'WP', '', '', 5877, 4.5, 207, 5670, 0, 0, 210, 162),
(123, 'Y7904', 21, '', 0, '', '', '12:37:50', '12:41', 'WL', '', '', 10369, 6, 276, 10080, 0.3, 13, 210, 162),
(124, 'Y7904', 21, 'D2153701-31', 418, '', '', '12:42:43', '12:46', 'WL', '', '', 4874, 3, 138, 4725, 0.24, 11, 210, 162),
(125, 'C9466', 14.6, 'STD E', 1395, '', '', '', '08:49', 'J', '', '', 20493, 4, 372, 20075, 0.5, 46, 212, 163),
(126, 'C9466', 14.6, 'STD E', 0, '', '', '', '18:29', 'J', '', '', 20660, 6, 558, 20075, 0.3, 27, 213, 165),
(127, 'Y7185', 22.9, 'D2118001-31', 0, '', '', '18:32:29', '18:36', 'W', '', '', 20946, 8, 740, 20160, 0.5, 46, 214, 166),
(128, 'C9466', 14.6, 'C9204301-31', 910, NULL, NULL, '14:05:54', '14:14', 'P', '', '', 13390, 3, 279, 12925, 2, 186, 218, 169),
(129, 'C9466', 14.6, 'C9204301-31', 525, NULL, NULL, '14:16:08', '15:21', 'P', '', '', 7828, 6.5, 604, 7150, 0.8, 74, 218, 169),
(130, 'Y7902', 18.2, 'E2126301-31', 0, '', '', '15:23:55', '15:51', 'H', '', '', 8113, 16, 692, 7400, 0.5, 21, 219, 170),
(131, 'Y7902', 18.2, 'E2126301-31', 188, '', '', '15:53:12', '16:03', 'H', '', '', 2120, 2.5, 108, 2000, 0.3, 12, 219, 170),
(132, 'Y7902', 18.2, 'E2424601-31', 0, '', '', '16:04:08', '16:12', 'H', '', '', 5937, 3, 129, 5800, 0.2, 8, 219, 170),
(133, 'Y7658', 20.3, 'F2035001-31', 0, '', '', '16:15:27', '16:22', 'K', '', '', 15528, 15, 454, 15050, 0.8, 24, 220, 171),
(134, 'Y7658', 20.3, 'E2287101-31', 0, '', '', '16:23:09', '16:28', 'L', '', '', 5396, 10, 303, 5075, 0.6, 18, 221, 171),
(135, 'C9480', 19.4, 'F9186301-31', 1096, NULL, NULL, '13:41:34', '14:21', 'T', '', '', 8990, 6.5, 137, 8760, 4.4, 93, 223, 172),
(136, 'C9480', 19.4, 'F9136101-31', 313, NULL, NULL, '14:21:40', '14:31', 'R', '', '', 2610, 4, 84, 2520, 0.3, 6, 223, 172),
(137, 'Y7007', 18, '', 0, '', '', '14:34:36', '15:12', 'E', '', '', 7019, 5, 294, 6720, 0.1, 5, 224, 173),
(138, 'Y7007', 18, 'E2033301-31', 0, '', '', '15:13:05', '15:15', 'J', '', '', 3488, 2, 117, 3360, 0.2, 11, 224, 173),
(139, 'A6075', 21.7, 'STD 1', 338, '', '', '', '14:25', 'A', '', '', 3254, 8.5, 173, 3000, 4, 81, 226, 174);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_registrer`
--

CREATE TABLE `das_registrer` (
  `iddas_registro` int(11) NOT NULL,
  `empleado_id_empleado` int(10) UNSIGNED DEFAULT NULL,
  `hora` varchar(20) COLLATE utf8_spanish_ci DEFAULT NULL,
  `acumulado` int(11) DEFAULT NULL,
  `piezasxhora` int(11) DEFAULT NULL,
  `ok` int(11) DEFAULT NULL,
  `das_id_das` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `das_registrer`
--

INSERT INTO `das_registrer` (`iddas_registro`, `empleado_id_empleado`, `hora`, `acumulado`, `piezasxhora`, `ok`, `das_id_das`) VALUES
(1, 69, '10:15', 3411, 3411, 1, 176),
(2, 42, '11:50', 4264, 4264, 1, 177),
(3, 42, '12:26', 10738, 6474, 1, 177),
(4, 42, '12:26', 10738, 6474, 1, 177),
(5, 131, '09:41', 148, 148, 1, 181),
(6, 131, '11:08', 9385, 9385, 0, 0),
(7, 131, '12:40', 9533, 9533, 1, 182),
(8, 69, '07:24', 190, 190, 0, 198),
(9, 69, '07:25', 9010, 8820, 1, 198),
(10, 69, '07:25', 9010, 8820, 1, 198),
(11, 69, '07:25', 9010, 8820, 1, 198),
(12, 131, '14:29', 1320, 1320, 1, 203),
(13, 131, '14:29', 1320, 1320, 1, 203),
(14, 131, '14:32', 1320, 0, 0, 203),
(15, 131, '07:46', 1111, 1111, 1, 216),
(16, 131, '07:53', 13693, 12582, 1, 216),
(17, 53, '11:04:45', 300, 300, 1, 230),
(18, 66, '11:05:08', 1500, 1200, 1, 230),
(19, 66, '11:05:29', 3100, 1600, 1, 230),
(20, 66, '11:05:49', 4600, 1500, 1, 230),
(21, 66, '11:06:13', 6500, 1900, 1, 230),
(22, 66, '11:06:25', 8300, 1800, 1, 230),
(23, 66, '11:06:52', 9700, 1400, 1, 230),
(24, 66, '11:07:14', 11500, 1800, 1, 230),
(25, 66, '11:07:31', 13200, 1700, 1, 230),
(26, 66, '11:07:41', 14200, 1000, 1, 230),
(27, 66, '11:07:50', 16200, 2000, 1, 230),
(28, 66, '11:08:11', 17669, 1469, 1, 230),
(29, 58, '12:12:59', 638, 638, 0, 232),
(30, 58, '12:14:15', 9226, 8588, 1, 233),
(31, 58, '12:14:15', 9226, 8588, 1, 233),
(32, 58, '13:11:04', 638, 638, 1, 234),
(33, 51, '10:52:30', 5000, 5000, 1, 235),
(34, 51, '10:52:30', 5000, 5000, 1, 235),
(35, 51, '10:52:51', 5001, 1, 1, 235),
(36, 28, '12:29:34', 20, 20, 1, 236),
(37, 45, '11:54:57', 3212, 3212, 1, 237),
(38, 2, '14:26:26', 66, 66, 0, 253),
(39, 2, '14:26:34', 66, 0, 1, 253),
(40, 2, '14:26:42', 114, 48, 1, 253);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_reg_coiling`
--

CREATE TABLE `das_reg_coiling` (
  `id_das_reg_Coiling` int(11) NOT NULL,
  `empleado_idempleado` int(10) UNSIGNED NOT NULL,
  `hora` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ok` int(11) NOT NULL,
  `das_iddas` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_reg_prensa`
--

CREATE TABLE `das_reg_prensa` (
  `id_regprensa` int(11) NOT NULL,
  `hora` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ok` tinyint(1) NOT NULL,
  `empleado_idempleado` int(10) UNSIGNED NOT NULL,
  `das_iddas` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_reg_slitter`
--

CREATE TABLE `das_reg_slitter` (
  `id_das_reg_Slitter` int(11) NOT NULL,
  `empleado_idempleado` int(10) UNSIGNED NOT NULL,
  `hora` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ok` int(11) NOT NULL,
  `das_iddas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_slitter`
--

CREATE TABLE `das_slitter` (
  `id_das_slitter` int(11) NOT NULL,
  `no_tiras` int(11) NOT NULL,
  `ajuste_inicio` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ajuste_final` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `proces_inicio` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `proces_final` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `amarre_inicio` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `amarre_fin` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `mtr_procesados` double NOT NULL,
  `mtrs_ng` double NOT NULL,
  `cantidad_mb` double NOT NULL,
  `scrap` double NOT NULL,
  `das_id_das` int(10) NOT NULL,
  `ordenSlitter_idordenS` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `defecto1`
--

CREATE TABLE `defecto1` (
  `id_defecto` int(11) NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `razon_rechazo_id_razon_rechazo` int(10) UNSIGNED NOT NULL,
  `cantidad_defecto` int(11) NOT NULL,
  `columna` int(11) NOT NULL,
  `columna_sorting` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `defecto1`
--

INSERT INTO `defecto1` (`id_defecto`, `registro_rbp_id_registro_rbp`, `razon_rechazo_id_razon_rechazo`, `cantidad_defecto`, `columna`, `columna_sorting`) VALUES
(1, 31, 18, 9, 2, NULL),
(2, 31, 19, 4, 2, NULL),
(3, 31, 27, 10, 2, NULL),
(4, 31, 29, 7, 2, NULL),
(5, 31, 9, 3, 4, NULL),
(6, 31, 16, 9, 4, NULL),
(7, 31, 23, 5, 4, NULL),
(8, 31, 29, 13, 4, NULL),
(9, 32, 29, 8, 2, NULL),
(10, 32, 24, 4, 3, NULL),
(11, 32, 27, 2, 3, NULL),
(12, 32, 29, 12, 3, NULL),
(13, 33, 29, 12, 2, NULL),
(14, 34, 18, 15, 2, NULL),
(15, 34, 19, 27, 2, NULL),
(16, 34, 29, 19, 2, NULL),
(17, 34, 30, 35, 2, NULL),
(18, 33, 19, 5, 3, NULL),
(19, 33, 23, 5, 3, NULL),
(20, 33, 29, 8, 3, NULL),
(21, 35, 18, 15, 2, NULL),
(22, 35, 19, 27, 2, NULL),
(23, 35, 29, 19, 2, NULL),
(24, 35, 30, 35, 2, NULL),
(25, 37, 15, 10, 2, NULL),
(26, 37, 29, 4, 2, NULL),
(27, 36, 15, 6, 2, NULL),
(28, 36, 19, 9, 2, NULL),
(29, 36, 23, 5, 2, NULL),
(30, 37, 9, 3, 3, NULL),
(31, 37, 20, 6, 3, NULL),
(32, 37, 29, 16, 3, NULL),
(33, 37, 30, 10, 3, NULL),
(34, 36, 15, 10, 3, NULL),
(35, 36, 19, 14, 3, NULL),
(36, 36, 23, 11, 3, NULL),
(37, 36, 15, 3, 4, NULL),
(38, 36, 19, 24, 4, NULL),
(39, 36, 23, 8, 4, NULL),
(40, 39, 19, 13, 2, NULL),
(41, 39, 27, 3, 2, NULL),
(42, 39, 29, 7, 2, NULL),
(43, 40, 27, 7, 2, NULL),
(44, 40, 29, 20, 2, NULL),
(45, 41, 29, 23, 2, NULL),
(46, 39, 19, 38, 3, NULL),
(47, 39, 29, 8, 3, NULL),
(48, 39, 19, 19, 4, NULL),
(49, 39, 27, 8, 4, NULL),
(50, 39, 29, 5, 4, NULL),
(51, 44, 13, 3, 2, NULL),
(52, 44, 27, 4, 2, NULL),
(53, 44, 29, 14, 2, NULL),
(54, 43, 18, 22, 2, NULL),
(55, 43, 28, 70, 2, NULL),
(56, 43, 29, 20, 2, NULL),
(57, 44, 13, 12, 3, NULL),
(58, 44, 27, 5, 3, NULL),
(59, 44, 29, 6, 3, NULL),
(60, 46, 19, 14, 2, NULL),
(61, 46, 20, 22, 2, NULL),
(62, 46, 27, 8, 2, NULL),
(63, 46, 29, 25, 2, NULL),
(64, 45, 27, 7, 2, NULL),
(65, 45, 29, 16, 2, NULL),
(66, 45, 18, 10, 3, NULL),
(67, 45, 20, 34, 3, NULL),
(68, 45, 27, 9, 3, NULL),
(69, 45, 29, 4, 3, NULL),
(70, 47, 23, 90, 2, NULL),
(71, 47, 29, 13, 2, NULL),
(72, 47, 30, 103, 2, NULL),
(73, 47, 17, 45, 3, NULL),
(74, 47, 18, 12, 3, NULL),
(75, 47, 19, 6, 3, NULL),
(76, 47, 20, 8, 3, NULL),
(77, 47, 29, 7, 3, NULL),
(78, 48, 12, 15, 2, NULL),
(79, 48, 19, 4, 2, NULL),
(80, 48, 29, 21, 2, NULL),
(81, 50, 27, 1, 2, NULL),
(82, 50, 29, 6, 2, NULL),
(83, 51, 12, 67, 2, NULL),
(84, 51, 29, 15, 2, NULL),
(85, 51, 5, 32, 3, NULL),
(86, 51, 13, 3, 3, NULL),
(87, 51, 19, 4, 3, NULL),
(88, 51, 29, 5, 3, NULL),
(89, 52, 5, 50, 2, NULL),
(90, 52, 13, 8, 2, NULL),
(91, 52, 19, 5, 2, NULL),
(92, 52, 29, 13, 2, NULL),
(93, 52, 13, 10, 3, NULL),
(94, 52, 19, 14, 3, NULL),
(95, 52, 21, 6, 3, NULL),
(96, 52, 29, 8, 3, NULL),
(97, 53, 19, 5, 2, NULL),
(98, 53, 29, 20, 2, NULL),
(99, 53, 16, 15, 3, NULL),
(100, 53, 29, 10, 3, NULL),
(101, 54, 20, 4, 2, NULL),
(102, 54, 29, 3, 2, NULL),
(103, 54, 29, 3, 3, NULL),
(104, 54, 18, 2, 4, NULL),
(105, 54, 21, 4, 4, NULL),
(106, 54, 27, 1, 4, NULL),
(107, 54, 29, 3, 4, NULL),
(108, 54, 17, 10, 5, NULL),
(109, 54, 24, 5, 5, NULL),
(110, 54, 29, 10, 5, NULL),
(111, 54, 29, 10, 6, NULL),
(112, 56, 13, 5, 2, NULL),
(113, 56, 24, 3, 2, NULL),
(114, 56, 27, 5, 2, NULL),
(115, 56, 29, 10, 2, NULL),
(116, 56, 14, 13, 3, NULL),
(117, 56, 23, 16, 3, NULL),
(118, 56, 24, 25, 3, NULL),
(119, 56, 29, 15, 3, NULL),
(120, 57, 18, 15, 2, NULL),
(121, 57, 19, 23, 2, NULL),
(122, 57, 29, 20, 2, NULL),
(123, 57, 30, 35, 2, NULL),
(124, 59, 27, 3, 2, NULL),
(125, 59, 29, 7, 2, NULL),
(126, 58, 9, 15, 2, NULL),
(127, 58, 18, 10, 2, NULL),
(128, 58, 24, 12, 2, NULL),
(129, 58, 29, 9, 2, NULL),
(130, 58, 18, 4, 3, NULL),
(131, 58, 20, 8, 3, NULL),
(132, 58, 29, 11, 3, NULL),
(133, 59, 8, 8, 3, NULL),
(134, 59, 29, 20, 3, NULL),
(135, 58, 18, 0, 4, NULL),
(136, 58, 29, 5, 4, NULL),
(137, 60, 18, 6, 2, NULL),
(138, 60, 21, 6, 2, NULL),
(139, 60, 29, 9, 2, NULL),
(140, 60, 12, 79, 3, NULL),
(141, 60, 26, 32, 3, NULL),
(142, 60, 29, 16, 3, NULL),
(143, 61, 19, 5, 2, NULL),
(144, 61, 24, 8, 2, NULL),
(145, 61, 27, 10, 2, NULL),
(146, 61, 29, 30, 2, NULL),
(147, 62, 13, 2, 2, NULL),
(148, 62, 28, 4, 2, NULL),
(149, 62, 30, 20, 2, NULL),
(150, 63, 19, 5, 2, NULL),
(151, 63, 20, 2, 2, NULL),
(152, 63, 27, 5, 2, NULL),
(153, 63, 29, 25, 2, NULL),
(154, 64, 15, 19, 2, NULL),
(155, 64, 23, 22, 2, NULL),
(156, 64, 30, 49, 2, NULL),
(157, 65, 29, 14, 2, NULL),
(158, 65, 30, 9, 2, NULL),
(159, 65, 18, 9, 3, NULL),
(160, 65, 29, 3, 3, NULL),
(161, 65, 30, 11, 3, NULL),
(162, 66, 13, 9, 2, NULL),
(163, 66, 14, 4, 2, NULL),
(164, 66, 18, 0, 2, NULL),
(165, 66, 19, 0, 2, NULL),
(166, 66, 28, 25, 2, NULL),
(167, 66, 29, 17, 2, NULL),
(168, 66, 18, 22, 3, NULL),
(169, 66, 19, 15, 3, NULL),
(170, 69, 18, 40, 2, NULL),
(171, 69, 20, 29, 2, NULL),
(172, 69, 23, 20, 2, NULL),
(173, 69, 28, 40, 2, NULL),
(174, 68, 20, 4, 2, NULL),
(175, 68, 29, 20, 2, NULL),
(176, 68, 20, 6, 3, NULL),
(177, 68, 27, 6, 3, NULL),
(178, 68, 29, 2, 3, NULL),
(179, 70, 19, 6, 2, NULL),
(180, 70, 23, 5, 2, NULL),
(181, 70, 29, 18, 2, NULL),
(182, 71, 19, 24, 2, NULL),
(183, 71, 27, 8, 2, NULL),
(184, 71, 29, 17, 2, NULL),
(185, 71, 30, 0, 2, NULL),
(186, 70, 29, 2, 3, NULL),
(187, 71, 23, 2, 3, NULL),
(188, 71, 29, 4, 3, NULL),
(189, 72, 27, 11, 2, NULL),
(190, 72, 29, 14, 2, NULL),
(191, 73, 17, 9, 2, NULL),
(192, 73, 18, 10, 2, NULL),
(193, 73, 20, 10, 2, NULL),
(194, 73, 23, 2, 2, NULL),
(195, 73, 29, 12, 2, NULL),
(196, 72, 13, 5, 3, NULL),
(197, 72, 14, 2, 3, NULL),
(198, 72, 17, 5, 3, NULL),
(199, 72, 19, 5, 3, NULL),
(200, 72, 20, 3, 3, NULL),
(201, 72, 29, 9, 3, NULL),
(202, 73, 20, 10, 3, NULL),
(203, 73, 27, 8, 3, NULL),
(204, 73, 29, 8, 3, NULL),
(207, 75, 29, 1, 2, NULL),
(208, 75, 30, 2, 2, NULL),
(209, 75, 13, 3, 3, NULL),
(210, 75, 17, 4, 3, NULL),
(211, 75, 19, 10, 3, NULL),
(212, 75, 27, 5, 3, NULL),
(213, 75, 29, 24, 3, NULL),
(214, 76, 13, 10, 2, NULL),
(215, 76, 17, 21, 2, NULL),
(216, 76, 18, 15, 2, NULL),
(217, 76, 19, 40, 2, NULL),
(218, 76, 21, 10, 2, NULL),
(219, 76, 29, 22, 2, NULL),
(220, 76, 30, 20, 2, NULL),
(221, 77, 21, 11, 2, NULL),
(222, 77, 29, 16, 2, NULL),
(223, 77, 20, 4, 3, NULL),
(224, 77, 24, 3, 3, NULL),
(225, 77, 27, 4, 3, NULL),
(226, 77, 29, 5, 3, NULL),
(227, 77, 30, 1, 3, NULL),
(228, 78, 23, 1, 2, NULL),
(229, 78, 29, 14, 2, NULL),
(230, 78, 19, 12, 3, NULL),
(231, 78, 27, 5, 3, NULL),
(232, 78, 29, 8, 3, NULL),
(233, 79, 18, 6, 2, NULL),
(234, 79, 19, 19, 2, NULL),
(235, 79, 27, 8, 2, NULL),
(236, 79, 29, 18, 2, NULL),
(237, 79, 29, 4, 3, NULL),
(238, 79, 30, 21, 3, NULL),
(239, 80, 13, 50, 2, NULL),
(240, 80, 17, 20, 2, NULL),
(241, 80, 19, 20, 2, NULL),
(242, 80, 20, 13, 2, NULL),
(243, 80, 24, 296, 2, NULL),
(244, 80, 27, 10, 2, NULL),
(245, 80, 29, 4, 2, NULL),
(246, 81, 13, 9, 2, NULL),
(247, 81, 29, 7, 2, NULL),
(248, 80, 13, 12, 3, NULL),
(249, 80, 17, 14, 3, NULL),
(250, 80, 19, 5, 3, NULL),
(251, 80, 20, 14, 3, NULL),
(252, 80, 25, 65, 3, NULL),
(253, 80, 29, 17, 3, NULL),
(254, 81, 13, 11, 3, NULL),
(255, 81, 17, 33, 3, NULL),
(256, 81, 18, 12, 3, NULL),
(257, 81, 19, 12, 3, NULL),
(258, 81, 27, 4, 3, NULL),
(259, 81, 29, 10, 3, NULL),
(260, 82, 9, 8, 2, NULL),
(261, 82, 18, 8, 2, NULL),
(262, 82, 19, 13, 2, NULL),
(263, 82, 29, 20, 2, NULL),
(264, 83, 5, 45, 2, NULL),
(265, 83, 19, 15, 2, NULL),
(266, 83, 28, 30, 2, NULL),
(267, 83, 29, 15, 2, NULL),
(268, 83, 24, 4, 3, NULL),
(269, 83, 28, 20, 3, NULL),
(270, 83, 29, 6, 3, NULL),
(271, 84, 18, 64, 2, NULL),
(272, 84, 29, 18, 2, NULL),
(273, 84, 27, 4, 3, NULL),
(274, 84, 29, 4, 3, NULL),
(275, 85, 19, 4, 2, NULL),
(276, 85, 28, 55, 2, NULL),
(277, 85, 29, 4, 2, NULL),
(278, 85, 19, 3, 3, NULL),
(279, 85, 23, 5, 3, NULL),
(280, 85, 27, 7, 3, NULL),
(281, 85, 29, 16, 3, NULL),
(282, 86, 28, 9, 2, NULL),
(283, 86, 29, 20, 2, NULL),
(284, 87, 19, 3, 2, NULL),
(285, 87, 21, 6, 2, NULL),
(286, 87, 29, 10, 2, NULL),
(287, 87, 30, 32, 2, NULL),
(288, 87, 18, 11, 3, NULL),
(289, 87, 29, 12, 3, NULL),
(290, 88, 13, 2, 2, NULL),
(291, 88, 19, 7, 2, NULL),
(292, 88, 20, 3, 2, NULL),
(293, 88, 27, 7, 2, NULL),
(294, 88, 29, 5, 2, NULL),
(295, 88, 27, 2, 3, NULL),
(296, 88, 29, 15, 3, NULL),
(297, 89, 19, 13, 2, NULL),
(298, 89, 29, 10, 2, NULL),
(299, 89, 19, 3, 3, NULL),
(300, 89, 27, 9, 3, NULL),
(301, 89, 29, 11, 3, NULL),
(302, 90, 29, 10, 2, NULL),
(303, 90, 13, 2, 3, NULL),
(304, 90, 19, 1, 3, NULL),
(305, 90, 28, 39, 3, NULL),
(306, 90, 29, 10, 3, NULL),
(307, 90, 30, 3, 3, NULL),
(308, 91, 27, 6, 2, NULL),
(309, 91, 29, 3, 2, NULL),
(310, 91, 21, 10, 3, NULL),
(311, 91, 29, 17, 3, NULL),
(312, 92, 13, 20, 2, NULL),
(313, 92, 17, 20, 2, NULL),
(314, 92, 18, 25, 2, NULL),
(315, 92, 24, 9, 2, NULL),
(316, 92, 28, 60, 2, NULL),
(317, 92, 29, 10, 2, NULL),
(318, 92, 30, 30, 2, NULL),
(319, 92, 15, 18, 3, NULL),
(320, 92, 23, 14, 3, NULL),
(321, 92, 30, 72, 3, NULL),
(322, 93, 18, 34, 2, NULL),
(323, 93, 28, 48, 2, NULL),
(324, 93, 30, 35, 2, NULL),
(325, 94, 13, 13, 2, NULL),
(326, 94, 14, 9, 2, NULL),
(327, 94, 20, 0, 2, NULL),
(328, 94, 27, 5, 2, NULL),
(329, 94, 29, 18, 2, NULL),
(330, 94, 20, 13, 3, NULL),
(331, 94, 29, 2, 3, NULL),
(332, 95, 23, 7, 2, NULL),
(333, 95, 29, 20, 2, NULL),
(334, 96, 15, 12, 2, NULL),
(335, 96, 23, 13, 2, NULL),
(336, 96, 30, 79, 2, NULL),
(337, 97, 29, 5, 2, NULL),
(338, 97, 15, 7, 3, NULL),
(339, 97, 23, 5, 3, NULL),
(340, 97, 30, 32, 3, NULL),
(341, 97, 13, 3, 4, NULL),
(342, 97, 19, 6, 4, NULL),
(343, 97, 20, 4, 4, NULL),
(344, 97, 29, 15, 4, NULL),
(345, 97, 30, 26, 4, NULL),
(346, 99, 20, 33, 2, NULL),
(347, 99, 29, 18, 2, NULL),
(348, 99, 13, 10, 3, NULL),
(349, 99, 29, 2, 3, NULL),
(350, 100, 20, 25, 2, NULL),
(351, 100, 29, 7, 3, NULL),
(352, 100, 20, 3, 4, NULL),
(353, 100, 29, 5, 4, NULL),
(354, 101, 23, 5, 2, NULL),
(355, 101, 27, 18, 3, NULL),
(356, 101, 29, 10, 3, NULL),
(357, 102, 14, 14, 2, NULL),
(358, 102, 17, 3, 2, NULL),
(359, 102, 20, 1, 2, NULL),
(360, 102, 23, 7, 2, NULL),
(361, 102, 24, 4, 2, NULL),
(362, 102, 29, 14, 2, NULL),
(363, 104, 13, 7, 2, NULL),
(364, 104, 20, 18, 2, NULL),
(365, 104, 27, 4, 2, NULL),
(366, 104, 29, 15, 2, NULL),
(367, 103, 25, 10, 2, NULL),
(368, 103, 28, 50, 2, NULL),
(369, 103, 29, 18, 2, NULL),
(370, 103, 27, 2, 3, NULL),
(371, 103, 29, 13, 3, NULL),
(372, 105, 13, 6, 2, NULL),
(373, 105, 19, 20, 2, NULL),
(374, 105, 20, 18, 2, NULL),
(375, 105, 30, 25, 2, NULL),
(376, 105, 23, 2, 3, NULL),
(377, 105, 29, 21, 3, NULL),
(378, 106, 20, 12, 2, NULL),
(379, 106, 21, 23, 2, NULL),
(380, 107, 13, 5, 2, NULL),
(381, 107, 18, 2, 2, NULL),
(382, 107, 19, 1, 2, NULL),
(383, 107, 29, 8, 2, NULL),
(384, 107, 30, 4, 2, NULL),
(385, 107, 13, 9, 3, NULL),
(386, 107, 19, 11, 3, NULL),
(387, 107, 20, 16, 3, NULL),
(388, 107, 30, 13, 3, NULL),
(389, 108, 18, 7, 2, NULL),
(390, 108, 19, 10, 2, NULL),
(391, 108, 20, 15, 2, NULL),
(392, 108, 29, 20, 2, NULL),
(393, 109, 17, 5, 2, NULL),
(394, 109, 29, 10, 2, NULL),
(395, 109, 20, 7, 3, NULL),
(396, 109, 29, 5, 3, NULL),
(397, 111, 9, 3, 2, NULL),
(398, 111, 14, 15, 2, NULL),
(399, 111, 29, 11, 2, NULL),
(400, 112, 20, 9, 2, NULL),
(401, 112, 29, 8, 2, NULL),
(402, 112, 9, 8, 3, NULL),
(403, 112, 20, 15, 3, NULL),
(404, 112, 24, 25, 3, NULL),
(405, 112, 28, 30, 3, NULL),
(406, 112, 29, 15, 3, NULL),
(407, 111, 9, 20, 3, NULL),
(408, 111, 25, 21, 3, NULL),
(409, 111, 27, 6, 3, NULL),
(410, 111, 29, 9, 3, NULL),
(411, 110, 27, 4, 2, NULL),
(412, 110, 29, 10, 2, NULL),
(413, 113, 29, 4, 2, NULL),
(414, 114, 13, 2, 2, NULL),
(415, 114, 14, 4, 2, NULL),
(416, 114, 18, 1, 2, NULL),
(417, 114, 20, 1, 2, NULL),
(418, 114, 29, 2, 2, NULL),
(419, 115, 14, 8, 2, NULL),
(420, 115, 28, 5, 2, NULL),
(421, 115, 29, 1, 2, NULL),
(422, 116, 2, 5, 2, NULL),
(423, 116, 13, 0, 2, NULL),
(424, 116, 14, 8, 2, NULL),
(425, 116, 18, 6, 2, NULL),
(426, 116, 20, 8, 2, NULL),
(427, 116, 23, 10, 2, NULL),
(428, 116, 29, 23, 2, NULL),
(429, 117, 13, 12, 2, NULL),
(430, 117, 24, 13, 2, NULL),
(431, 117, 29, 15, 2, NULL),
(432, 118, 14, 10, 2, NULL),
(433, 118, 18, 5, 2, NULL),
(434, 118, 23, 18, 2, NULL),
(435, 118, 27, 5, 2, NULL),
(436, 118, 29, 5, 2, NULL),
(437, 118, 2, 5, 3, NULL),
(438, 118, 23, 6, 3, NULL),
(439, 118, 29, 2, 3, NULL),
(440, 119, 13, 7, 2, NULL),
(441, 119, 25, 6, 2, NULL),
(442, 119, 28, 6, 2, NULL),
(443, 119, 29, 2, 2, NULL),
(444, 119, 4, 0, 3, NULL),
(445, 119, 13, 4, 3, NULL),
(446, 119, 21, 5, 3, NULL),
(447, 119, 26, 2, 3, NULL),
(448, 119, 29, 12, 3, NULL),
(449, 119, 30, 3, 3, NULL),
(450, 120, 13, 10, 2, NULL),
(451, 120, 14, 21, 2, NULL),
(452, 120, 17, 7, 2, NULL),
(453, 120, 18, 15, 2, NULL),
(454, 120, 27, 9, 2, NULL),
(455, 120, 29, 20, 2, NULL),
(456, 121, 14, 8, 2, NULL),
(457, 121, 14, 2, 3, NULL),
(458, 121, 27, 5, 3, NULL),
(459, 121, 29, 17, 3, NULL),
(460, 122, 13, 2, 2, NULL),
(461, 122, 18, 3, 2, NULL),
(462, 122, 29, 6, 2, NULL),
(463, 122, 13, 2, 3, NULL),
(464, 122, 14, 6, 3, NULL),
(465, 122, 20, 4, 3, NULL),
(466, 122, 29, 14, 3, NULL),
(467, 123, 14, 53, 2, NULL),
(468, 123, 29, 10, 2, NULL),
(469, 123, 30, 32, 2, NULL),
(470, 123, 14, 74, 3, NULL),
(471, 123, 29, 14, 3, NULL),
(472, 123, 30, 28, 3, NULL),
(473, 123, 29, 1, 4, NULL),
(474, 124, 14, 27, 2, NULL),
(475, 124, 29, 13, 2, NULL),
(476, 124, 14, 13, 3, NULL),
(477, 124, 29, 7, 3, NULL),
(478, 125, 29, 12, 2, NULL),
(479, 125, 30, 18, 2, NULL),
(480, 125, 13, 15, 3, NULL),
(481, 125, 14, 170, 3, NULL),
(482, 125, 17, 14, 3, NULL),
(483, 125, 18, 20, 3, NULL),
(484, 125, 20, 10, 3, NULL),
(485, 125, 27, 10, 3, NULL),
(486, 125, 29, 8, 3, NULL),
(487, 126, 9, 142, 2, NULL),
(488, 126, 29, 6, 2, NULL),
(489, 126, 14, 80, 3, NULL),
(490, 126, 20, 20, 3, NULL),
(491, 126, 24, 15, 3, NULL),
(492, 126, 29, 4, 3, NULL),
(493, 126, 30, 12, 3, NULL),
(494, 128, 43, 2, 2, NULL),
(495, 128, 45, 12, 2, NULL),
(496, 128, 47, 6, 2, NULL),
(497, 128, 63, 10, 2, NULL),
(498, 129, 36, 15, 2, NULL),
(499, 129, 42, 10, 2, NULL),
(500, 129, 61, 10, 2, NULL),
(501, 129, 62, 5, 2, NULL),
(502, 129, 14, 25, 3, NULL),
(503, 129, 32, 12, 3, NULL),
(504, 129, 34, 8, 3, NULL),
(505, 129, 41, 14, 3, NULL),
(506, 129, 42, 4, 3, NULL),
(507, 129, 43, 8, 3, NULL),
(508, 129, 44, 12, 3, NULL),
(509, 129, 55, 28, 3, NULL),
(510, 129, 57, 25, 3, NULL),
(511, 129, 58, 24, 3, NULL),
(512, 129, 61, 10, 3, NULL),
(513, 129, 63, 10, 3, NULL),
(514, 131, 36, 1, 2, NULL),
(515, 131, 37, 1, 2, NULL),
(516, 131, 41, 5, 2, NULL),
(517, 131, 42, 1, 2, NULL),
(518, 131, 44, 3, 2, NULL),
(519, 131, 45, 11, 2, NULL),
(520, 131, 49, 2, 2, NULL),
(521, 131, 59, 3, 2, NULL),
(522, 131, 61, 7, 2, NULL),
(523, 131, 32, 3, 3, NULL),
(524, 131, 33, 2, 3, NULL),
(525, 131, 37, 4, 3, NULL),
(526, 131, 41, 9, 3, NULL),
(527, 131, 42, 3, 3, NULL),
(528, 131, 43, 10, 3, NULL),
(529, 131, 44, 12, 3, NULL),
(530, 131, 45, 39, 3, NULL),
(531, 131, 46, 2, 3, NULL),
(532, 131, 49, 3, 3, NULL),
(533, 131, 50, 3, 3, NULL),
(534, 131, 51, 13, 3, NULL),
(535, 131, 61, 4, 3, NULL),
(536, 131, 63, 8, 3, NULL),
(537, 131, 63, 380, 4, NULL),
(538, 132, 57, 36, 3, NULL),
(539, 132, 62, 150, 3, NULL),
(540, 134, 35, 100, 2, NULL),
(541, 134, 61, 17, 2, NULL),
(542, 134, 63, 30, 2, NULL),
(543, 135, 32, 4, 2, NULL),
(544, 135, 51, 200, 3, NULL),
(545, 135, 61, 20, 3, NULL),
(546, 139, 35, 5, 2, NULL),
(547, 139, 41, 7, 2, NULL),
(548, 139, 42, 1, 2, NULL),
(549, 139, 63, 7, 2, NULL),
(550, 139, 41, 7, 3, NULL),
(551, 139, 45, 53, 3, NULL),
(552, 139, 51, 31, 3, NULL),
(553, 139, 59, 30, 3, NULL),
(554, 139, 32, 6, 4, NULL),
(555, 139, 33, 6, 4, NULL),
(556, 139, 35, 2, 4, NULL),
(557, 139, 41, 14, 4, NULL),
(558, 139, 42, 13, 4, NULL),
(559, 139, 43, 8, 4, NULL),
(560, 139, 44, 13, 4, NULL),
(561, 139, 45, 42, 4, NULL),
(562, 139, 46, 1, 4, NULL),
(563, 139, 47, 10, 4, NULL),
(564, 139, 51, 34, 4, NULL),
(565, 139, 59, 19, 4, NULL),
(566, 139, 63, 25, 4, NULL),
(567, 141, 38, 519, 2, NULL),
(568, 142, 39, 30, 2, NULL),
(569, 142, 45, 5, 3, NULL),
(570, 142, 55, 179, 3, NULL),
(571, 142, 58, 4, 3, NULL),
(572, 143, 45, 49, 2, NULL),
(573, 143, 61, 120, 3, NULL),
(574, 143, 41, 43, 4, NULL),
(575, 144, 55, 94, 2, NULL),
(576, 147, 29, 21, 2, NULL),
(577, 147, 30, 10, 2, NULL),
(578, 148, 14, 9, 2, NULL),
(579, 148, 29, 18, 2, NULL),
(580, 148, 21, 7, 3, NULL),
(581, 148, 29, 2, 3, NULL),
(582, 149, 27, 2, 2, NULL),
(583, 149, 29, 7, 2, NULL),
(584, 149, 27, 2, 2, NULL),
(585, 149, 29, 7, 2, NULL),
(586, 149, 27, 2, 2, NULL),
(587, 149, 29, 7, 2, NULL),
(588, 149, 27, 2, 2, NULL),
(589, 149, 29, 7, 2, NULL),
(590, 149, 27, 2, 2, NULL),
(591, 149, 29, 7, 2, NULL),
(592, 149, 27, 2, 2, NULL),
(593, 149, 29, 7, 2, NULL),
(594, 149, 27, 2, 2, NULL),
(595, 149, 29, 7, 2, NULL),
(596, 149, 27, 2, 2, NULL),
(597, 149, 29, 7, 2, NULL),
(598, 149, 27, 2, 2, NULL),
(599, 149, 29, 7, 2, NULL),
(600, 149, 27, 2, 2, NULL),
(601, 149, 29, 7, 2, NULL),
(602, 149, 27, 2, 2, NULL),
(603, 149, 29, 7, 2, NULL),
(604, 149, 27, 2, 2, NULL),
(605, 149, 29, 7, 2, NULL),
(606, 149, 27, 2, 2, NULL),
(607, 149, 29, 7, 2, NULL),
(608, 149, 27, 2, 2, NULL),
(609, 149, 29, 7, 2, NULL),
(610, 149, 27, 2, 2, NULL),
(611, 149, 29, 7, 2, NULL),
(612, 149, 27, 2, 2, NULL),
(613, 149, 29, 7, 2, NULL),
(614, 149, 27, 2, 2, NULL),
(615, 149, 29, 7, 2, NULL),
(616, 149, 27, 2, 2, NULL),
(617, 149, 29, 7, 2, NULL),
(618, 149, 27, 2, 2, NULL),
(619, 149, 29, 7, 2, NULL),
(620, 149, 27, 2, 2, NULL),
(621, 149, 29, 7, 2, NULL),
(622, 149, 27, 2, 2, NULL),
(623, 149, 29, 7, 2, NULL),
(624, 149, 27, 2, 2, NULL),
(625, 149, 29, 7, 2, NULL),
(626, 149, 27, 2, 2, NULL),
(627, 149, 29, 7, 2, NULL),
(628, 149, 27, 2, 2, NULL),
(629, 149, 29, 7, 2, NULL),
(630, 149, 27, 2, 2, NULL),
(631, 149, 29, 7, 2, NULL),
(632, 149, 27, 2, 2, NULL),
(633, 149, 29, 7, 2, NULL),
(634, 149, 27, 2, 2, NULL),
(635, 149, 29, 7, 2, NULL),
(636, 149, 27, 2, 2, NULL),
(637, 149, 29, 7, 2, NULL),
(638, 149, 27, 2, 2, NULL),
(639, 149, 29, 7, 2, NULL),
(640, 149, 27, 2, 2, NULL),
(641, 149, 29, 7, 2, NULL),
(642, 149, 27, 2, 2, NULL),
(643, 149, 29, 7, 2, NULL),
(644, 149, 27, 2, 2, NULL),
(645, 149, 29, 7, 2, NULL),
(646, 149, 27, 2, 2, NULL),
(647, 149, 29, 7, 2, NULL),
(648, 149, 27, 2, 2, NULL),
(649, 149, 29, 7, 2, NULL),
(650, 149, 27, 2, 2, NULL),
(651, 149, 29, 7, 2, NULL),
(652, 149, 27, 2, 2, NULL),
(653, 149, 29, 7, 2, NULL),
(654, 149, 27, 2, 2, NULL),
(655, 149, 29, 7, 2, NULL),
(656, 149, 27, 2, 2, NULL),
(657, 149, 29, 7, 2, NULL),
(658, 150, 18, 5, 2, NULL),
(659, 150, 29, 3, 2, NULL),
(660, 150, 30, 2, 2, NULL),
(661, 152, 41, 6, 2, NULL),
(662, 152, 45, 3, 2, NULL),
(663, 152, 52, 45, 2, NULL),
(664, 152, 57, 5, 2, NULL),
(665, 152, 59, 1, 2, NULL),
(666, 152, 61, 2, 2, NULL),
(667, 152, 52, 50, 3, NULL),
(668, 152, 61, 98, 3, NULL),
(669, 152, 63, 35, 3, NULL),
(670, 154, 18, 5, 2, NULL),
(671, 154, 20, 5, 2, NULL),
(672, 154, 27, 4, 2, NULL),
(673, 154, 29, 5, 2, NULL),
(674, 154, 2, 4, 3, NULL),
(675, 154, 3, 7, 3, NULL),
(676, 154, 29, 8, 3, NULL),
(677, 155, 18, 4, 2, NULL),
(678, 155, 20, 6, 2, NULL),
(679, 155, 23, 3, 2, NULL),
(680, 155, 29, 6, 2, NULL),
(681, 156, 13, 4, 2, NULL),
(682, 156, 20, 5, 2, NULL),
(683, 156, 29, 10, 2, NULL),
(684, 156, 8, 0, 3, NULL),
(685, 156, 10, 1, 3, NULL),
(686, 156, 29, 10, 3, NULL),
(687, 156, 8, 0, 3, NULL),
(688, 156, 10, 1, 3, NULL),
(689, 156, 29, 10, 3, NULL),
(690, 156, 8, 0, 3, NULL),
(691, 156, 10, 1, 3, NULL),
(692, 156, 29, 10, 3, NULL),
(693, 156, 8, 0, 3, NULL),
(694, 156, 10, 1, 3, NULL),
(695, 156, 29, 10, 3, NULL),
(696, 156, 8, 0, 3, NULL),
(697, 156, 10, 1, 3, NULL),
(698, 156, 29, 10, 3, NULL),
(699, 156, 8, 0, 3, NULL),
(700, 156, 10, 1, 3, NULL),
(701, 156, 29, 10, 3, NULL),
(702, 156, 8, 0, 3, NULL),
(703, 156, 10, 1, 3, NULL),
(704, 156, 29, 10, 3, NULL),
(705, 156, 8, 0, 3, NULL),
(706, 156, 10, 1, 3, NULL),
(707, 156, 24, 0, 3, NULL),
(708, 156, 29, 10, 3, NULL),
(709, 156, 8, 0, 3, NULL),
(710, 156, 10, 1, 3, NULL),
(711, 156, 24, 0, 3, NULL),
(712, 156, 29, 10, 3, NULL),
(713, 156, 8, 0, 3, NULL),
(714, 156, 10, 1, 3, NULL),
(715, 156, 24, 0, 3, NULL),
(716, 156, 29, 10, 3, NULL),
(717, 156, 8, 0, 3, NULL),
(718, 156, 10, 1, 3, NULL),
(719, 156, 24, 0, 3, NULL),
(720, 156, 29, 10, 3, NULL),
(721, 156, 8, 0, 3, NULL),
(722, 156, 10, 1, 3, NULL),
(723, 156, 24, 0, 3, NULL),
(724, 156, 29, 10, 3, NULL),
(725, 156, 8, 0, 3, NULL),
(726, 156, 10, 1, 3, NULL),
(727, 156, 24, 0, 3, NULL),
(728, 156, 29, 10, 3, NULL),
(729, 156, 8, 0, 3, NULL),
(730, 156, 10, 1, 3, NULL),
(731, 156, 24, 0, 3, NULL),
(732, 156, 29, 10, 3, NULL),
(733, 156, 8, 0, 3, NULL),
(734, 156, 10, 1, 3, NULL),
(735, 156, 24, 0, 3, NULL),
(736, 156, 29, 10, 3, NULL),
(737, 156, 8, 0, 3, NULL),
(738, 156, 10, 1, 3, NULL),
(739, 156, 24, 0, 3, NULL),
(740, 156, 29, 10, 3, NULL),
(741, 156, 8, 0, 3, NULL),
(742, 156, 10, 1, 3, NULL),
(743, 156, 24, 0, 3, NULL),
(744, 156, 29, 10, 3, NULL),
(745, 156, 8, 0, 3, NULL),
(746, 156, 10, 1, 3, NULL),
(747, 156, 24, 0, 3, NULL),
(748, 156, 29, 10, 3, NULL),
(749, 156, 8, 0, 3, NULL),
(750, 156, 10, 1, 3, NULL),
(751, 156, 24, 0, 3, NULL),
(752, 156, 29, 10, 3, NULL),
(753, 156, 8, 0, 3, NULL),
(754, 156, 10, 1, 3, NULL),
(755, 156, 24, 0, 3, NULL),
(756, 156, 29, 10, 3, NULL),
(757, 156, 8, 0, 3, NULL),
(758, 156, 10, 1, 3, NULL),
(759, 156, 24, 0, 3, NULL),
(760, 156, 29, 10, 3, NULL),
(761, 156, 8, 0, 3, NULL),
(762, 156, 10, 1, 3, NULL),
(763, 156, 24, 0, 3, NULL),
(764, 156, 29, 10, 3, NULL),
(765, 156, 8, 0, 3, NULL),
(766, 156, 10, 1, 3, NULL),
(767, 156, 24, 0, 3, NULL),
(768, 156, 29, 10, 3, NULL),
(769, 156, 8, 0, 3, NULL),
(770, 156, 10, 1, 3, NULL),
(771, 156, 24, 0, 3, NULL),
(772, 156, 29, 10, 3, NULL),
(773, 156, 8, 0, 3, NULL),
(774, 156, 10, 1, 3, NULL),
(775, 156, 24, 0, 3, NULL),
(776, 156, 29, 10, 3, NULL),
(777, 156, 8, 0, 3, NULL),
(778, 156, 10, 1, 3, NULL),
(779, 156, 24, 0, 3, NULL),
(780, 156, 29, 10, 3, NULL),
(781, 157, 2, 10, 2, NULL),
(782, 157, 18, 10, 2, NULL),
(783, 158, 33, 2, 2, NULL),
(784, 158, 45, 15, 2, NULL),
(785, 158, 51, 3, 2, NULL),
(786, 158, 57, 7, 2, NULL),
(787, 158, 59, 7, 2, NULL),
(788, 158, 63, 10, 2, NULL),
(789, 158, 33, 6, 3, NULL),
(790, 158, 44, 2, 3, NULL),
(791, 158, 51, 10, 3, NULL),
(792, 158, 57, 13, 3, NULL),
(793, 158, 58, 16, 3, NULL),
(794, 158, 59, 1, 3, NULL),
(795, 158, 63, 12, 3, NULL),
(796, 159, 14, 4, 2, NULL),
(797, 159, 15, 9, 2, NULL),
(798, 159, 18, 7, 2, NULL),
(799, 159, 23, 3, 2, NULL),
(800, 159, 29, 7, 2, NULL),
(801, 159, 27, 2, 3, NULL),
(802, 159, 29, 13, 3, NULL),
(803, 160, 17, 4, 2, NULL),
(804, 160, 29, 14, 2, NULL),
(805, 160, 22, 8, 3, NULL),
(806, 160, 29, 1, 3, NULL),
(807, 160, 27, 5, 4, NULL),
(808, 160, 29, 4, 4, NULL),
(809, 161, 10, 10, 2, NULL),
(810, 161, 14, 10, 2, NULL),
(811, 161, 23, 5, 2, NULL),
(812, 161, 27, 4, 2, NULL),
(813, 161, 29, 6, 2, NULL),
(814, 162, 14, 3, 3, NULL),
(815, 162, 29, 10, 3, NULL),
(816, 162, 7, 2, 4, NULL),
(817, 162, 14, 2, 4, NULL),
(818, 162, 27, 2, 4, NULL),
(819, 162, 29, 5, 4, NULL),
(820, 163, 28, 21, 2, NULL),
(821, 163, 29, 20, 2, NULL),
(822, 165, 29, 20, 2, NULL),
(823, 166, 9, 3, 2, NULL),
(824, 166, 14, 3, 2, NULL),
(825, 166, 18, 1, 2, NULL),
(826, 166, 27, 7, 2, NULL),
(827, 166, 29, 24, 2, NULL),
(828, 166, 30, 8, 2, NULL),
(829, 167, 32, 14, 2, NULL),
(830, 167, 33, 50, 2, NULL),
(831, 167, 34, 50, 2, NULL),
(832, 167, 36, 5, 2, NULL),
(833, 167, 37, 2, 2, NULL),
(834, 167, 41, 1, 2, NULL),
(835, 167, 43, 4, 2, NULL),
(836, 167, 44, 5, 2, NULL),
(837, 167, 47, 3, 2, NULL),
(838, 167, 48, 6, 2, NULL),
(839, 167, 57, 1, 2, NULL),
(840, 167, 59, 2, 2, NULL),
(841, 167, 63, 5, 2, NULL),
(842, 167, 32, 19, 3, NULL),
(843, 167, 33, 19, 3, NULL),
(844, 167, 36, 3, 3, NULL),
(845, 167, 37, 12, 3, NULL),
(846, 167, 43, 5, 3, NULL),
(847, 167, 44, 1, 3, NULL),
(848, 167, 45, 78, 3, NULL),
(849, 167, 47, 1, 3, NULL),
(850, 167, 51, 15, 3, NULL),
(851, 167, 59, 9, 3, NULL),
(852, 167, 63, 5, 3, NULL),
(853, 167, 32, 25, 4, NULL),
(854, 167, 33, 12, 4, NULL),
(855, 167, 41, 20, 4, NULL),
(856, 167, 42, 15, 4, NULL),
(857, 167, 43, 13, 4, NULL),
(858, 167, 45, 20, 4, NULL),
(859, 167, 61, 7, 4, NULL),
(860, 167, 63, 7, 4, NULL),
(861, 168, 16, 3, 2, NULL),
(862, 168, 17, 0, 2, NULL),
(863, 168, 21, 6, 2, NULL),
(864, 168, 34, 44, 2, NULL),
(865, 168, 39, 10, 2, NULL),
(866, 168, 44, 18, 2, NULL),
(867, 168, 50, 6, 2, NULL),
(868, 168, 51, 1, 2, NULL),
(869, 168, 52, 2, 2, NULL),
(870, 168, 53, 6, 2, NULL),
(871, 168, 16, 10, 3, NULL),
(872, 168, 17, 3, 3, NULL),
(873, 168, 18, 4, 3, NULL),
(874, 168, 20, 7, 3, NULL),
(875, 168, 32, 6, 3, NULL),
(876, 168, 33, 20, 3, NULL),
(877, 168, 35, 3, 3, NULL),
(878, 168, 37, 16, 3, NULL),
(879, 168, 39, 4, 3, NULL),
(880, 168, 40, 41, 3, NULL),
(881, 168, 41, 1, 3, NULL),
(882, 168, 42, 4, 3, NULL),
(883, 168, 43, 9, 3, NULL),
(884, 168, 45, 42, 3, NULL),
(885, 168, 48, 66, 3, NULL),
(886, 169, 13, 3, 2, NULL),
(887, 169, 14, 140, 2, NULL),
(888, 169, 24, 2, 2, NULL),
(889, 169, 29, 20, 2, NULL),
(890, 169, 14, 39, 3, NULL),
(891, 169, 27, 15, 3, NULL),
(892, 169, 29, 12, 3, NULL),
(893, 170, 2, 3, 2, NULL),
(894, 170, 22, 8, 2, NULL),
(895, 170, 29, 10, 2, NULL),
(896, 170, 22, 5, 3, NULL),
(897, 170, 29, 2, 3, NULL),
(898, 170, 30, 6, 3, NULL),
(899, 170, 2, 2, 4, NULL),
(900, 170, 27, 3, 4, NULL),
(901, 170, 29, 3, 4, NULL),
(902, 171, 14, 9, 2, NULL),
(903, 171, 29, 15, 2, NULL),
(904, 171, 14, 5, 3, NULL),
(905, 171, 24, 5, 3, NULL),
(906, 171, 27, 3, 3, NULL),
(907, 171, 29, 5, 3, NULL),
(908, 172, 13, 5, 2, NULL),
(909, 172, 14, 2, 2, NULL),
(910, 172, 17, 71, 2, NULL),
(911, 172, 27, 3, 2, NULL),
(912, 172, 29, 12, 2, NULL),
(913, 172, 13, 1, 3, NULL),
(914, 172, 27, 3, 3, NULL),
(915, 172, 29, 2, 3, NULL),
(916, 173, 29, 5, 2, NULL),
(917, 173, 28, 6, 3, NULL),
(918, 173, 29, 5, 3, NULL),
(919, 174, 13, 6, 2, NULL),
(920, 174, 28, 55, 2, NULL),
(921, 174, 29, 3, 2, NULL),
(922, 174, 30, 18, 2, NULL),
(923, 180, 38, 12, 2, NULL),
(924, 180, 41, 1, 2, NULL),
(925, 180, 43, 5, 2, NULL),
(926, 180, 44, 6, 2, NULL),
(927, 180, 45, 18, 2, NULL),
(928, 180, 63, 10, 2, NULL),
(929, 180, 38, 2, 3, NULL),
(930, 180, 42, 12, 3, NULL),
(931, 180, 45, 7, 3, NULL),
(932, 180, 49, 20, 3, NULL),
(933, 180, 51, 8, 3, NULL),
(934, 180, 57, 15, 3, NULL),
(935, 180, 58, 10, 3, NULL),
(936, 180, 61, 11, 3, NULL),
(937, 180, 63, 55, 3, NULL),
(938, 181, 62, 178, 2, NULL),
(939, 191, 38, 10, 2, NULL),
(940, 192, 42, 2, 2, NULL),
(941, 202, 52, 66, 3, NULL),
(942, 207, 42, 14, 2, NULL),
(943, 213, 21, 1, 2, NULL),
(944, 213, 21, 1, 2, NULL),
(945, 213, 21, 1, 2, NULL),
(946, 213, 21, 1, 2, NULL),
(947, 213, 21, 1, 2, NULL),
(948, 213, 21, 1, 2, NULL),
(949, 213, 21, 1, 2, NULL),
(950, 213, 21, 1, 2, NULL),
(951, 213, 21, 1, 2, NULL),
(952, 213, 21, 1, 2, NULL),
(953, 213, 21, 1, 2, NULL),
(954, 213, 21, 1, 2, NULL),
(955, 213, 21, 1, 2, NULL),
(956, 213, 21, 1, 2, NULL),
(957, 213, 21, 1, 2, NULL),
(958, 213, 21, 0, 2, NULL),
(959, 213, 21, 0, 2, NULL),
(960, 213, 21, 0, 2, NULL),
(961, 213, 21, 0, 2, NULL),
(962, 213, 21, 0, 2, NULL),
(963, 213, 21, 0, 2, NULL),
(964, 213, 21, 0, 2, NULL),
(965, 213, 21, 0, 2, NULL),
(966, 213, 21, 0, 2, NULL),
(967, 213, 21, 0, 2, NULL),
(968, 213, 21, 0, 2, NULL),
(969, 213, 21, 0, 2, NULL),
(970, 213, 21, 0, 2, NULL),
(971, 213, 21, 0, 2, NULL),
(972, 213, 21, 0, 2, NULL),
(973, 213, 21, 0, 2, NULL),
(974, 213, 21, 0, 2, NULL),
(975, 213, 21, 0, 2, NULL),
(976, 213, 21, 0, 2, NULL),
(977, 213, 21, 0, 2, NULL),
(978, 213, 21, 0, 2, NULL),
(979, 213, 21, 0, 2, NULL),
(980, 213, 21, 0, 2, NULL),
(981, 213, 21, 0, 2, NULL),
(982, 213, 21, 0, 2, NULL),
(983, 213, 21, 0, 2, NULL),
(984, 213, 21, 0, 2, NULL),
(985, 213, 21, 0, 2, NULL),
(986, 213, 21, 0, 2, NULL),
(987, 213, 21, 0, 2, NULL),
(988, 213, 21, 0, 2, NULL),
(989, 213, 21, 0, 2, NULL),
(990, 213, 21, 0, 2, NULL),
(991, 213, 21, 0, 2, NULL),
(992, 213, 21, 0, 2, NULL),
(993, 213, 21, 0, 2, NULL),
(994, 213, 21, 0, 2, NULL),
(995, 213, 21, 0, 2, NULL),
(996, 213, 21, 0, 2, NULL),
(997, 213, 21, 0, 2, NULL),
(998, 213, 21, 0, 2, NULL),
(999, 213, 21, 0, 2, NULL),
(1000, 213, 21, 0, 2, NULL),
(1001, 213, 21, 0, 2, NULL),
(1002, 213, 21, 0, 2, NULL),
(1003, 213, 21, 0, 2, NULL),
(1004, 213, 21, 0, 2, NULL),
(1005, 213, 21, 0, 2, NULL),
(1006, 213, 21, 0, 2, NULL),
(1007, 213, 21, 0, 2, NULL),
(1008, 213, 21, 0, 2, NULL),
(1009, 213, 21, 0, 2, NULL),
(1010, 213, 21, 0, 2, NULL),
(1011, 213, 21, 0, 2, NULL),
(1012, 213, 21, 0, 2, NULL),
(1013, 213, 21, 0, 2, NULL),
(1014, 213, 21, 0, 2, NULL),
(1015, 213, 21, 0, 2, NULL),
(1016, 213, 21, 0, 2, NULL),
(1017, 213, 21, 0, 2, NULL),
(1018, 213, 21, 0, 2, NULL),
(1019, 213, 21, 0, 2, NULL),
(1020, 213, 21, 0, 2, NULL),
(1021, 213, 21, 0, 2, NULL),
(1022, 213, 21, 0, 2, NULL),
(1023, 213, 21, 0, 2, NULL),
(1024, 213, 21, 0, 2, NULL),
(1025, 213, 21, 0, 2, NULL),
(1026, 213, 21, 0, 2, NULL),
(1027, 213, 21, 0, 2, NULL),
(1028, 213, 21, 0, 2, NULL),
(1029, 213, 21, 0, 2, NULL),
(1030, 213, 21, 0, 2, NULL),
(1031, 214, 30, 0, 2, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `id_empleado` int(10) UNSIGNED NOT NULL,
  `nombre_empleado` varchar(30) DEFAULT NULL,
  `apellido` varchar(30) DEFAULT NULL,
  `codigo` int(11) DEFAULT NULL,
  `codigo_alea` varchar(20) DEFAULT NULL,
  `tipo_usuario` varchar(20) DEFAULT NULL,
  `cerrar_orden` tinyint(1) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT NULL,
  `id_proceso` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`id_empleado`, `nombre_empleado`, `apellido`, `codigo`, `codigo_alea`, `tipo_usuario`, `cerrar_orden`, `activo`, `id_proceso`) VALUES
(2, 'Edgar Ivan', 'Agüero', 10474, '#7524790922#', 'Supervisor', 1, 1, 1),
(3, 'Azael', 'Cholico', 10148, NULL, 'Supervisor', 1, 1, 1),
(4, 'Enrique de Jesús', 'Camacho', 233, '#8369805387#', 'Lider', 1, 1, 5),
(5, 'Osvaldo', 'Palomino', 300, '#9101270504#', 'Supervisor', 1, 1, 15),
(6, 'Francys Alejandra ', 'Mora', 478, '#9713686488#', 'Lider', 1, 1, 5),
(7, 'Eric', 'Carrilo', 627, NULL, 'Operador', 1, 1, 5),
(8, 'Soledad Paola', 'Acosta', 644, NULL, 'Operador', 1, 1, 5),
(9, 'Osvaldo', 'Chavarin', 750, '#5436002103#', 'Operador', 1, 1, 5),
(10, 'Efren Eduardo', 'Díaz', 767, NULL, 'Operador', 1, 1, 5),
(11, 'Elizabeth', 'Zamora', 775, '#4080948427#', 'Operador', 1, 1, 5),
(12, 'Jose Guadalupe', 'Hernandez', 884, '#8406596359#', 'Operador', 1, 1, 5),
(13, 'Laura Patricia', 'Ramos', 928, '#8150937068#', 'Operador', 1, 1, 5),
(14, 'Anselmo', 'Correa', 999, NULL, 'Operador', 1, 1, 1),
(15, 'Laura Jacqueline', 'Zuñiga', 10223, '#2123467326#', 'Operador', 1, 1, 5),
(16, 'Julio Noe', 'Acosta', 10234, '#5484823854#', 'Operador', 1, 1, 5),
(17, 'Jose Armando', 'Macias', 10229, '#2460351257#', 'Operador', 1, 1, 5),
(18, 'Abel', 'Alanis', 10225, '#9210330772#', 'Operador', 1, 1, 5),
(19, 'Edgar Melquiades', 'Salazar', 10237, '#6793307718#', 'Operador', 1, 1, 5),
(20, 'Isaac', 'Corona', 10369, NULL, 'Operador', 1, 1, 5),
(21, 'Eugenia', 'Rodriguez', 10395, '#2795431287#', 'Operador', 1, 1, 5),
(22, 'Maria Cruz', 'Garcia', 10396, '#2815207322#', 'Operador', 1, 1, 5),
(23, 'Erika Rubi', 'Villalobos', 10399, NULL, 'Operador', 1, 1, 5),
(24, 'Norma Iliana', 'Guzman', 10440, NULL, 'Operador', 1, 1, 5),
(25, 'Berenice', 'Palacios', 10443, '#5379124672#', 'Operador', 1, 1, 5),
(26, 'Rosario', 'Flores', 10450, '#6873482573#', 'Operador', 1, 1, 5),
(27, 'Pedro', 'Rueda', 415, '#1968029528#', 'Operador', 1, 1, 1),
(28, 'Horacio', 'Guzman', 537, '#2144429696#', 'Operador', 1, 1, 1),
(29, 'Ana Cecilia', 'Carranza', 289, NULL, 'Operador', 1, 1, 1),
(30, 'David', 'Rojas', 726, '#1696213975#', 'Operador', 1, 1, 1),
(31, 'Lucia Elena', 'Quintero', 341, '#2280298981#', 'Operador', 1, 1, 1),
(32, 'Jorge ', 'Humberto', 10431, NULL, 'Operador', 1, 1, 1),
(33, 'Maria de Jesus', 'Lomeli', 944, NULL, 'Operador', 1, 1, 1),
(34, 'Oscar Ivan', 'Hernandez', 326, '#8080287165#', 'Operador', 1, 1, 1),
(35, 'Diana Laura', 'Flores', 291, '#4703847410#', 'Operador', 1, 1, 1),
(36, 'Alberto', 'Ortega', 549, '#1656989239#', 'Operador', 1, 1, 1),
(37, 'Jesus Joel', 'Rivera', 694, '#6819348874#', 'Operador', 1, 1, 1),
(38, 'Carlos', 'Renteria', 715, '#2452736977#\r\n', 'Operador', 1, 1, 1),
(39, 'Christian Alejandro', 'Gutierrez', 325, NULL, 'Operador', 1, 1, 1),
(40, 'Julio Cesar', 'Diaz', 10200, NULL, 'Operador', 1, 0, 2),
(41, 'Omar', 'Hernandez', 594, NULL, 'Operador', 1, 1, 1),
(42, 'Carlos Alberto', 'Velazquez', 270, '#4921571673#', 'Operador', 1, 1, 1),
(43, 'Manuel ', 'Muñoz', 556, '#6480397329#', 'Operador', 1, 1, 1),
(44, 'Jose Erick', 'Ortega', 10339, NULL, 'Operador', 1, 1, 1),
(45, 'Alfredo', 'Monje', 297, '#9609963427#', 'Keeper', 1, 1, 1),
(46, 'Abel ', 'Arellano', 479, '#3225834218#', 'Keeper', 1, 1, 1),
(47, 'Maricruz', 'Flores', 292, '#3566828708#', 'Operador', 1, 1, 1),
(48, 'Jessica', 'Torres', 547, '#8495546167#', 'Operador', 1, 1, 1),
(49, 'Cesar Damian', 'Hernandez', 565, '#6722701617#', 'Operador', 1, 1, 1),
(50, 'Francisco Daniel', 'Gonzalez', 611, NULL, 'Operador', 1, 1, 1),
(51, 'Carlos Abelardo', 'Amador', 764, '#8661300619#', 'Operador', 1, 1, 1),
(52, 'Brisnael', 'Reyes', 723, NULL, 'Operador', 1, 1, 1),
(53, 'Olaf', 'Mendoza', 721, '#4211745341#', 'Operador', 1, 1, 1),
(54, 'Jose Angel', 'Lopez', 10379, '#8842005991#', 'Inspector', 1, 1, 1),
(55, 'Diego Guadalupe', 'Rivera', 345, '#1319486697#', 'Keeper', 1, 1, 1),
(56, 'Margarito', 'Pineda', 340, NULL, 'Operador', 1, 1, 1),
(57, 'Amelia ', 'Beltran', 316, '#4031772526#', 'Operador', 1, 1, 1),
(58, 'Jairo', 'Hernandez', 575, '#5478802385#', 'Operador', 1, 1, 1),
(59, 'Maria Lizette', 'Montes', 728, NULL, 'Operador', 1, 1, 1),
(60, 'Juan Luis ', 'Curiel', 421, NULL, 'Operador', 1, 1, 1),
(61, 'Christian', 'Barrera', 315, NULL, 'Operador', 1, 1, 1),
(62, 'Rodolfo', 'Hernandez', 591, '#6027946351#', 'Operador', 1, 1, 1),
(63, 'Luis Manuel', 'Flores', 308, NULL, 'Operador', 1, 1, 1),
(64, 'Arturo ', 'Tellez', 10334, NULL, 'Operador', 1, 1, 1),
(65, 'Ana', 'Gonzalez', 729, NULL, 'Operador', 1, 1, 1),
(66, 'Salvador', 'Torres', 608, '#4833644595#', 'Operador', 1, 1, 1),
(67, 'Jose de Jesus', 'Ramos', 10189, NULL, 'Operador', 1, 1, 1),
(68, 'Salvador', 'Padilla', 10368, '#5905206120#', 'Operador', 1, 1, 1),
(69, 'Jose de Jesus', 'Mares', 696, '#8687033548#', 'Operador', 1, 1, 1),
(70, 'Noemi Elizabeth', 'Arizaga', 379, NULL, 'Operador', 1, 1, 1),
(71, 'Adan', 'de Anda', 10430, NULL, 'Operador', 1, 1, 1),
(72, 'David Adrian', 'Hernandez', 10425, NULL, 'Operador', 1, 1, 1),
(73, 'Ismael Absalon', 'Guzman', 10320, NULL, 'Operador', 1, 1, 1),
(74, 'Jose Humberto', 'Lopez', 799, NULL, 'Operador', 1, 1, 1),
(75, 'Hector Gonzalo', 'Vazquez', 788, NULL, 'Operador', 1, 1, 1),
(76, 'Juana', 'Lopez', 10270, NULL, 'Operador', 1, 1, 3),
(89, 'Caritina', 'Salcedo', 10121, NULL, 'Operador', 1, 1, 1),
(90, 'Rosario Guadalupe', 'Martínez', 10126, NULL, 'Operador', 1, 1, 1),
(91, 'María de Rosario', 'Navarro', 10130, NULL, 'Operador', 1, 1, 1),
(92, 'Martha', 'Díaz', 10191, NULL, 'Operador', 1, 1, 1),
(93, 'Ma. Soledad', 'Rubio', 10192, NULL, 'Operador', 1, 1, 1),
(94, 'María Teresa', 'Cuevas', 10196, NULL, 'Operador', 1, 1, 1),
(95, 'Elisa Noemí', 'Delgadillo', 10199, NULL, 'Operador', 1, 1, 1),
(96, 'Martha Alicia', 'Ochoa', 10253, NULL, 'Operador', 1, 1, 1),
(97, 'Deisi', 'Becerra', 10254, NULL, 'Operador', 1, 1, 1),
(98, 'Teresa', 'Hernández', 10257, NULL, 'Operador', 1, 1, 1),
(99, 'Perla Gabriela', 'Gómez', 10262, NULL, 'Operador', 1, 1, 1),
(100, 'Karla Edith', 'Valadez', 10278, NULL, 'Operador', 1, 1, 1),
(101, 'Jonathan Efraín', 'Eligio', 10291, NULL, 'Operador', 1, 1, 1),
(102, 'Carlos Eduardo', 'Serrano', 10296, NULL, 'Operador', 1, 1, 1),
(103, 'Isarís Alejandra', 'Álvarez', 10299, '#12345678910#', 'Operador', 1, 1, 1),
(104, 'Juana Yadira', 'Nuño', 10301, NULL, 'Operador', 1, 1, 1),
(105, 'Mayra Leoneli', 'Marquez', 10306, NULL, 'Operador', 1, 1, 1),
(106, 'Elizabeth', 'Rosas', 10307, NULL, 'Operador', 1, 1, 1),
(107, 'Fernanda Michelle', 'Moreno', 10328, '#7251329054#', 'Inspector', 1, 1, 1),
(108, 'Dallana Zitlalic', 'Ponce', 10329, NULL, 'Operador', 1, 1, 1),
(109, 'María de Jesús', 'Osorio', 10332, NULL, 'Operador', 1, 1, 1),
(110, 'Joselín Cecilia', 'Rosas', 10335, NULL, 'Operador', 1, 1, 1),
(111, 'Leticia', 'Rodriguez', 10338, NULL, 'Operador', 1, 1, 1),
(112, 'Martha Guadalupe', 'Soto', 10343, NULL, 'Operador', 1, 1, 1),
(113, 'Marcela', 'Reyna', 10367, NULL, 'Operador', 1, 1, 1),
(114, 'Ana Laura', 'Tovar', 10384, NULL, 'Operador', 1, 1, 1),
(115, 'Patricia Jeannette', 'Jiménez', 10387, NULL, 'Operador', 1, 1, 1),
(116, 'Sandibel', 'Sepúlveda', 10402, '#9831262866#', 'Inspector', 1, 1, 1),
(117, 'María Luisa', 'García', 245, NULL, 'Operador', 1, 1, 1),
(118, 'Anaclariza Guadalupe', 'Hernández', 409, NULL, 'Asistente', 1, 1, 1),
(119, 'Celia Viviana', 'López', 566, '#6825197531#', 'Operador', 1, 1, 1),
(120, 'Laura Teresa', 'Sánchez', 568, NULL, 'Operador', 1, 1, 1),
(121, 'Yaneth', 'Lazo', 593, NULL, 'Operador', 1, 1, 1),
(122, 'Liliana ', 'Chitica', 669, '#4850015151#', 'Operador', 1, 1, 1),
(123, 'Alma Patricia', 'Paz', 742, '#4646481622#', 'Operador', 1, 1, 1),
(124, 'Gabriela', 'Joya', 776, NULL, 'Operador', 1, 1, 1),
(125, 'Ma Guadalupe', 'Becerra', 796, '#5366325753#\r\n', 'Operador', 1, 1, 1),
(126, 'Ma del Carmen', 'Nuñez', 801, NULL, 'Operador', 1, 1, 1),
(127, 'Rosa Laura', 'Tejeda', 802, NULL, 'Operador', 1, 1, 1),
(128, 'Lus Elena', 'Herrera', 832, NULL, 'Operador', 1, 1, 1),
(129, 'Ana Mónica', 'García', 834, '#9534565872#', 'Operador', 1, 1, 1),
(130, 'Claudia Verónica', 'Romo', 866, NULL, 'Operador', 1, 1, 1),
(131, 'María de los Ángeles', 'Meza', 867, '#4276994839#', 'Operador', 1, 1, 1),
(132, 'Ximena', 'Olea', 880, NULL, 'Operador', 1, 1, 1),
(133, 'Ma de Lourdes', 'Montes', 969, NULL, 'Operador', 1, 1, 1),
(134, 'Celia', 'Landino', 972, '#4409436733#', 'Operador', 1, 1, 1),
(135, 'Amanda Esther', 'Robles', 985, NULL, 'Operador', 1, 1, 1),
(136, 'Karen Icela', 'Rodriguez', 10166, NULL, 'Operador', 1, 0, 2),
(137, 'Alma Gabriela', 'Hernandez', 10176, NULL, 'Operador', 1, 1, 10),
(138, 'Anabel ', 'Rojo', 10180, '#123456#', 'Supervisor', 1, 1, 10),
(139, 'Luis Eduardo', 'Martinez', 10249, NULL, 'Lider', 1, 1, 10),
(140, 'Gilberto', 'Cruz', 10263, NULL, 'Operador', 1, 0, 2),
(143, 'Carlos', 'Alcaraz', 10297, NULL, 'Operador', 1, 0, 2),
(145, 'Manuel Enrique', 'Guerrero', 10375, '#2683600117#', 'Operador', 1, 1, 2),
(146, 'Deyaneira Fabiola', 'Rosales', 10389, NULL, 'Operador', 1, 1, 2),
(147, 'Luis Miguel', 'Gaona', 10391, NULL, 'Operador', 1, 1, 2),
(148, 'Marisol', 'Navarrete', 10394, '#6291854842#', 'Operador', 1, 1, 2),
(149, 'Alejandro', 'Treviño', 10405, '#5397729568#', 'Operador', 1, 1, 2),
(150, 'Gustavo ', 'Padilla', 10411, NULL, 'Operador', 1, 1, 2),
(151, 'Ana Isabel', 'Hernandez', 10436, NULL, 'Operador', 1, 1, 2),
(152, 'Martin ', 'Huerta', 10441, NULL, 'Operador', 1, 1, 2),
(153, 'Armando Jonas', 'Gorgonio', 10444, NULL, 'Operador', 1, 1, 2),
(154, 'Tania Sarai', 'Medina', 10454, NULL, 'Operador', 1, 1, 2),
(155, 'Julia Janely', 'Ruiz', 10455, NULL, 'Operador', 1, 1, 2),
(156, 'Jose Francisco', 'Barrios', 230, NULL, 'Operador', 1, 1, 2),
(159, 'Cesar Oswaldo', 'Rodriguez', 388, NULL, 'Operador', 1, 1, 2),
(162, 'Salvador', 'Flores', 407, '#9462842574#', 'Operador', 1, 0, 2),
(163, 'Cristobal', 'Alvarez', 437, NULL, 'Operador', 1, 1, 2),
(165, 'Ernesto', 'Gozales', 508, NULL, 'Operador', 1, 1, 2),
(166, 'Ramon', 'Diaz', 517, NULL, 'Operador', 1, 1, 2),
(167, 'Luis Orlando', 'Guzman', 560, NULL, 'Operador', 1, 1, 2),
(168, 'Jaime', 'Hernandez', 595, NULL, 'Operador', 1, 1, 2),
(169, 'Janai Donaciano', 'Cardenas', 620, '#6482253109#', 'Operador', 1, 1, 2),
(170, 'Victor', 'Zavala', 645, '#6804274506#', 'Operador', 1, 1, 2),
(171, 'J Jesus', 'Cipres', 695, NULL, 'Operador', 1, 1, 2),
(174, 'Alejandro', 'Cortez', 738, '#2644358107#', 'Operador', 1, 1, 2),
(175, 'Herminio', 'Vazquez', 744, NULL, 'Operador', 1, 1, 2),
(176, 'Lauro', 'Zuñiga', 766, NULL, 'Operador', 1, 1, 2),
(177, 'Jose Felipe', 'Dueñas', 794, NULL, 'Operador', 1, 1, 2),
(178, 'Alex Wilfredo', 'Hernandez', 851, NULL, 'Operador', 1, 1, 2),
(179, 'Luis Daniel', 'Estrada', 865, NULL, 'Operador', 1, 1, 2),
(180, 'Juan', 'Lomeli', 891, NULL, 'Operador', 1, 1, 2),
(181, 'Rogelio', 'Romero', 979, NULL, 'Operador', 1, 1, 2),
(182, 'Carlos', 'Hernandez', 984, NULL, 'Operador', 1, 1, 2),
(183, 'Ma. de los Milagros', 'Lomeli', 10165, NULL, 'Operador', 1, 1, 3),
(184, 'Tomas', 'Manzano', 10168, '#7738473080#', 'Keeper', 1, 1, 3),
(185, 'Perfecto', 'Espino', 10182, NULL, 'Operador', 1, 1, 3),
(186, 'Blanca Azucena', 'Monje', 10190, NULL, 'Operador', 1, 1, 3),
(187, 'Rosa María', 'Rodriguez', 10203, NULL, 'Operador', 1, 1, 3),
(188, 'Ana Isabel', 'Gonzalez', 10219, NULL, 'Operador', 1, 1, 3),
(189, 'Yolanda', 'Reyes', 10267, NULL, 'Operador', 1, 1, 3),
(190, 'Ma. Guadalupe', 'Esquivel', 10269, NULL, 'Operador', 1, 1, 3),
(191, 'Mario Eduardo', 'Diaz', 10283, '#2828256461#', 'Keeper', 1, 1, 3),
(192, 'Julio Cesar', 'Lopez', 10292, '#8489175111#', 'Operador', 1, 1, 3),
(193, 'Julio Emanuel', 'Curiel', 10312, NULL, 'Operador', 1, 1, 3),
(194, 'Martin Alejandro', 'Panuco', 10313, NULL, 'Operador', 1, 1, 3),
(195, 'Erik Paul', 'Corral', 10319, NULL, 'Operador', 1, 1, 3),
(196, 'Mario Sebastian', 'Aguayo', 10336, NULL, 'Keeper', 1, 1, 3),
(197, 'Arnold Santiago', 'Nuño', 10337, NULL, 'Operador', 1, 1, 3),
(198, 'Karina Elizabeth', 'Barajas', 10361, NULL, 'Operador', 1, 1, 3),
(199, 'Rocio', 'Urista', 10388, NULL, 'Operador', 1, 1, 3),
(200, 'Luis Roberto', 'Casillas', 10435, NULL, 'Operador', 1, 1, 3),
(201, 'Martin', ' de Leon', 10449, NULL, 'Operador', 1, 1, 3),
(202, 'Miguel Angel', 'Estrella', 10457, NULL, 'Operador', 1, 1, 3),
(203, 'Victor Alfonso', 'Bravo', 10460, '#8064389502#', 'Keeper', 1, 1, 3),
(204, 'Diego', 'Delgado', 10470, NULL, 'Operador', 1, 1, 3),
(205, 'Jose', 'Bedoy', 10478, NULL, 'Operador', 1, 1, 3),
(206, 'Jose Azael', 'Garcia', 10484, NULL, 'Operador', 1, 1, 3),
(207, 'Raul Antonio', 'Gonzalez', 10492, NULL, 'Operador', 1, 1, 3),
(208, 'Victor Manuel', 'Martinez', 398, '#5008903391#', 'Operador', 1, 1, 3),
(209, 'Carlos', 'Pavon', 435, '#3993276820#', 'Operador', 1, 1, 3),
(210, 'Alan Christopher', 'Garcia', 482, NULL, 'Operador', 1, 1, 3),
(211, 'Hilario del Jesus', 'Renteral', 483, NULL, 'Operador', 1, 1, 3),
(212, 'Jorge', 'Ibarra', 489, NULL, 'Operador', 1, 1, 3),
(213, 'Maria de Jesus', 'Serrano', 523, '#8745860418#', 'Operador', 1, 1, 3),
(214, 'Adriana', 'Garcia', 544, NULL, 'Supervisor', 1, 1, 16),
(215, 'Griselda', 'Cervantes', 546, '#5616101074#', 'Operador', 1, 1, 16),
(216, 'Abraham', 'Hernandez', 592, NULL, 'Operador', 1, 1, 3),
(217, 'Emmanuel', 'Maria', 655, NULL, 'Operador', 1, 1, 3),
(218, 'Felipe de Jesus', 'Rosas', 757, NULL, 'Operador', 1, 1, 3),
(219, 'J. Jesus', 'Mora', 804, NULL, 'Operador', 1, 1, 3),
(220, 'Mario Aaron', 'Plata', 900, NULL, 'Operador', 1, 1, 3),
(221, 'Francisco', 'Garcia', 930, '#1818252393#', 'Keeper', 1, 1, 3),
(222, 'Juan Jose', 'Ramirez', 931, NULL, 'Operador', 1, 1, 3),
(223, 'Jesus Emmanuel', 'Gonzalez', 960, NULL, 'Operador', 1, 1, 3),
(224, 'Ricardo', 'Navarro', 974, NULL, 'Operador', 1, 1, 3),
(308, 'Arlette Ivanna', 'Gonzalez', 10433, NULL, 'Operador', 1, 1, 5),
(309, 'Aaron Adrian', 'Torres', 504, NULL, 'Operador', 1, 1, 5),
(310, 'Luz Gabriela', 'Carrillo', 10133, NULL, 'Supervisor', 1, 1, 9),
(311, 'Rosa Elia', 'Villanueva', 10134, '#12345678#', 'Supervisor', 1, 1, 12),
(312, 'Ma. de Jesús', 'Segura', 10138, NULL, 'Operador', 1, 1, 4),
(313, 'Lizbeth', 'Rosas', 10183, NULL, 'Operador', 1, 1, 4),
(314, 'Perla Aidee', 'Torres', 10201, NULL, 'Operador', 1, 1, 4),
(315, 'Norma Angelica', 'Rodriguez', 10204, NULL, 'Operador', 1, 1, 4),
(316, 'Brenda Mirella', 'Perez', 10206, '#4305709069#', 'Operador', 1, 1, 5),
(317, 'Nancy Dolores', 'Pacas', 10207, NULL, 'Operador', 1, 1, 4),
(318, 'Hugo Ivan', 'Leonel', 10210, NULL, 'Operador', 1, 1, 4),
(319, 'Maria del Refugio', 'Salcido', 10214, NULL, 'Operador', 1, 1, 4),
(320, 'Cristina', 'Gonzalez', 10218, NULL, 'Operador', 1, 1, 4),
(321, 'Miriam Alejandra', 'Hernandez', 10220, NULL, 'Operador', 1, 1, 4),
(322, 'Daira Lizeth', 'Gomez', 10221, NULL, 'Operador', 1, 1, 4),
(323, 'Maria Veronica', 'Delgado', 10226, NULL, 'Operador', 1, 1, 4),
(324, 'Mariela Lisset', 'Rios', 10231, NULL, 'Operador', 1, 1, 4),
(325, 'Itzel Saray', 'Tovar', 10232, '#6210371563#', 'Operador', 1, 1, 8),
(326, 'Karla Estefany', 'Garcia', 10239, NULL, 'Operador', 1, 1, 4),
(327, 'Brenda Guadalupe', 'Saucedo', 10241, '#9709388701#', 'Operador', 1, 1, 5),
(328, 'Rosario Adriana', 'Ordoñez', 10243, NULL, 'Operador', 1, 1, 4),
(329, 'Griselda', 'Lomeli', 10245, NULL, 'Operador', 1, 1, 4),
(330, 'Miguel Angel', 'Rodriguez', 10268, NULL, 'Operador', 1, 1, 4),
(331, 'Brenda Elizabeth', 'Rosales', 10273, NULL, 'Operador', 1, 1, 4),
(332, 'Ma Guadalupe', 'Rodriguez', 10284, NULL, 'Operador', 1, 1, 8),
(333, 'Gabriela', 'Ruiz', 10286, NULL, 'Operador', 1, 1, 4),
(334, 'Zenaida', 'Gomez', 10298, NULL, 'Operador', 1, 1, 4),
(335, 'Selina', 'Iñiguez', 10314, NULL, 'Operador', 1, 1, 4),
(336, 'Sonia Maria', 'Avila', 10318, NULL, 'Operador', 1, 1, 4),
(337, 'Elizabeth', 'Perez', 10321, NULL, 'Operador', 1, 1, 4),
(338, 'Albina', 'Rojas', 10322, NULL, 'Operador', 1, 1, 4),
(339, 'Humberto Martin', 'Sigala', 10327, NULL, 'Operador', 1, 1, 4),
(340, 'Jessica Berenice', 'Sanchez', 10340, '#7892482185#', 'Operador', 1, 1, 4),
(341, 'Nancy Elizabeth', 'Silva', 10354, NULL, 'Operador', 1, 1, 4),
(342, 'Maria Candelaria', 'Naranjo', 10356, '#3279937291#', 'Operador', 1, 1, 4),
(343, 'Maria Bibiana', 'Contreras', 10362, '#1703775869#', 'Operador', 1, 1, 4),
(344, 'Xitlalit Abigail', 'Trujillo', 10363, '#8507336587#', 'Operador', 1, 1, 4),
(345, 'Nora Isabel', 'Garcia', 10365, '#5215351231#', 'Operador', 1, 1, 4),
(347, 'Maria del Carmen', 'Carrera', 10370, '#6340145049#', 'Operador', 1, 1, 4),
(348, 'Maria del Carmen', 'Benavides', 10372, '#7433457164#', 'Operador', 1, 1, 4),
(349, 'Norma Angelica', 'Jimenez', 10374, '#8419756762#', 'Operador', 1, 1, 4),
(350, 'Norma Angelica', 'Gonzalez', 10377, '#9282966381#', 'Operador', 1, 1, 4),
(352, 'Sonia Elizabeth', 'Ramos', 10380, '#1375243677#', 'Operador', 1, 1, 8),
(353, 'Ma. Cristina', 'Arambula', 10383, '#6326831295#', 'Operador', 1, 1, 5),
(354, 'Alma Beatriz', 'Flores', 10385, '#2636999825#', 'Operador', 1, 1, 4),
(355, 'Gladis Paola', 'Ortiz', 10386, '#4406260268#', 'Operador', 1, 1, 4),
(356, 'Evangelina', 'Lazo', 10400, '#7321093879#', 'Operador', 1, 1, 4),
(357, 'Nelva Rocio', 'Juarez', 10410, '#8502129981#', 'Operador', 1, 1, 5),
(358, 'Cruz Elena', 'Ramos', 10437, NULL, 'Operador', 1, 1, 4),
(359, 'Adelina', 'Rodriguez', 10447, '#9757295755#', 'Operador', 1, 1, 4),
(360, 'Eric Alonso', 'Garcia', 10456, NULL, 'Operador', 1, 1, 4),
(361, 'Yazmin Briseida', 'Leyva', 10468, '#7965171117#', 'Operador', 1, 1, 4),
(362, 'Cutberto Jaime', 'Delgado', 10469, '#8249903151#', 'Operador', 1, 1, 4),
(363, 'Ismael', 'Tapia', 10471, NULL, 'Operador', 1, 1, 4),
(365, 'Angely Guadalupe', 'Pelayo', 10477, '#5124330199#', 'Operador', 1, 1, 4),
(366, 'Irma', 'Gonzalez', 10480, '#1009044751#', 'Operador', 1, 1, 4),
(367, 'Maria del Rosario', 'Camarena', 10481, NULL, 'Operador', 1, 1, 4),
(368, 'Susana', 'Trejo', 10483, '#6638787713#', 'Operador', 1, 1, 4),
(369, 'Ma Guadalupe Estela', 'Ramos', 10485, NULL, 'Operador', 1, 1, 4),
(370, 'Erika Joanna', 'Guzman', 10487, '#2145832527#', 'Operador', 1, 1, 4),
(371, 'Marisol', 'Melgoza', 10488, '#9289268296#', 'Operador', 1, 1, 4),
(372, 'Sandra Janette', 'Tinoco', 10489, NULL, 'Operador', 1, 1, 4),
(373, 'Rosario', 'Valdes', 10493, '#4178387170#', 'Operador', 1, 1, 9),
(374, 'Teresa Esmeralda', 'Ramirez', 10495, '#6792335470#', 'Operador', 1, 1, 4),
(375, 'Maria de Jesus', 'Camarena', 10498, NULL, 'Operador', 1, 1, 4),
(376, 'Luz Angelica', 'Avalos', 10499, '#4339379272#', 'Operador', 1, 1, 4),
(377, 'Luis', 'Bolaños', 231, NULL, 'Supervisor', 1, 0, 9),
(378, 'Efren Alfredo', 'Huerta', 251, NULL, 'Operador', 1, 1, 4),
(379, 'Jose Manuel', 'Torres', 312, NULL, 'Keeper', 1, 1, 9),
(380, 'Javier', 'Hernandez', 328, NULL, 'Keeper', 1, 1, 4),
(381, 'Blanca Guadalupe', 'Trejo', 351, NULL, 'Operador', 1, 1, 4),
(382, 'Rosa Elia', 'Valdovinos', 356, NULL, 'Operador', 1, 1, 4),
(383, 'Martha Felicitas', 'Ramos', 359, NULL, 'Operador', 1, 1, 4),
(384, 'Leticia Isabel', 'Bocanegra', 385, NULL, 'Operador', 1, 1, 4),
(385, 'Erika', 'Murillo', 391, '#4737934800#', 'Supervisor', 1, 1, 9),
(386, 'Maria Felix', 'Torres', 456, NULL, 'Operador', 1, 1, 4),
(387, 'Nayeli Alejandra', 'Solano', 490, '#7559838123#', 'Operador', 1, 1, 8),
(388, 'Antonia', 'Hernandez', 492, NULL, 'Operador', 1, 1, 4),
(389, 'Ana Maricela', 'Garcia', 494, NULL, 'Operador', 1, 1, 4),
(390, 'Imer', 'Aguirre', 495, NULL, 'Supervisor', 1, 1, 4),
(391, 'Hilda Celene', 'Garcia', 497, NULL, 'Supervisor', 1, 1, 6),
(392, 'Miguel Dario', 'Tellez', 506, '#9098138929#', 'Lider', 1, 1, 8),
(393, 'Alondra Viridiana', 'Torres', 507, NULL, 'Operador', 1, 1, 4),
(394, 'Jazmin de Jesus', 'Delgadillo', 510, NULL, 'Operador', 1, 1, 4),
(395, 'Cynthia Beatriz', 'Rivas', 511, NULL, 'Operador', 1, 1, 4),
(396, 'Ma Lourdes', 'Arias', 522, NULL, 'Operador', 1, 1, 4),
(397, 'Ana Luisa', 'Alvarez', 530, NULL, 'Operador', 1, 1, 4),
(398, 'Noemi Betsabe', 'Duarte', 540, NULL, 'Operador', 1, 1, 4),
(399, 'Ana Claudia', 'Sanabria', 541, NULL, 'Operador', 1, 1, 4),
(400, 'Monica', 'Gonzalez', 555, NULL, 'Operador', 1, 1, 4),
(401, 'Olga Cecilia', 'Mora', 582, NULL, 'Operador', 1, 1, 4),
(402, 'Nancy Lizbeth', 'Duque', 603, NULL, 'Supervisor', 1, 1, 4),
(403, 'Blanca Loren', 'Ramirez', 614, NULL, 'Operador', 1, 1, 4),
(404, 'Nancy', 'Ramos', 635, NULL, 'Operador', 1, 1, 8),
(405, 'Patricia', 'Morales', 638, NULL, 'Operador', 1, 1, 4),
(406, 'Maria Alejandra', 'Patiño', 668, NULL, 'Operador', 1, 1, 4),
(407, 'Liliana', 'Arambula', 671, NULL, 'Operador', 1, 1, 4),
(408, 'Josefina', 'Medina', 675, '#9685125496#', 'Operador', 1, 1, 9),
(409, 'Norma Angelica', 'Arellano', 682, '#8827063194#', 'Operador', 1, 1, 4),
(410, 'Norma Cecilia', 'Carreon', 687, NULL, 'Operador', 1, 1, 4),
(411, 'Maria Concepcion', 'Vargas', 10475, NULL, 'Operador', 1, 1, 4),
(458, 'Avelardo', 'Aguirre', 688, '#4771482843#', 'Lider', 1, 1, 8),
(459, 'Aracely', 'Orozco', 689, NULL, 'Operador', 1, 1, 4),
(460, 'Beatriz Stephanie', 'Cervantes', 692, NULL, 'Operador', 1, 1, 4),
(509, 'Maria Guadalupe', 'Mejia', 698, NULL, 'Operador', 1, 1, 4),
(510, 'Ana Cristina', 'Sagrero', 711, NULL, 'Operador', 1, 1, 8),
(560, 'Maria del Carmen', 'Vargas', 719, NULL, 'Operador', 1, 1, 4),
(561, 'Patricia', 'Perez', 731, NULL, 'Operador', 1, 1, 4),
(562, 'Karina Gisel', 'Alvarez', 759, NULL, 'Operador', 1, 1, 4),
(563, 'Manuel Alejandro', 'Guzman', 765, '#8662287160#', 'Operador', 1, 1, 9),
(565, 'Alma Karina', 'Rodriguez', 784, '#4926011626#', 'Operador', 1, 1, 8),
(566, 'Senaida', 'Bolaños', 793, NULL, 'Operador', 1, 1, 8),
(567, 'Guadalupe', 'Garcia', 797, '#3347485035#', 'Lider', 1, 1, 8),
(568, 'Sonia Celene', 'Cortez', 816, NULL, 'Operador', 1, 1, 4),
(569, 'Diana Minerva', 'Sandoval', 822, NULL, 'Operador', 1, 1, 4),
(570, 'Ana Jessury', 'Valdivia', 825, NULL, 'Operador', 1, 1, 4),
(571, 'Ana Leydi', 'Valdovinos', 829, NULL, 'Operador', 1, 1, 4),
(572, 'Maricela', 'Ayala', 839, NULL, 'Operador', 1, 1, 4),
(573, 'Mayra', 'Esquivel', 844, NULL, 'Operador', 1, 1, 4),
(574, 'Olga Lidia', 'Garcia', 845, NULL, 'Operador', 1, 1, 4),
(575, 'Eduardo', 'Avalos', 855, NULL, 'Operador', 1, 1, 4),
(576, 'Gloria Elisabed', 'Alvarado', 856, NULL, 'Operador', 1, 1, 4),
(577, 'Efrain', 'Reynoso', 873, '#9075425786#', 'Operador', 1, 1, 9),
(578, 'Ma Rosalba', 'Ramos', 876, NULL, 'Operador', 1, 1, 4),
(579, 'Blanca Melina', 'Villarruel', 885, NULL, 'Operador', 1, 1, 4),
(580, 'Maria Antonia', 'Ocampo', 904, NULL, 'Operador', 1, 1, 4),
(581, 'Monica Livier', 'Carrillo', 932, NULL, 'Operador', 1, 1, 4),
(582, 'Esmeralda', 'Escobedo', 935, NULL, 'Operador', 1, 1, 4),
(583, 'Martha Alicia', 'Sanchez', 936, NULL, 'Operador', 1, 1, 4),
(584, 'Maria Salud', 'Hernandez', 940, NULL, 'Operador', 1, 1, 4),
(585, 'Norma Isabel', 'Diaz', 941, NULL, 'Operador', 1, 1, 4),
(586, 'Rosa', 'Torres', 946, NULL, 'Operador', 1, 1, 4),
(587, 'Hilda Soraya', 'Maldonado', 947, NULL, 'Operador', 1, 1, 4),
(588, 'Cesar', 'Rodriguez', 950, NULL, 'Lider', 1, 1, 4),
(589, 'Maria Elena', 'Villegas', 951, NULL, 'Operador', 1, 1, 4),
(590, 'Judith', 'Cervantes', 957, NULL, 'Operador', 1, 1, 4),
(591, 'Norma Angelica', 'Ramos', 961, NULL, 'Operador', 1, 1, 4),
(592, 'Yesenia', 'Trejo', 964, NULL, 'Operador', 1, 1, 4),
(593, 'Darely Yazmin', 'Ruvalcaba', 967, NULL, 'Operador', 1, 1, 4),
(594, 'Flor Lorena', 'Alonso', 968, NULL, 'Operador', 1, 1, 4),
(595, 'Veronica', 'Garcia', 971, NULL, 'Operador', 1, 1, 4),
(596, 'Maria del Pilar', 'Canchola', 980, NULL, 'Operador', 1, 1, 4),
(597, 'Francisca', 'Escobar', 983, NULL, 'Operador', 1, 1, 4),
(598, 'Patricia', 'Vazquez', 990, NULL, 'Operador', 1, 1, 4),
(599, 'Angelica', 'Collazo', 995, NULL, 'Operador', 1, 1, 4),
(600, 'Juan Daniel ', 'Gomez', 247, '#9173122054#', 'Supervisor', 1, 1, 2),
(641, 'Gabriel', 'Torres', 10427, '#7310541003#', 'Supervisor', 1, 1, 8),
(642, 'Fabian', 'Rodriguez', 519, '#5958775159#', 'Supervisor', 1, 1, 13),
(643, 'Juan José', 'Jiménez', 10161, '#6726249912#', 'Supervisor', 1, 1, 9),
(644, 'Omar', 'Gutierrez', 10506, NULL, 'Keeper', 1, 1, 7),
(645, 'Victor', 'Vazquez', 10505, NULL, 'Supervisor', 1, 1, 7),
(646, 'Alejandro', 'Lares', 10507, NULL, 'Aduana', 1, 1, 1),
(647, 'Jesús', 'Sánchez', 304, '#9537606777#', 'Supervisor', 1, 1, 16),
(648, 'Jaime', 'Aviña', 10209, NULL, 'Supervisor', 1, 1, 4),
(649, 'Ricardo', 'Velazquez', 10704, '#4538350436#', 'Supervisor', 1, 1, 3),
(650, 'Carlos', 'Hernández', 10668, '#1828575722#', 'Supervisor', 1, 1, 3),
(651, 'Kenta', 'Sobue', 921, NULL, 'Operador', 1, 1, 4),
(653, 'Austreberto', 'Estrada', 10814, '#7082013928#', 'Supervisor', 1, 1, 8),
(654, 'Jorge', 'Alvarez', 10792, '#8844475132#', 'Supervisor', 1, 0, 1),
(656, 'Mónica Graciela', 'Hernández', 10778, NULL, 'Aduana', 1, 1, 1),
(658, 'Alan Daniel', 'Figueroa', 10302, NULL, 'Aduana', 1, 0, 1),
(660, 'Priscila Lizbeth', 'Peña ', 625, NULL, 'Aduana', 1, 0, 8),
(661, 'Martha Alicia', 'García', 431, NULL, 'Aduana', 1, 1, 8),
(662, 'Lesly Yamilex ', 'Vazquez ', 10603, NULL, 'Aduana', 1, 1, 1),
(663, 'Aracely', 'Perez', 10303, NULL, 'Aduana', 1, 0, 2),
(664, 'Luis Fernando', 'Vazquez', 10747, NULL, 'Aduana', 1, 1, 2),
(665, 'Erika Michelle', 'Hernandez', 10774, NULL, 'Aduana', 1, 0, 1),
(666, 'Alondra Michelle', 'Gomez', 10760, NULL, 'Aduana', 1, 0, 4),
(667, 'Ma Isabel', 'Hernández', 896, NULL, 'Aduana', 1, 1, 4),
(668, 'Rubí Yesenia', 'López', 989, NULL, 'Aduana', 1, 0, 2),
(669, 'Miguel', 'Lares', 145, '#1234567#', 'Prueba', 1, 1, 1),
(670, 'Sabina', 'Gomez', 10537, '#5095636371#', 'Keeper', 1, 1, 3),
(671, 'Ana Yareli', 'Silvas', 10518, '#5360839639#', 'Operador', 1, 1, 8),
(673, 'Salvador ', 'Cuarenta', 453, '453', 'Supervisor', 1, 1, 1),
(674, 'María Anabel ', 'Hernández', 10597, '#3482630411#', 'Operador', 1, 1, 9),
(675, 'Daniel', 'Carlos', 10669, '#3866181245#', 'Operador', 1, 1, 5),
(676, 'Carlos Alberto', 'Miranda', 10636, '#2148746458#', 'Operador', 1, 1, 5),
(677, 'Gerardo', 'Calzada', 10850, '#6072841468#', 'Operador', 1, 1, 5),
(678, 'Brian Alejandro', 'Limon', 10569, '#8727073765#', 'Operador', 1, 1, 5),
(680, 'Arsenia', 'Gutierrez', 10764, '#8497948216#', 'Operador', 1, 1, 5),
(681, 'Mariana Itzel', 'Oliva', 10775, '#3500662833#', 'Operador', 1, 1, 9),
(683, 'Angel', 'Navarro', 10846, '#8157904859#', 'Operador', 1, 1, 15),
(684, 'Diana Marianela', 'Valle', 10841, '#9133119410#', 'Aduana', 1, 1, 5),
(685, 'Oscar Edgardo', 'Eligio Zuñiga', 821, NULL, 'Aduana', 1, 0, 5),
(686, 'Miguel Angel', 'Contreras', 10653, '#12345687#', 'Supervisor', 1, 1, 9),
(687, 'Arturo ', 'Enciso', 10847, '#6472738394#', 'Operador', 1, 1, 5),
(688, 'Aldo ', 'Vivian', 10907, '#1571381126#', 'Operador', 1, 1, 5),
(689, 'Irene', 'Lambarena Preciado', 10925, NULL, 'Aduana', 1, 0, 9),
(690, 'Alejandra', 'Flores', 10977, NULL, 'Aduana', 1, 0, 9),
(691, 'Victoria Nallely', 'Navarro', 10926, '#9664032868#', 'Aduana', 1, 1, 9),
(692, 'Olivia', 'Lara Ortíz', 10789, '#6005688660#', 'Operador', 1, 1, 9),
(694, 'Christian Ignacio', 'Rodriguez', 10577, '#3571909922#', 'Operador', 1, 1, 5),
(695, 'Mayra', 'Sanabria', 10547, '#8898708112#', 'Operador', 1, 1, 5),
(696, 'Maria Guadalupe', 'Lopez', 10955, '#2328404954#', 'Operador', 1, 1, 10),
(697, 'Lorena', 'Lucas', 10960, '#6222771490#', 'Operador', 1, 1, 10),
(698, 'Marisela', 'Zuñiga', 10944, '#7305352918#', 'Operador', 1, 1, 10),
(699, 'Jesica', 'Angel', 10942, '#7208553730#', 'Operador', 1, 1, 10),
(700, 'Ronaldo', 'Santos', 10567, '#3897122426#', 'Operador', 1, 1, 3),
(701, 'Alondra ', 'Padilla', 10808, '#1834603428#', 'Operador', 1, 1, 3),
(702, 'TB01', 'Prensa Bush', 1, 'TB01', 'Supervisor', 1, 1, 10),
(703, 'TB05', 'Prensa Bush', 5, 'TB05', 'Supervisor', 1, 1, 10),
(704, 'TB06', 'Prensa Bush', 6, 'TB06', 'Supervisor', 1, 1, 10),
(705, 'TB03B', 'Prensa Bush', 3, 'TB03B', 'Supervisor', 1, 1, 10),
(706, 'TB32', 'Coining Bush', 32, 'TB32', 'Supervisor', 1, 1, 13),
(707, 'TB03F', 'Forming Bush', 33, 'TB03F', 'Supervisor', 1, 1, 12),
(708, 'TB91', 'Bush Ensamble', 91, 'TB91', 'Supervisor', 1, 1, 11),
(709, 'Jonathan de Jesús', 'Neri', 11009, '#8446546751#', 'Operador', 1, 1, 5),
(710, 'Ronaldo', 'Galvez', 11024, '#2493659918#', 'Operador', 1, 1, 5),
(711, 'Ulises', 'Vazquez', 10995, '#5614767615#', 'Operador', 1, 1, 5),
(712, 'Yazmin', 'Barrera', 10992, NULL, 'Supervisor', 1, 1, 16),
(713, 'Rodrigo', 'Alcala', 10990, NULL, 'Supervisor', 1, 1, 16),
(714, 'Maria Guadalupe', 'Talavera', 10882, '#8239594743#', 'Operador', 1, 1, 16),
(715, 'Miguel Angel', 'Navarro', 11043, '#6373272566#', 'Operador', 1, 1, 5),
(716, 'Ruth', 'Mandujano', 10504, '1234', 'Aduana', 1, 1, 1),
(717, 'Bush', 'Seal Ring', 92, 'TB92', 'Supervisor', 1, 1, 17),
(718, 'Walter', 'Angulo', 10989, '#7310541002#', 'Supervisor', 1, 1, 14),
(720, 'Erasmo', 'Menecio', 11060, '#6873439865#', 'Operador', 1, 1, 5),
(721, 'Sergio Antonio', 'Diaz', 11028, '#7217465819#', 'Operador', 1, 1, 5),
(722, 'Juan Manuel', 'Gonzalez', 302394, '#4974237849#', 'Operador', 1, 1, 5),
(723, 'Erika del Carmen', 'Gonzalez', 11099, '#1398463694#', 'Operador', 1, 1, 5),
(724, 'Yolanda', 'Lopez', 11097, '#4597801895#', 'Operador', 1, 1, 5),
(725, 'Lorena', 'Gonzalez', 11071, '#8125469108#', 'Operador', 1, 1, 5),
(726, 'Paula', 'Gonzalez', 11029, '#8311249365#', 'Operador', 1, 1, 5),
(727, 'Larisa', 'García Rodriguez', 11128, NULL, 'Aduana', 1, 1, 10),
(728, 'Francisco Javier', 'Lopez', 11140, '#1781082407#', 'Operador', 1, 1, 5),
(729, 'Cirilo', 'Vazquez', 11141, '#5444807335#', 'Operador', 1, 0, 5),
(731, 'Esther', 'Morales', 10772, NULL, 'Supervisor', 1, 1, 16),
(732, 'Juan Carlos', 'Gonzalez', 11151, NULL, 'Aduana', 1, 1, 5),
(733, 'Alma Laura', 'Llagas', 10890, '#5532331945#\r\n', 'Operador', 1, 1, 16),
(734, 'ERNESTO CELESTINO', 'PRECIADO', 365, '#4973669679#', 'Operador', 1, 1, 2),
(735, 'UZIEL ', 'CABADILLA', 378, '#5969524998#', 'Operador', 1, 1, 2),
(736, 'ABRAHAM ', 'FLORES', 404, '#3344857141#', 'Operador', 1, 1, 2),
(737, 'OSIRIS ALAIN', 'MEZA', 498, '#3520293884#', 'Operador', 1, 1, 2),
(738, 'SERGIO ISRAEL', 'RAMIREZ', 716, '#2464508320#', 'Operador', 1, 1, 2),
(739, 'JORGE ALBERTO', 'VILLANUEVA', 730, '#9916952031#', 'Operador', 1, 1, 2),
(740, 'ANA MARIA', 'CUEVAS', 10265, '#5455785533#', 'Operador', 1, 1, 2),
(741, 'JAIME CRUZ', 'CORNEJO', 10279, '#8150555939#', 'Operador', 1, 1, 2),
(742, 'EDGAR ABRAHAM', 'HERNANDEZ', 10310, '#8307972696#', 'Operador', 1, 1, 2),
(743, 'MARTHA ALEJANDRA', 'CHAVIRA', 10617, '#7513690919#', 'Operador', 1, 1, 2),
(744, 'ERIKA ', 'ENCISO', 10644, '#1024897386#', 'Operador', 1, 1, 2),
(745, 'ELSA VERONICA', 'LAZO', 10685, '#5918757294#', 'Operador', 1, 1, 2),
(746, 'MARIA EDWIGES', 'MONROY', 10691, '#7819785055#', 'Operador', 1, 1, 2),
(747, 'ESMERALDA ', 'CHAVEZ', 10700, '#8412989332#', 'Operador', 1, 1, 2),
(748, 'MARIA DOLORES', 'LEDEZMA', 10702, '#5755084790#', 'Operador', 1, 1, 2),
(749, 'SERGIO ', 'IBARRA', 10834, '#7586298516#', 'Operador', 1, 1, 2),
(750, 'GLORIA ALICIA', 'CARRILLO', 10861, '#6425459613#', 'Operador', 1, 1, 2),
(751, 'VICTORIA JACQUELINE', 'LEDESMA', 10870, '#6774374929#', 'Operador', 1, 1, 2),
(752, 'ALEJANDRA NAYELI', 'HERNANDEZ', 10871, '#2246876385#', 'Operador', 1, 1, 2),
(753, 'MA. ELVIRA', 'JIMENEZ', 10979, '#8119891934#', 'Operador', 1, 1, 2),
(754, 'ALBERTO ', 'ZAMARRIPA', 10994, '#9825157115#', 'Operador', 1, 1, 2),
(755, 'VERONICA ', 'CHAVEZ', 10998, '#3792746305#', 'Operador', 1, 1, 2),
(756, 'ANA ISABEL', 'CHAVEZ', 11063, '#3752614225#', 'Operador', 1, 1, 2),
(757, 'YESENIA ZENAIDA', 'MEZA', 11066, '#9969829386#', 'Operador', 1, 1, 2),
(758, 'KAREN VIRIDIANA', 'MADRIZ', 11129, '#3775583519#', 'Operador', 1, 1, 2),
(759, 'LAURA ELIZABETH', 'VAZQUEZ', 11130, '#3487364824#', 'Operador', 1, 1, 2),
(760, 'KEVIN', 'CAZARES', 11059, '#7648391467#', 'Operador', 1, 1, 16),
(761, 'Liliana Guadalupe', 'Diosdado', 394, '#4140991369#', 'Keeper', 1, 0, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado_has_registro_rbp`
--

CREATE TABLE `empleado_has_registro_rbp` (
  `empleado_id_empleado` int(10) UNSIGNED NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `empleado_supervisor_id_empleado_supervisor` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `empleado_has_registro_rbp`
--

INSERT INTO `empleado_has_registro_rbp` (`empleado_id_empleado`, `registro_rbp_id_registro_rbp`, `empleado_supervisor_id_empleado_supervisor`) VALUES
(737, 31, 5),
(737, 31, 5),
(755, 31, 5),
(758, 32, 5),
(758, 32, 5),
(745, 33, 5),
(758, 34, 5),
(745, 33, 5),
(148, 35, 5),
(753, 37, 5),
(753, 36, 5),
(756, 37, 5),
(753, 36, 5),
(753, 36, 5),
(756, 37, 5),
(145, 39, 5),
(745, 40, 5),
(145, 41, 5),
(145, 39, 5),
(745, 39, 5),
(742, 42, 5),
(746, 44, 5),
(149, 43, 5),
(746, 44, 5),
(745, 46, 5),
(145, 45, 5),
(745, 45, 5),
(149, 47, 5),
(739, 47, 5),
(739, 48, 5),
(145, 3, 5),
(174, 49, 5),
(737, 50, 5),
(749, 51, 5),
(739, 51, 5),
(739, 52, 5),
(739, 52, 5),
(734, 53, 5),
(740, 53, 5),
(740, 54, 5),
(740, 54, 5),
(740, 54, 5),
(741, 54, 5),
(741, 54, 5),
(752, 56, 5),
(752, 56, 5),
(751, 57, 5),
(145, 59, 5),
(746, 58, 5),
(746, 58, 5),
(145, 59, 5),
(747, 58, 5),
(149, 60, 5),
(145, 60, 5),
(148, 61, 5),
(739, 62, 5),
(148, 63, 5),
(753, 64, 5),
(756, 65, 5),
(756, 65, 5),
(169, 66, 5),
(169, 66, 5),
(170, 69, 5),
(148, 68, 5),
(148, 68, 5),
(755, 70, 5),
(747, 71, 5),
(755, 70, 5),
(755, 71, 5),
(745, 72, 5),
(748, 73, 5),
(745, 72, 5),
(745, 73, 5),
(170, 74, 5),
(170, 74, 5),
(752, 75, 5),
(148, 75, 5),
(743, 76, 5),
(738, 77, 5),
(743, 77, 5),
(755, 78, 5),
(747, 78, 5),
(747, 79, 5),
(755, 79, 5),
(739, 80, 5),
(739, 81, 5),
(739, 80, 5),
(739, 81, 5),
(740, 82, 5),
(740, 83, 5),
(744, 83, 5),
(738, 84, 5),
(145, 84, 5),
(740, 85, 5),
(740, 85, 5),
(746, 86, 5),
(757, 87, 5),
(757, 87, 5),
(738, 88, 5),
(738, 88, 5),
(747, 89, 5),
(747, 89, 5),
(738, 90, 5),
(738, 90, 5),
(738, 91, 5),
(738, 91, 5),
(742, 92, 5),
(742, 92, 5),
(174, 93, 5),
(746, 94, 5),
(738, 94, 5),
(755, 95, 5),
(753, 96, 5),
(174, 97, 5),
(753, 97, 5),
(756, 97, 5),
(740, 99, 5),
(740, 99, 5),
(735, 100, 5),
(735, 100, 5),
(740, 100, 5),
(755, 101, 5),
(755, 101, 5),
(743, 102, 5),
(746, 104, 5),
(740, 103, 5),
(745, 103, 5),
(756, 105, 5),
(744, 105, 5),
(747, 106, 5),
(740, 107, 5),
(744, 107, 5),
(745, 108, 5),
(169, 109, 5),
(169, 109, 5),
(758, 111, 5),
(752, 112, 5),
(745, 112, 5),
(738, 111, 5),
(745, 110, 5),
(738, 113, 5),
(745, 114, 5),
(748, 115, 5),
(737, 116, 5),
(741, 117, 5),
(757, 118, 5),
(757, 118, 5),
(753, 119, 5),
(753, 119, 5),
(758, 120, 5),
(749, 121, 5),
(748, 121, 5),
(756, 122, 5),
(756, 122, 5),
(752, 123, 5),
(752, 123, 5),
(737, 123, 5),
(755, 124, 5),
(755, 124, 5),
(758, 125, 5),
(737, 125, 5),
(746, 126, 5),
(746, 126, 5),
(66, 128, 1),
(69, 129, 3),
(69, 129, 3),
(42, 131, 2),
(42, 131, 2),
(42, 131, 2),
(35, 132, 2),
(35, 132, 2),
(47, 134, 2),
(27, 135, 2),
(48, 135, 2),
(131, 139, 2),
(131, 139, 2),
(131, 139, 2),
(42, 141, 2),
(122, 142, 2),
(123, 142, 2),
(58, 143, 2),
(58, 143, 2),
(58, 143, 2),
(66, 144, 2),
(747, 147, 5),
(740, 148, 5),
(740, 148, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(745, 149, 5),
(174, 150, 5),
(66, 152, 2),
(122, 152, 2),
(745, 154, 5),
(745, 154, 5),
(757, 155, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(170, 156, 5),
(131, 157, 2),
(35, 158, 2),
(35, 158, 2),
(738, 159, 5),
(738, 159, 5),
(736, 160, 5),
(148, 160, 5),
(745, 160, 5),
(750, 161, 5),
(752, 162, 5),
(751, 162, 5),
(748, 162, 5),
(737, 163, 5),
(737, 165, 5),
(148, 166, 5),
(42, 167, 2),
(42, 167, 2),
(42, 167, 2),
(131, 168, 2),
(131, 168, 2),
(174, 169, 5),
(751, 169, 5),
(735, 170, 5),
(169, 170, 5),
(737, 170, 5),
(742, 171, 5),
(759, 171, 5),
(755, 172, 5),
(745, 172, 5),
(736, 173, 5),
(169, 173, 5),
(746, 174, 5),
(122, 180, 2),
(31, 180, 2),
(58, 181, 2),
(45, 186, 2),
(34, 187, 2),
(701, 190, 4),
(28, 191, 2),
(28, 192, 2),
(28, 194, 2),
(28, 196, 2),
(28, 197, 2),
(47, 198, 2),
(28, 199, 2),
(28, 200, 2),
(17, 204, 2),
(25, 205, 2),
(25, 206, 2),
(19, 207, 2),
(19, 208, 2),
(18, 211, 2),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(162, 213, 5),
(31, 215, 2),
(735, 151, 5),
(735, 151, 5),
(735, 151, 5),
(735, 151, 5),
(734, 151, 5),
(734, 151, 5),
(735, 214, 5),
(735, 214, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado_supervisor`
--

CREATE TABLE `empleado_supervisor` (
  `id_empleado_supervisor` int(10) UNSIGNED NOT NULL,
  `empleado_id_empleado` int(10) UNSIGNED NOT NULL,
  `procesos_idproceso` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `empleado_supervisor`
--

INSERT INTO `empleado_supervisor` (`id_empleado_supervisor`, `empleado_id_empleado`, `procesos_idproceso`) VALUES
(1, 1, 9),
(2, 2, 1),
(3, 3, 1),
(4, 647, 16),
(5, 600, 2),
(6, 311, 12),
(8, 138, 10),
(9, 377, 9),
(10, 391, 6),
(11, 645, 7),
(12, 4, 12),
(13, 641, 8),
(14, 649, 3),
(15, 650, 3),
(19, 642, 13),
(25, 643, 14),
(32, 654, 1),
(33, 653, 8),
(47, 100, 1),
(48, 169, 2),
(49, 170, 2),
(50, 673, 1),
(53, 519, 14),
(54, 5, 15),
(56, 702, 10),
(57, 703, 10),
(58, 704, 10),
(59, 705, 10),
(60, 706, 13),
(61, 707, 12),
(62, 708, 11),
(63, 717, 17),
(64, 385, 9),
(65, 718, 9),
(66, 214, 16),
(67, 731, 16);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lote_coil`
--

CREATE TABLE `lote_coil` (
  `id_lote_coil` int(10) UNSIGNED NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `lote_coil` varchar(20) NOT NULL,
  `metros` double DEFAULT NULL,
  `f_terminado` tinyint(1) DEFAULT NULL,
  `das_id_das` int(10) DEFAULT NULL,
  `mog_id_mog` int(11) DEFAULT NULL,
  `tiempo_insercion` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `lote_coil`
--

INSERT INTO `lote_coil` (`id_lote_coil`, `registro_rbp_id_registro_rbp`, `lote_coil`, `metros`, `f_terminado`, `das_id_das`, `mog_id_mog`, `tiempo_insercion`) VALUES
(1, 1, 'E23659-31', 200, 0, 2, 1, '14:26:19'),
(2, 12, 'E235689-32', 2000, 1, 2, 12, '17:05:49'),
(3, 16, 'TYEFD-31', 120, 0, 4, 16, '07:52:59'),
(4, 17, 'E235689-31', 300, 1, 6, 17, '14:48:01'),
(5, 20, 'E1225996-32', 2000, 0, 8, 20, '16:03:51'),
(6, 12, 'ASADFSD-32', 12, 0, 9, 12, '16:09:14'),
(7, 12, 'DSADS-31', 120, 0, 10, 12, '16:15:18'),
(8, 21, 'G2201401-31', 213, 0, 12, 21, '10:38:49'),
(9, 4, 'E1236649-31', 200, 0, 14, 4, '18:03:36'),
(10, 4, 'E1236649-31', 300, 1, 14, 4, '18:06:59'),
(11, 25, 'nfo0000-31', 100, 0, 15, 25, '18:19:28'),
(12, 25, 'nfo0000-31', 4646, 1, 15, 25, '18:22:31'),
(13, 27, 'G2081801-31', 229, 0, 18, 27, '11:10:58'),
(14, 26, 'A2056501-31', 44, 0, 20, 26, '11:21:12'),
(15, 26, 'A2056501-31', 1639, 0, 22, 26, '11:26:27'),
(16, 31, 'A2020701-31', 602, 1, 2, 31, '15:55:06'),
(17, 31, 'A2020601-31', 178, 0, 2, 31, '16:00:30'),
(18, 31, 'A2020601-31', 1058, 0, 2, 31, '16:06:53'),
(19, 32, 'A2020601-31', 383, 1, 4, 32, '16:10:30'),
(20, 32, 'L2211902-31', 1439, 0, 4, 32, '16:23:41'),
(21, 34, 'F2211701-31', 1749, 1, 5, 34, '11:52:36'),
(22, 33, 'L2211902-31', 1026, 0, 7, 33, '11:54:11'),
(23, 33, 'L2211902-31', 802, 0, 10, 33, '12:06:41'),
(24, 35, 'E22365214-31', 200, 0, 11, 35, '18:08:28'),
(25, 37, 'A2379101-31', 461, 0, 12, 37, '13:43:05'),
(26, 36, 'B2058801-31', 486, 1, 15, 36, '13:45:28'),
(27, 37, 'A2379101-31', 1530, 1, 13, 37, '13:49:12'),
(28, 36, 'A2349401-31', 1039, 1, 15, 36, '13:51:23'),
(29, 36, 'A2379101-31', 506, 0, 15, 36, '13:54:40'),
(30, 39, 'D2197201-31', 528, 1, 21, 39, '14:12:59'),
(31, 40, 'E2027801-31', 1904, 1, 19, 40, '14:13:12'),
(32, 41, 'D2197201-31', 1909, 0, 24, 41, '14:21:26'),
(33, 39, 'E2027801-31', 597, 0, 21, 39, '14:23:37'),
(34, 39, 'E2027801-31', 786, 0, 21, 39, '14:28:04'),
(35, 44, 'G9005702-31', 1114, 0, 28, 44, '11:53:00'),
(36, 43, 'G9038201-31', 1927, 0, 30, 43, '11:53:15'),
(37, 44, 'G9005702-31', 348, 0, 28, 44, '11:59:47'),
(38, 46, 'E2185401-31', 2050, 1, 32, 46, '13:55:09'),
(39, 45, 'E2027701-31', 1187, 1, 34, 45, '13:55:41'),
(40, 45, 'E2185401-31', 735, 0, 34, 45, '15:17:15'),
(41, 47, 'G9006201-31', 1109, 1, 36, 47, '11:24:22'),
(42, 47, 'F9066202-31', 390, 0, 36, 47, '11:48:13'),
(43, 48, 'F9066202-31', 1434, 0, 38, 48, '12:11:48'),
(44, 3, 'R596464-31', 200, 0, 40, 3, '18:00:54'),
(45, 3, 'E13235656-31', 200, 0, 40, 3, '18:03:31'),
(46, 49, 'E235689-31', 200, 0, 42, 49, '18:08:12'),
(47, 50, 'D2301801-31', 358, 0, 44, 50, '12:26:58'),
(48, 51, 'F9112502-31', 1145, 0, 46, 51, '15:42:17'),
(49, 51, 'F9112502-31', 374, 0, 46, 51, '16:02:09'),
(50, 52, 'F9112502-31', 907, 1, 46, 52, '16:12:11'),
(51, 52, 'F9112404-31', 589, 0, 46, 52, '16:19:24'),
(52, 53, 'D2290601-31', 1311, 0, 48, 53, '15:23:12'),
(53, 53, 'D2290601-31', 554, 0, 48, 53, '15:28:14'),
(54, 54, 'C2184801-32', 221, 1, 50, 54, '15:32:15'),
(55, 54, 'C2184901-32', 261, 1, 50, 54, '15:35:45'),
(56, 54, 'D2290701-31', 394, 0, 50, 54, '15:40:06'),
(57, 54, 'D2290701-31', 199, 1, 50, 54, '15:45:19'),
(58, 54, 'D2290601-31', 810, 0, 50, 54, '15:51:28'),
(59, 56, 'H2235001-31', 421, 1, 52, 56, '15:41:51'),
(60, 56, 'H2260901-31', 1487, 0, 52, 56, '15:47:42'),
(61, 57, 'H2260901-31', 1853, 1, 52, 57, '15:56:30'),
(62, 59, 'G9079801-31', 483, 1, 56, 59, '10:24:29'),
(63, 58, 'H2234901-31', 622, 1, 55, 58, '10:28:13'),
(64, 58, 'H2261001-31', 1033, 0, 55, 58, '13:00:19'),
(65, 59, 'G9079802-31', 1468, 0, 56, 59, '15:33:29'),
(66, 58, 'H2261001-31', 103, 1, 55, 58, '16:50:31'),
(67, 61, 'G2354001-31', 954, 0, 60, 61, '11:45:57'),
(68, 60, 'H9093202-31', 1234, 1, 58, 60, '12:51:21'),
(69, 60, 'H9092601-31', 1366, 1, 62, 60, '14:03:02'),
(70, 62, 'H2048201-31', 893, 1, 64, 62, '15:17:53'),
(71, 63, 'H2091201-31', 930, 0, 66, 63, '15:31:43'),
(72, 64, 'I2370301-31', 1822, 0, 68, 64, '16:06:44'),
(73, 65, 'I2370301-31', 1306, 1, 70, 65, '16:09:57'),
(74, 65, 'H2152301-31', 286, 1, 70, 65, '16:17:14'),
(75, 66, 'G2337301-31', 1531, 1, 71, 66, '15:51:14'),
(76, 66, 'G2273901-31', 402, 0, 72, 66, '15:55:39'),
(77, 69, 'I2041101-31', 1932, 1, 74, 69, '10:21:43'),
(78, 68, 'H2281001-31', 1264, 0, 76, 68, '10:44:34'),
(79, 68, 'H2281001-31', 395, 0, 76, 68, '11:08:50'),
(80, 70, 'I2058901-31', 1528, 1, 78, 70, '15:35:33'),
(81, 71, 'I2317001-31', 1468, 0, 80, 71, '15:38:11'),
(82, 70, 'I2059001-31', 166, 0, 78, 70, '15:41:22'),
(83, 71, 'I2317001-31', 398, 1, 80, 71, '15:45:02'),
(84, 72, 'B2091101-31', 894, 1, 82, 72, '14:35:10'),
(85, 73, 'B2016201-31', 1145, 0, 84, 73, '14:37:03'),
(86, 72, 'F2183830-31', 818, 0, 82, 72, '14:41:38'),
(87, 73, 'B2016201-31', 299, 0, 84, 73, '14:42:59'),
(88, 75, 'I2213601-31', 74, 0, 87, 75, '11:28:08'),
(89, 75, 'I2213601-31', 867, 0, 87, 75, '11:37:00'),
(90, 76, 'I2213601-31', 916, 0, 87, 76, '11:43:26'),
(91, 77, 'I2213701-31', 731, 0, 89, 77, '13:36:28'),
(92, 77, 'I2213701-31', 264, 1, 89, 77, '14:24:24'),
(93, 78, 'I2287201-31', 1309, 0, 91, 78, '11:40:48'),
(94, 78, 'I2287201-31', 616, 0, 91, 78, '11:44:16'),
(95, 79, 'I2164501-31', 1603, 0, 93, 79, '12:35:05'),
(96, 79, 'I2164501-31', 470, 1, 95, 79, '12:40:07'),
(97, 80, 'H9001301-31', 421, 0, 97, 80, '12:01:54'),
(98, 81, 'H9001301-31', 466, 1, 99, 81, '12:03:55'),
(99, 80, 'H9001301-31', 1050, 0, 97, 80, '12:06:50'),
(100, 81, 'H9088302-31', 699, 0, 99, 81, '12:07:54'),
(101, 82, 'J2170401-31', 1752, 0, 102, 82, '15:47:22'),
(102, 83, 'G2206501-31', 1448, 1, 103, 83, '15:47:38'),
(103, 83, 'G2137201-31', 468, 0, 103, 83, '15:52:21'),
(104, 84, 'H9001302-31', 1266, 0, 105, 84, '12:32:09'),
(105, 84, 'H9001302-31', 193, 0, 105, 84, '12:38:01'),
(106, 85, 'I2254001-31', 191, 0, 107, 85, '14:35:10'),
(107, 85, 'I2254001-31', 717, 0, 107, 85, '14:38:45'),
(108, 86, 'I9145801-31', 1759, 0, 109, 86, '15:49:34'),
(109, 87, 'I2446501-31', 899, 0, 111, 87, '15:54:49'),
(110, 87, 'I2446501-31', 1018, 0, 111, 87, '16:02:57'),
(111, 88, 'J9008101-31', 516, 0, 113, 88, '16:10:47'),
(112, 88, 'J9008101-31', 1165, 0, 113, 88, '16:15:36'),
(113, 89, 'J2465201-31', 757, 1, 115, 89, '15:49:29'),
(114, 89, 'J2182301-31', 1160, 0, 115, 89, '15:51:57'),
(115, 90, 'I2144701-31', 721, 1, 117, 90, '12:38:54'),
(116, 90, 'I2144801-31', 887, 0, 117, 90, '12:44:01'),
(117, 91, 'I2279201-31', 157, 1, 119, 91, '12:55:20'),
(118, 91, 'I2279201-31', 157, 1, 119, 91, '12:57:20'),
(119, 92, 'H2103101-31', 497, 0, 121, 92, '15:35:57'),
(120, 93, 'I2204601-31', 931, 0, 123, 93, '15:36:51'),
(121, 92, 'H2103101-31', 1222, 1, 121, 92, '15:41:41'),
(122, 94, 'H2103801-31', 1795, 1, 125, 94, '12:05:54'),
(123, 95, 'I2369301-31', 1890, 0, 127, 95, '12:17:59'),
(124, 96, 'H2103001-31', 1005, 1, 129, 96, '15:02:21'),
(125, 97, 'I2436201-31', 448, 1, 131, 97, '15:26:45'),
(126, 97, 'I2205801-31', 435, 0, 131, 97, '15:30:21'),
(127, 97, 'I2205801-31', 927, 0, 131, 97, '15:35:59'),
(128, 99, 'J2130801-31', 2666, 1, 133, 99, '14:24:13'),
(129, 99, 'J2265701-31', 245, 0, 133, 99, '14:27:52'),
(130, 100, 'J2265701-31', 809, 0, 135, 100, '14:45:59'),
(131, 100, 'J2265701-31', 681, 1, 135, 100, '14:49:55'),
(132, 100, 'J2188201-31', 418, 0, 135, 100, '14:53:33'),
(133, 101, 'J9067701-31', 638, 1, 137, 101, '12:33:13'),
(134, 101, 'I9118101-31', 321, 1, 137, 101, '12:41:45'),
(135, 102, 'C2014001-31', 1157, 1, 139, 102, '12:45:47'),
(136, 104, 'J2077001-31', 1378, 1, 141, 104, '14:54:27'),
(137, 103, 'J2518501-31', 1423, 0, 143, 103, '15:00:09'),
(138, 103, 'J2518501-31', 179, 0, 143, 103, '15:03:34'),
(139, 105, 'K2380201-31', 1414, 0, 145, 105, '14:48:11'),
(140, 105, 'K2380201-31', 588, 0, 145, 105, '14:52:02'),
(141, 106, 'K2508601-31', 978, 1, 147, 106, '14:56:40'),
(142, 107, 'K2141101-31', 745, 1, 149, 107, '11:42:14'),
(143, 107, 'K2475401-31', 1194, 0, 149, 107, '11:46:06'),
(144, 108, 'K2131101-31', 1914, 0, 151, 108, '10:35:18'),
(145, 109, 'D2197501-31', 964, 1, 155, 109, '16:52:49'),
(146, 109, 'D2197301-31', 495, 0, 155, 109, '17:05:17'),
(147, 111, 'C9156801-31', 1047, 1, 157, 111, '08:55:39'),
(148, 112, 'B2258701-31', 1292, 0, 161, 112, '14:40:01'),
(149, 111, 'C9174702-31', 881, 0, 161, 111, '14:47:29'),
(150, 110, 'B2258701-31', 971, 1, 161, 110, '14:55:29'),
(151, 113, 'C9156802-31', 213, 0, 162, 113, '13:25:41'),
(152, 115, 'C2189401-31', 157, 1, 166, 115, '20:01:52'),
(153, 116, 'A2365301-31', 2057, 1, 166, 116, '20:05:28'),
(154, 117, 'A2461001-31', 816, 1, 168, 117, '04:43:27'),
(155, 118, 'B2308901-31', 707, 1, 168, 118, '04:51:10'),
(156, 118, 'B2560901-31', 212, 0, 168, 118, '04:54:35'),
(157, 119, 'C2304701-31', 615, 1, 170, 119, '08:59:35'),
(158, 119, 'C2304501-31', 1293, 0, 170, 119, '09:04:47'),
(159, 120, 'D9028302-31', 1420, 0, 172, 120, '10:40:01'),
(160, 121, 'C2074301-31', 1076, 1, 172, 121, '15:40:24'),
(161, 121, 'C2074201-31', 657, 0, 172, 121, '15:42:38'),
(162, 122, 'C2457601-31', 434, 1, 172, 122, '15:49:45'),
(163, 122, 'C2528301-31', 991, 0, 172, 122, '15:54:54'),
(164, 123, 'E9130401-31', 826, 1, 172, 123, '15:59:50'),
(165, 123, 'E9130102-31', 395, 0, 172, 123, '16:06:05'),
(166, 123, 'E9130102-31', 149, 1, 172, 123, '16:09:06'),
(167, 124, 'D9028302-31', 923, 0, 172, 124, '16:24:10'),
(168, 124, 'D9028302-31', 481, 0, 172, 124, '16:29:01'),
(169, 125, 'D9038401-311', 801, 0, 174, 125, '08:31:24'),
(170, 125, 'C9159901-31', 231, 0, 174, 125, '08:44:05'),
(171, 126, 'C9159901-31', 768, 1, 174, 126, '09:21:27'),
(172, 126, 'C9159802-31', 569, 0, 174, 126, '09:25:41'),
(173, 140, 'E9002702-31', 1928, 0, 183, 140, '04:37:44'),
(174, 147, 'D2243701-31', 908, 0, 191, 147, '15:07:04'),
(175, 148, 'D2154101-31', 1761, 1, 192, 148, '15:19:46'),
(176, 148, 'D227380121-31', 197, 0, 192, 148, '15:37:16'),
(177, 149, 'D2552701-31', 689, 1, 194, 149, '16:38:31'),
(178, 150, 'C2564301-32', 255, 1, 196, 150, '17:20:55'),
(179, 154, 'J2491901-31', 535, 1, 200, 154, '08:51:46'),
(180, 154, 'C2490301-31', 487, 0, 200, 154, '09:03:04'),
(181, 155, 'D2527301-31', 628, 0, 201, 155, '09:11:27'),
(182, 156, 'E2074401-31', 1022, 1, 202, 156, '09:21:41'),
(183, 156, 'E2439601-31', 865, 0, 202, 156, '09:26:52'),
(184, 159, 'E2439801-31', 702, 1, 207, 159, '08:27:57'),
(185, 159, 'E2439701-31', 1154, 0, 207, 159, '08:31:08'),
(186, 160, 'D2405601-31', 671, 1, 208, 160, '09:22:46'),
(187, 160, 'D2406001-31', 93, 0, 208, 160, '09:52:00'),
(188, 160, 'D2406001-31', 163, 0, 208, 160, '09:55:44'),
(189, 161, 'D2527301-31', 786, 1, 209, 161, '12:23:56'),
(190, 162, 'D2191701-31', 539, 1, 210, 162, '12:34:42'),
(191, 162, 'D2153701-31', 418, 1, 210, 162, '12:43:42'),
(192, 163, 'D9131201-31', 1395, 0, 212, 163, '08:42:30'),
(193, 165, 'D9131201-31', 1405, 0, 212, 165, '18:26:12'),
(194, 166, 'D2118001-31', 926, 0, 213, 166, '18:30:53'),
(195, 169, 'C9204301-31', 910, 0, 218, 169, '14:10:48'),
(196, 169, 'C9204301-31', 525, 0, 218, 169, '14:19:42'),
(197, 170, 'E2126301-31', 722, 0, 218, 170, '15:34:36'),
(198, 170, 'E2126301-31', 188, 0, 219, 170, '15:53:23'),
(199, 170, 'E2424601-31', 528, 0, 219, 170, '16:03:44'),
(200, 171, 'F2035001-31', 1397, 0, 219, 171, '16:16:57'),
(201, 171, 'E2287101-31', 485, 0, 220, 171, '16:22:32'),
(202, 172, 'F9186301-31', 1096, 1, 223, 172, '13:42:11'),
(203, 172, 'F9136101-31', 313, 0, 223, 172, '14:28:45'),
(204, 173, 'E2033301-31', 319, 0, 224, 173, '15:12:54'),
(205, 174, 'D2212101-31', 338, 0, 226, 174, '14:22:10'),
(206, 213, '3223243-33', 3, 0, 255, 212, '11:31:19'),
(207, 151, '515151515-31', 1, 0, 259, 151, '12:28:05'),
(208, 151, '151516-31', 0, 0, 261, 151, '14:32:58'),
(209, 151, '15151515-31', 0, 0, 262, 151, '14:41:40'),
(210, 151, '151515-31', 1, 0, 263, 151, '14:59:54'),
(211, 214, '2256165-31', 1, 0, 264, 213, '14:44:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mog`
--

CREATE TABLE `mog` (
  `id_mog` int(11) NOT NULL,
  `mog` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `descripcion` varchar(40) COLLATE utf8_spanish_ci NOT NULL,
  `num_dibujo` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `no_parte` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `modelo` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `STD` varchar(15) COLLATE utf8_spanish_ci DEFAULT NULL,
  `cantidad_planeada` int(11) NOT NULL,
  `peso` double DEFAULT NULL,
  `largo` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `mog`
--

INSERT INTO `mog` (`id_mog`, `mog`, `descripcion`, `num_dibujo`, `no_parte`, `modelo`, `STD`, `cantidad_planeada`, `peso`, `largo`) VALUES
(1, 'MOG001676', 'HB I4 M/N L', 'A-291306-1', '6M8G6A338CB', 'I4 M/N L', 'STD 3', 21000, 33.2, 1.1),
(2, 'MOG001677', 'HB I4 M/N L', 'A-291306-1', '6M8G6A338CB', 'I4 M/N L', 'STD 3', 20000, 33.2, 1.1),
(3, 'MOG002711', 'HB I4 M/N L', 'A-291306-1', '6M8G6A338CB', 'I4 M/N L', 'STD 3', 20000, 33.2, 1.1),
(4, 'MOG001622', 'HB 5A2 (AP4)', 'K-171720', '132145A2A010', '5A2 (AP4)', 'STD D', 17000, 16.29, 1.1),
(5, 'MOG002712', 'HB I4 M/N L', 'A-291306-1', '6M8G6A338CB', 'I4 M/N L', 'STD 3', 20000, 33.2, 1.1),
(6, 'MOG002713', 'HB I4 M/N L', 'A-291306-1', '6M8G6A338CB', 'I4 M/N L', 'STD 3', 20000, 33.2, 1.1),
(7, 'MOG002684', 'HB 5A2 (AP4)', 'K-171720', '132155A2A010', '5A2 (AP4)', 'STD E', 20000, 16.29, 1.1),
(8, 'MOG003254', 'HB GTDI M/N L', 'A-291365-1', '9E5G6A338CA', 'GTDI M/N L', 'STD 3', 20000, 32.9, 1.1),
(9, 'MOG008374', 'HB AP4 C/R STD D', 'K-171720-4', '13214-5A2-A010-M1', 'AP4 C/R STD D', 'STD D', 20000, 16.4, 1.1),
(10, 'MOG001485', 'HB I4 C/R STD 1', 'A-291308-01', '3S7G6211AA', 'I4 C/R STD 1', 'STD 1', 18500, 14.9, 1.1),
(11, 'MOG004139', 'HB AP4 C/R STD D', 'K-171720-4', '13214-5A2-A010-M1', 'AP4 C/R STD D', 'STD D', 20000, 16.4, 1.1),
(12, 'MOG015754', 'HB TR M/N U', 'A-155947', '122154KH4A', 'TR M/N U', 'STD 4', 20000, 28.5, 1.1),
(13, 'MOG003437', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(14, 'MOG001852', 'HB AP4 M/N L STD A', 'A-170808-1', '13341-RPYX-G000', 'AP4 M/N L STD A', 'STD A', 2000, 28.2, 1.1),
(15, 'MOG009622', 'HB XH C/R U STD 4', 'A-155993', '12110-5RB4A', 'XH C/R U STD 4', 'STD 4', 20000, 11.5, 1.1),
(16, 'MOG001375', 'HB I4 C/R STD 2', 'A-291308-01', '3S7G6211BA', 'I4 C/R STD 2', 'STD 2', 24000, 14.9, 1.1),
(17, 'MOG001376', 'HB I4 B/M STD', 'A-290909-2', '8E5G6K347AA', 'I4 B/M STD', 'STD', 33500, 9.64, 1.1),
(18, 'MOG015575', 'HB HUR M/N U STD 2', 'A-215296-00', '04893530AA', 'HUR M/N U STD 2', 'STD 2', 3360, 29.9, 1.1),
(19, 'MOG015579', 'HB HUR M/N L STD 3', 'A-215297-00', '04893535AA', 'HUR M/N L STD 3', 'STD 3', 4200, 29.9, 1.1),
(20, 'MOG045435', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(21, 'MOG045385', 'HB PY C/R STD 2', 'A-175950-01', 'PX13 11 226', 'PY C/R STD 2', 'STD 2', 20000, 15.8, 1.1),
(22, 'MOG032388', 'HB AP2 TC C/R STD E', 'K-171922-2', '13215-59B-0031', 'AP2 TC C/R STD E', 'STD E', 20000, 10.75, 1.1),
(23, 'MOG005542', 'HB PENTASTARC/RSTD1', 'A-215253', '04893951AA', 'PENTASTARC/RSTD1', 'STD 1', 20000, 19.3, 1.1),
(24, 'MOG001569', 'HB RNA (NP4)', 'A-170507-1', '13214RNAA010', 'RNA (NP4)', 'STD D', 24500, 12.83, 1.1),
(25, 'MOG007041', 'HB TR C/R STD 2', 'A-155995-00', '121114KH2A', 'TR C/R STD 2', 'STD 2', 20000, 15.9, 1.1),
(26, 'MOG045312', 'HB TR C/R STD 1', 'A-155995-00', '121114KH1A', 'TR C/R STD 1', 'STD 1', 20000, 15.9, 1.1),
(27, 'MOG045101', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 5000, 10.8, 1.1),
(28, 'MOG045402', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(29, 'MOG045406', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(30, 'MOG045430', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(31, 'MOG045455', 'HB ZV5K4 C/R STD 2', 'A-156007', '12111 6KA2A', 'ZV5K4 C/R STD 2', 'STD 2', 20000, 16.4, 1.1),
(32, 'MOG045549', 'HB ZV5K4 C/R STD 1', 'A-156007', '12111 6KA1A', 'ZV5K4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(33, 'MOG045550', 'HB ZV5K4 C/R STD 1', 'A-156007', '12111 6KA1A', 'ZV5K4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(34, 'MOG045547', 'HB PY M/N L STD 4', 'A-175949', 'PX13 11 854', 'PY M/N L STD 4', 'STD 4', 20000, 31.1, 1.1),
(35, 'MOG002721', 'HB R70 (NP0 C/R)', 'K-171698', '13214R70D010', 'R70 (NP0 C/R)', 'STD D', 20000, 17.37, 1.1),
(36, 'MOG045545', 'HB AP4 M/N U STD C', 'A-170807-03', '13323-RPY-G021-M1', 'AP4 M/N U STD C', 'STD C', 20000, 28.2, 1.1),
(37, 'MOG045546', 'HB AP4 M/N U STD C', 'A-170807-03', '13323-RPY-G021-M1', 'AP4 M/N U STD C', 'STD C', 20000, 28.2, 1.1),
(38, 'MOG045414', 'HB TR M/N L', 'A-155949-1', '122234KH3A', 'TR M/N L', 'STD 3', 20000, 28.5, 1.1),
(39, 'MOG045518', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(40, 'MOG045552', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(41, 'MOG045521', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(42, 'MOG004222', 'HB TR M/N L', 'A-155949-1', '122234KH3A', 'TR M/N L', 'STD 3', 20000, 28.5, 1.1),
(43, 'MOG045419', 'HB NP0 C/R STD D', 'A-171698-01', '13214-R70-D011-M1', 'NP0 C/R STD D', 'STD D', 20000, 17.8, 1.1),
(44, 'MOG044882', 'HB AP2 TC C/R STD C', 'K-171922-2', '13213-59B-0031', 'AP2 TC C/R STD C', 'STD C', 20000, 10.75, 1.1),
(45, 'MOG045614', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(46, 'MOG045616', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(47, 'MOG045737', 'HB AP2 TC C/R STD C', 'K-171922-2', '13213-59B-0031', 'AP2 TC C/R STD C', 'STD C', 20000, 10.75, 1.1),
(48, 'MOG045738', 'HB AP2 TC C/R STD C', 'K-171922-2', '13213-59B-0031', 'AP2 TC C/R STD C', 'STD C', 20000, 10.75, 1.1),
(49, 'MOG005867', 'HB NP0 C/R STD D', 'A-171698-00', '13214R70D010M1', 'NP0 C/R STD D', 'STD D', 20000, 17.8, 1.1),
(50, 'MOG045598', 'HB I4 UP M/N L STD2', 'A-292794', 'K2GE-6A338-BA', 'I4 UP M/N L STD2', 'STD 2', 20000, 33, 1.1),
(51, 'MOG045812', 'HB AP2 TC C/R STD C', 'K-171922-2', '13213-59B-0031', 'AP2 TC C/R STD C', 'STD C', 20000, 10.75, 1.1),
(52, 'MOG045857', 'HB AP2 TC C/R STD C', 'K-171922-2', '13213-59B-0031', 'AP2 TC C/R STD C', 'STD C', 20000, 10.75, 1.1),
(53, 'MOG045818', 'HB PENTASTARC/RSTD2', 'A-215253', '04893952AA', 'PENTASTARC/RSTD2', 'STD 2', 20000, 19.3, 1.1),
(54, 'MOG045589', 'HB PENTASTARC/RSTD3', 'A-215253', '04893953AA', 'PENTASTARC/RSTD3', 'STD 3', 20000, 19.3, 1.1),
(55, 'MOG045353', 'HB TR M/N U', 'A-155947', '122154KH3A', 'TR M/N U', 'STD 3', 20000, 28.5, 1.1),
(56, 'MOG045901', 'HB HELLCAT C/R', 'A-215252-1', '53010881AA', 'HELLCAT C/R', 'STD', 20000, 21.7, 1.1),
(57, 'MOG045902', 'HB HELLCAT C/R', 'A-215252-1', '53010881AA', 'HELLCAT C/R', 'STD', 20000, 21.7, 1.1),
(58, 'MOG045975', 'HB HELLCAT C/R', 'A-215252-1', '53010881AA', 'HELLCAT C/R', 'STD', 20000, 21.7, 1.1),
(59, 'MOG045889', 'HB NP0 C/R STD C', 'A-171698-01', '13213-R70-D011-M1', 'NP0 C/R STD C', 'STD C', 20000, 17.8, 1.1),
(60, 'MOG046037', 'HB NP0 M/N L STD C', 'K-171787-02', '13343-R9P-A011-M1', 'NP0 M/N L STD C', 'STD C', 20000, 47.2, 1.1),
(61, 'MOG046031', 'HB CSS 675T B/C STD', 'A-223062-03', '12665019', 'CSS 675T B/C STD', 'STD', 20000, 9.4, 1.1),
(62, 'MOG045995', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(63, 'MOG046029', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(64, 'MOG046134', 'HB I4GTDI C/RU STD2', 'A-292795-3', 'K2GE-6211-BB', 'I4GTDI C/RU STD2', 'STD 2', 20000, 16.4, 1.1),
(65, 'MOG046164', 'HB I4GTDI C/RU STD2', 'A-292795-3', 'K2GE-6211-BB', 'I4GTDI C/RU STD2', 'STD 2', 20000, 16.4, 1.1),
(66, 'MOG046079', 'HB COYOTE C/R U STD', 'A-292751-1', 'JR3E-6211-CA', 'COYOTE C/R U STD', 'STD', 20000, 27, 1.1),
(67, 'MOG045597', 'HB XH M/N U STD 1', 'A-155991', '12215-5RB1A', 'XH M/N U STD 1', 'STD 1', 20000, 20.1, 1.1),
(68, 'MOG046293', 'HB 5R0(AP2)M/NUSTDD', 'A-170840-02-DPMS', '13324-5R0-0032-DPMS', '5R0(AP2)M/NUSTDD', 'STD D', 20000, 20.4, 1.1),
(69, 'MOG046187', 'HB PENTASTARC/RSTD2', 'A-215253', '04893952AA', 'PENTASTARC/RSTD2', 'STD 2', 20000, 19.3, 1.1),
(70, 'MOG046416', 'HB 5R0(AP2)M/NUSTDE', 'A-170840-02-DPMS', '13325-5R0-0032-DPMS', '5R0(AP2)M/NUSTDE', 'STD E', 20000, 20.4, 1.1),
(71, 'MOG046344', 'HB 5R0(AP2)M/NUSTDD', 'A-170840-02-DPMS', '13324-5R0-0032-DPMS', '5R0(AP2)M/NUSTDD', 'STD D', 20000, 20.4, 1.1),
(72, 'MOG046386', 'HB TR C/R STD 2', 'A-155995-00', '121114KH2A', 'TR C/R STD 2', 'STD 2', 20000, 15.9, 1.1),
(73, 'MOG046425', 'HB XH C/R U STD 3', 'A-155993', '12110-5RB3A', 'XH C/R U STD 3', 'STD 3', 20000, 11.5, 1.1),
(74, 'MOG000609', 'HB I4 B/M STD', 'A-290909-2', '8E5G6K347AA', 'I4 B/M STD', 'STD', 29000, 9.64, 1.1),
(75, 'MOG046449', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(76, 'MOG046424', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(77, 'MOG046423', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(78, 'MOG046403', 'HB PENTASTARC/RSTD2', 'A-215253', '04893952AA', 'PENTASTARC/RSTD2', 'STD 2', 20000, 19.3, 1.1),
(79, 'MOG046493', 'HB PENTASTARC/RSTD1', 'A-215253', '04893951AA', 'PENTASTARC/RSTD1', 'STD 1', 20000, 19.3, 1.1),
(80, 'MOG046477', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(81, 'MOG046478', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(82, 'MOG046318', 'HB XH M/N U STD 2', 'A-155991', '12215-5RB2A', 'XH M/N U STD 2', 'STD 2', 20000, 20.1, 1.1),
(83, 'MOG046483', 'HB I4 UP M/N U STD2', 'A-292793', 'K2GE-6333-BA', 'I4 UP M/N U STD2', 'STD 2', 20000, 33, 1.1),
(84, 'MOG046526', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(85, 'MOG046762', 'HB CSS 675T B/C STD', 'A-223062-03', '12665019', 'CSS 675T B/C STD', 'STD', 20000, 9.4, 1.1),
(86, 'MOG046727', 'HB AP4 C/R STD E', 'K-171720-05', '13215-5A2-A011-M1', 'AP4 C/R STD E', 'STD E', 20000, 16.4, 1.1),
(87, 'MOG046780', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(88, 'MOG046773', 'HB AP4 C/R STD E', 'K-171720-05', '13215-5A2-A011-M1', 'AP4 C/R STD E', 'STD E', 20000, 16.4, 1.1),
(89, 'MOG046855', 'HB HELLCAT C/R', 'A-215252-1', '53010881AA', 'HELLCAT C/R', 'STD', 20000, 21.7, 1.1),
(90, 'MOG046953', 'HB RNA (NP4)', 'A-170507-1', '13214RNAA010', 'RNA (NP4)', 'STD D', 20000, 12.83, 1.1),
(91, 'MOG046998', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(92, 'MOG046776', 'HB PEDD M/N U STD 4', 'A-175898', 'PEDD11354', 'PEDD M/N U STD 4', 'STD 4', 20000, 28.5, 1.1),
(93, 'MOG046894', 'HB DRAGON MU#2 STD1', 'A-292004-4', 'GN1G-6333-AAA', 'DRAGON MU#2 STD1', 'STD 1', 20000, 34.6, 1.1),
(94, 'MOG046958', 'HB I4 UP M/N L STD2', 'A-292794', 'K2GE-6A338-BA', 'I4 UP M/N L STD2', 'STD 2', 20000, 33, 1.1),
(95, 'MOG046698', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(96, 'MOG046912', 'HB PEDD M/N L STD 5', 'A-175899', 'PEDD11855', 'PEDD M/N L STD 5', 'STD 5', 10000, 28.5, 1.1),
(97, 'MOG046965', 'HB DRAGON MU#4 STD1', 'A-292003-3', 'GN1G-6333-BAA', 'DRAGON MU#4 STD1', 'STD 1', 20000, 34.6, 1.1),
(98, 'MOG047095', 'HB XH C/R L STD 4', 'A-155994', '12111-5RB4A', 'XH C/R L STD 4', 'STD 4', 20000, 11.5, 1.1),
(99, 'MOG047141', 'HB PENTASTARC/RSTD2', 'A-215253', '04893952AA', 'PENTASTARC/RSTD2', 'STD 2', 20000, 19.3, 1.1),
(100, 'MOG047182', 'HB PENTASTARC/RSTD2', 'A-215253', '04893952AA', 'PENTASTARC/RSTD2', 'STD 2', 20000, 19.3, 1.1),
(101, 'MOG047121', 'HB NP0 C/R STD D', 'A-171698-01', '13214-R70-D011-M1', 'NP0 C/R STD D', 'STD D', 20000, 17.8, 1.1),
(102, 'MOG047194', 'HB XH C/R U STD 3', 'A-155993', '12110-5RB3A', 'XH C/R U STD 3', 'STD 3', 20000, 11.5, 1.1),
(103, 'MOG047074', 'HB RNA (NP4)', 'A-170507-1', '13212RNAA010', 'RNA (NP4)', 'STD B', 20000, 12.83, 1.1),
(104, 'MOG047181', 'HB DRAGON M/NL STD1', 'A-292005-3', 'GN1G-6A338-AAA', 'DRAGON M/NL STD1', 'STD 1', 20000, 34.6, 1.1),
(105, 'MOG047960', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(106, 'MOG047910', 'HB TR M/N U', 'A-155947', '122154KH4A', 'TR M/N U', 'STD 4', 20000, 28.5, 1.1),
(107, 'MOG048158', 'HB PENTASTARC/RSTD2', 'A-215253', '04893952AA', 'PENTASTARC/RSTD2', 'STD 2', 20000, 19.3, 1.1),
(108, 'MOG049163', 'HB TR M/N L', 'A-155949-1', '122234KH4A', 'TR M/N L', 'STD 4', 20000, 28.5, 1.1),
(109, 'MOG051089', 'HB I4UPDMK MNU STD1', 'A-292793-00-DMK', 'K2GE-6333-AA-DMK', 'I4UPDMK MNU STD1', 'STD 1', 20000, 32.976, 1.1),
(110, 'MOG051273', 'HB AP4 M/N U STD D', 'A-170807-03', '13324-RPY-G021-M1', 'AP4 M/N U STD D', 'STD D', 10000, 28.2, 1.1),
(111, 'MOG051162', 'HB NP0 C/R STD D', 'A-171698-01', '13214-R70-D011-M1', 'NP0 C/R STD D', 'STD D', 20000, 17.8, 1.1),
(112, 'MOG051244', 'HB AP4 M/N U STD C', 'A-170807-03', '13323-RPY-G021-M1', 'AP4 M/N U STD C', 'STD C', 20000, 28.2, 1.1),
(113, 'MOG051286', 'HB NP0 C/R STD C', 'A-171698-01', '13213-R70-D011-M1', 'NP0 C/R STD C', 'STD C', 20000, 17.8, 1.1),
(114, 'MOG051422', 'HB GME-T6H0M/NUSTD5', 'A-215314-03', '68358337AA', 'GME-T6H0M/NUSTD5', 'STD 5', 3000, 49.1, 1.1),
(115, 'MOG051573', 'HB 5R0(AP2)M/NUSTDD', 'A-170840-02', '13324-5R0-0032', '5R0(AP2)M/NUSTDD', 'STD D', 3000, 20.4, 1.1),
(116, 'MOG051542', 'HB I4 C/R STD 2', 'A-291339-01', '8E5G6211BA', 'I4 C/R STD 2', 'STD 2', 20000, 16.4, 1.1),
(117, 'MOG051609', 'HB GME-T6H0M/NLSTD3', 'A-215315-03', '68358324AA', 'GME-T6H0M/NLSTD3', 'STD 3', 6000, 49.1, 1.1),
(118, 'MOG051577', 'HB ZV9 M/N U STD 4', 'A-156101-00', '12215 9BT4A', 'ZV9 M/N U STD 4', 'STD 4', 7000, 46, 1.1),
(119, 'MOG051547', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(120, 'MOG052297', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(121, 'MOG052213', 'HB XH M/N U STD 3', 'A-155991', '12215-5RB3A', 'XH M/N U STD 3', 'STD 3', 20000, 20.1, 1.1),
(122, 'MOG051984', 'HB XH C/R L STD 3', 'A-155994', '12111-5RB3A', 'XH C/R L STD 3', 'STD 3', 20000, 11.5, 1.1),
(123, 'MOG052246', 'HB NP0 M/N L STD D', 'K-171787-02', '13344-R9P-A011-M1', 'NP0 M/N L STD D', 'STD D', 10000, 47.2, 1.1),
(124, 'MOG052211', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(125, 'MOG052306', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(126, 'MOG052170', 'HB ISB6.7LC/RU STD', 'K-216194-R01', '5663852', 'ISB6.7LC/RU STD', 'STD', 10000, 60.927, 1.1),
(127, 'MOG052330', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(128, 'MOG052638', 'HB GMET4H0 MNL STD2', 'A-215412-00', '68466348AB', 'GMET4H0 MNL STD2', 'STD 2', 600, 35.873, 1.1),
(129, 'MOG052681', 'HB COYOTE C/R L STD', 'A-292752-1', 'JR3E-6211-DA', 'COYOTE C/R L STD', 'STD', 20000, 27, 1.1),
(130, 'MOG052482', 'HB 130Y/133Y M/N L5', 'A-146280-1', '11721-F2010-05', '130Y/133Y M/N L5', 'STD 5', 20000, 23.1, 1.1),
(131, 'MOG052163', 'HB AP2 TC C/R STD C', 'K-171922-2', '13213-59B-0031', 'AP2 TC C/R STD C', 'STD C', 20000, 10.75, 1.1),
(132, 'MOG052659', 'HB I4 C/R STD 2', 'A-291339-01', '8E5G6211BA', 'I4 C/R STD 2', 'STD 2', 20000, 16.4, 1.1),
(133, 'MOG052738', 'HB XH C/R U STD 3', 'A-155993', '12110-5RB3A', 'XH C/R U STD 3', 'STD 3', 20000, 11.5, 1.1),
(134, 'MOG052793', 'HB PENTASTARCR STD3', 'A-215253', '04893953AA', 'PENTASTARCR STD3', 'STD 3', 20000, 19.261, 1.1),
(135, 'MOG052801', 'HB COYOTE C/R U STD', 'A-292751-1', 'JR3E-6211-CA', 'COYOTE C/R U STD', 'STD', 20000, 27, 1.1),
(136, 'MOG052450', 'HB AP2 TC C/R STD E', 'K-171922-2', '13215-59B-0031', 'AP2 TC C/R STD E', 'STD E', 20000, 10.75, 1.1),
(137, 'MOG052818', 'HB TR M/N U', 'A-155947', '122154KH4A', 'TR M/N U', 'STD 4', 20000, 28.5, 1.1),
(138, 'MOG052757', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(139, 'MOG052848', 'HB TR C/R STD 2', 'A-155995-00', '121114KH2A', 'TR C/R STD 2', 'STD 2', 20000, 15.9, 1.1),
(140, 'MOG052821', 'HB NP0 C/R STD D', 'A-171698-01', '13214-R70-D011-M1', 'NP0 C/R STD D', 'STD D', 20000, 17.8, 1.1),
(141, 'MOG052174', 'HB NP0 M/N U STD D', 'K-171860-01', '13324-5J6-A011-M1', 'NP0 M/N U STD D', 'STD D', 4215, 47.2, 1.1),
(142, 'MOG052997', 'HB DRAGON M/NL STD2', 'A-292005-3', 'GN1G-6A338-ABA', 'DRAGON M/NL STD2', 'STD 2', 20000, 34.6, 1.1),
(143, 'MOG052806', 'HB 6TA M/N U STD3', 'A-156096-01', '12215 6TA3A', '6TA M/N U STD3', 'STD 3', 20000, 30.9, 1.1),
(144, 'MOG053049', 'HB I4 C/R STD 2', 'A-291339-01', '8E5G6211BA', 'I4 C/R STD 2', 'STD 2', 20000, 16.4, 1.1),
(145, 'MOG052277', 'BG AP4 M/N L STD D', 'A-170808-03', '13344-RPY-G021-M1', 'AP4 M/N L STD D', 'STD D', 20000, 28.2, 1.1),
(146, 'MOG052319', 'BG NP0 C/R STD D', 'A-171698-01', '13214-R70-D011-M1', 'NP0 C/R STD D', 'STD D', 20000, 17.8, 1.1),
(147, 'MOG053044', 'HB CSS 675T B/C STD', 'A-223062-03', '12665019', 'CSS 675T B/C STD', 'STD', 20000, 9.4, 1.1),
(148, 'MOG053187', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(149, 'MOG052601', 'HB 5R0(AP2)C/RSTDD', 'A-170930-00', '13214-5R0-0230', '5R0(AP2)C/RSTDD', 'STD D', 22000, 10.3, 1.1),
(150, 'MOG053162', 'HB PENTASTARCR STD3', 'A-215253', '04893953AA', 'PENTASTARCR STD3', 'STD 3', 20000, 19.261, 1.1),
(151, 'MOG053076', 'HB ISB6.7LC/RU STD', 'K-216194-R01', '5663852', 'ISB6.7LC/RU STD', 'STD', 10000, 60.927, 1.1),
(152, 'MOG052984', 'HB GTDI M/N L', 'A-291365-1', '9E5G6A338DA', 'GTDI M/N L', 'STD 4', 20000, 32.9, 1.1),
(153, 'MOG053159', 'HB PENTASTARCR STD1', 'A-215253', '04893951AA', 'PENTASTARCR STD1', 'STD 1', 20000, 19.261, 1.1),
(154, 'MOG053222', 'HB 5R0(AP2)C/R STDC', 'A-170925-00', '13213-5R0-0130', '5R0(AP2)C/R STDC', 'STD C', 14000, 10.4, 1.1),
(155, 'MOG053290', 'HB ZH2 M/N U STD 4', 'A-156040', '12215 9FT4A', 'ZH2 M/N U STD 4', 'STD 4', 6000, 42.2, 1.1),
(156, 'MOG053094', 'HB GTDI M/N L', 'A-291365-1', '9E5G6A338DA', 'GTDI M/N L', 'STD 4', 20000, 32.9, 1.1),
(157, 'MOG052604', 'HB 5R0(AP2)C/RSTDG', 'A-170930-00', '13217-5R0-0230', '5R0(AP2)C/RSTDG', 'STD G', 3000, 10.3, 1.1),
(158, 'MOG053093', 'HB GTDI M/N L', 'A-291365-1', '9E5G6A338DA', 'GTDI M/N L', 'STD 4', 20000, 32.9, 1.1),
(159, 'MOG052457', 'HB I4 UP M/N L STD1', 'A-292794', 'K2GE-6A338-AA', 'I4 UP M/N L STD1', 'STD 1', 20000, 33, 1.1),
(160, 'MOG053209', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(161, 'MOG053289', 'HB ZH2 M/N U STD 5', 'A-156040', '12215 9FT5A', 'ZH2 M/N U STD 5', 'STD 5', 2000, 42.2, 1.1),
(162, 'MOG053224', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(163, 'MOG053432', 'HB AP2 TC C/R STD E', 'K-171922-2', '13215-59B-0031', 'AP2 TC C/R STD E', 'STD E', 20000, 10.75, 1.1),
(164, 'MOG053318', 'HB ZV5K4 C/R STD 1', 'A-156007', '12111 6KA1A', 'ZV5K4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(165, 'MOG053433', 'HB AP2 TC C/R STD E', 'K-171922-2', '13215-59B-0031', 'AP2 TC C/R STD E', 'STD E', 20000, 10.75, 1.1),
(166, 'MOG053235', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(167, 'MOG052729', 'HB NP0 C/R STD C', 'A-171698-01', '13213-R70-D011-M1', 'NP0 C/R STD C', 'STD C', 20000, 17.8, 1.1),
(168, 'MOG052605', 'HB 5R0(AP2)C/R STDC', 'A-170925-00', '13213-5R0-0130', '5R0(AP2)C/R STDC', 'STD C', 14000, 10.4, 1.1),
(169, 'MOG053843', 'HB AP2 TC C/R STD D', 'K-171922-2', '13214-59B-0031', 'AP2 TC C/R STD D', 'STD D', 20000, 10.75, 1.1),
(170, 'MOG053988', 'HB 130Y/133Y M/NU3', 'A-146279-1', '11711-F2010-03', '130Y/133Y M/NU3', 'STD 3', 15000, 23.1, 1.1),
(171, 'MOG053873', 'HB I4 UP M/N L STD2', 'A-292794', 'K2GE-6A338-BA', 'I4 UP M/N L STD2', 'STD 2', 20000, 33, 1.1),
(172, 'MOG053546', 'HB NP0 M/N U STD B', 'K-171860-01', '13322-5J6-A011-M1', 'NP0 M/N U STD B', 'STD B', 20000, 47.2, 1.1),
(173, 'MOG053811', 'HB ZV9 C/R STD 0', 'A-156103-00', '12111 9BT0A', 'ZV9 C/R STD 0', 'STD 0', 20000, 17, 1.1),
(174, 'MOG054042', 'HB GME-T6H0M/NLSTD1', 'A-215315-03', '68358322AA', 'GME-T6H0M/NLSTD1', 'STD 1', 3000, 49.1, 1.1),
(175, 'MOG053286', 'HB ISB6.7LC/RU STD', 'K-216194-R01', '5663852', 'ISB6.7LC/RU STD', 'STD', 10000, 60.927, 1.1),
(176, 'MOG053786', 'HB PENTASTARCR STD2', 'A-215253', '04893952AA', 'PENTASTARCR STD2', 'STD 2', 20000, 19.261, 1.1),
(177, 'MOG053585', 'BG NP0 M/N U STD D', 'K-171860-01', '13324-5J6-A011-M1', 'NP0 M/N U STD D', 'STD D', 20000, 47.2, 1.1),
(178, 'MOG053620', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', '122Y B/M STD 2', 'STD 2', 20000, 10.8, 1.1),
(179, 'MOG053677', 'HB I4GTDI C/RU STD2', 'A-292795-3', 'K2GE-6211-BB', 'I4GTDI C/RU STD2', 'STD 2', 20000, 16.4, 1.1),
(180, 'MOG053361', 'HB 130Y/133Y M/NU5', 'A-146279-1', '11711-F2010-05', '130Y/133Y M/NU5', 'STD 5', 10000, 23.1, 1.1),
(181, 'MOG054432', 'HB ZV9 M/N U STD 3', 'A-156101-00', '12215 9BT3A', 'ZV9 M/N U STD 3', 'STD 3', 12000, 46, 1.1),
(182, 'MOG017560', 'HB ZV5K4 MNU  STD 4', 'A-156005', '12215 6KA4A', 'ZV5K4 MNU  STD 4', 'STD 4', 20000, 30.9, 1.1),
(183, 'MOG002812', 'HB I4 M/N U', 'A-291305-1', '6M8G6333DB', 'I4 M/N U', 'STD 4', 20000, 33.2, 1.1),
(184, 'MOG005849', 'HB PENTASTARCR STD3', 'A-215253', '04893953AA', 'PENTASTARCR STD3', 'STD 3', 20000, 19.261, 1.1),
(185, 'MOG018128', 'HB ZH2 M/N L STD 5', 'A-156041', '12223 9FT5A', 'ZH2 M/N L STD 5', 'STD 5', 20000, 42.2, 1.1),
(186, 'MOG017017', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(187, 'MOG029739', 'HB AP4 M/N L STD D', 'A-170808-02', '13344-RPY-G020', 'AP4 M/N L STD D', 'STD D', 20000, 28.2, 1.1),
(188, 'MOG014121', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 30000, 21.7, 1.1),
(189, 'MOG036890', 'BG AP2 TC C/R STD F', 'K-171922-2', '13216-59B-0031', 'AP2 TC C/R STD F', 'STD F', 20000, 10.75, 1.1),
(190, 'MOG037275', 'HB ZV5K4 C/R STD 1', 'A-156007', '12111 6KA1A', 'ZV5K4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(191, 'MOG018247', 'HB I4GTDI C/RL STD2', 'A-292796-1', 'EJ7E-6211-HB', 'I4GTDI C/RL STD2', 'STD 2', 1584, 16.4, 1.1),
(192, 'MOG013020', 'HB 5J6 (NP0 M/N U)', 'K-171860', '133225J6A010M1', '5J6 (NP0 M/N U)', 'STD B', 20000, 47.09, 1.1),
(193, 'MOG012868', 'HB I4 M/N L', 'A-291306-1', '6M8G6A338DB', 'I4 M/N L', 'STD 4', 20000, 33.2, 1.1),
(194, 'MOG002919', 'HB AP4 C/R STD D', 'K-171720-4', '13214-5A2-A010-M1', 'AP4 C/R STD D', 'STD D', 20000, 16.4, 1.1),
(195, 'MOG002918', 'HB AP4 C/R STD D', 'K-171720-4', '13214-5A2-A010-M1', 'AP4 C/R STD D', 'STD D', 20000, 16.4, 1.1),
(196, 'MOG012078', 'HB ZV5K4 C/R STD 1', 'A-156007', '12111 6KA1A', 'ZV5K4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(197, 'MOG014006', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(198, 'MOG001666', 'HB I4 M/N U', 'A-291305-1', '6M8G6333EB', 'I4 M/N U', 'STD 5', 20000, 33.2, 1.1),
(199, 'MOG019618', 'HB HEMI 5.7L C/R', 'A-215278', '53021545AJ', 'HEMI 5.7L C/R', 'STD', 20000, 21.7, 1.1),
(200, 'MOG019567', 'HB NP0 C/R STD E', 'A-171698-00', '13215R70D010M1', 'NP0 C/R STD E', 'STD E', 20000, 17.8, 1.1),
(201, 'MOG020028', 'HB PENTASTARCR STD3', 'A-215253', '04893953AA', 'PENTASTARCR STD3', 'STD 3', 20000, 19.261, 1.1),
(202, 'MOG015371', 'HB ZV5K4 C/R STD 0', 'A-156007', '12111 6KA0A', 'ZV5K4 C/R STD 0', 'STD 0', 20000, 16.4, 1.1),
(203, 'MOG017274', 'HB ZH2 M/N L STD 6', 'A-156041', '12223 9FT6A', 'ZH2 M/N L STD 6', 'STD 6', 10000, 42.2, 1.1),
(204, 'MOG012736', 'HB GTDI C/R', 'A-291637-2', 'EJ7E6211BA', 'GTDI C/R', 'STD 2', 20000, 16.5, 1.1),
(205, 'MOG013806', 'HB NP0 C/R STD C', 'A-171698-00', '13213R70D010M1', 'NP0 C/R STD C', 'STD C', 20000, 17.8, 1.1),
(206, 'MOG027109', 'HB ZV5K4 C/R STD 1', 'A-156007', '12111 6KA1A', 'ZV5K4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(207, 'MOG032648', 'HB AP4 M/N L STD D', 'A-170808-02', '13344-RPY-G020', 'AP4 M/N L STD D', 'STD D', 20000, 28.2, 1.1),
(208, 'MOG015372', 'HB ZV5K4 C/R STD 0', 'A-156007', '12111 6KA0A', 'ZV5K4 C/R STD 0', 'STD 0', 20000, 16.4, 1.1),
(209, 'MOG059078', 'BG DL2-0704 STD', 'K-922348-R04', 'HZ230685-0010', 'DL2-0704 STD', 'STD', 30000, 0.7, 1.1),
(210, 'MOG012518', 'HB I4 C/R STD 1', 'A-291339-01', '8E5G6211AA', 'I4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(211, 'MOG056355', 'HB CSS M/N L STD W', 'A-223060-4', '12663477', 'CSS M/N L STD W', 'STD W', 15000, 30.8, 1.1),
(212, 'MOG073253', 'HB ZH2 C/R STD 3', 'A-156042-00', '12111 9FT3A', 'ZH2 C/R STD 3', 'STD 3', 5000, 18.4, 1.1),
(213, 'MOG073254', 'HB ZH2 C/R STD 1', 'A-156042-00', '12111 9FT1A', 'ZH2 C/R STD 1', 'STD 1', 10000, 18.4, 1.1),
(216, 'MOG083559', 'HB 122Y B/M STD 2', 'A-146209-1', '11911-F0010-02-TP', '122Y B/M STD 2', 'STD 2', 30000, 10.8, 1.1),
(218, 'MOG083558', 'HB HB NP0 C/R STD D', 'A-171698-01', '13214-R70-D011-M1', 'HB NP0 C/R STD D', 'STD D', 10000, 17.47, 1.1),
(219, 'MOG083551', 'HB 123Y C/R STD 4', 'A-146362', '13281-70031-4', '123Y C/R STD 4', 'STD 4', 1000, 17.17, 1.1),
(220, 'MOG083549', 'HB I4 C/R STD 2', 'A-291339-01', '8E5G6211BA', 'I4 C/R STD 2', 'STD 2', 20000, 16.4, 1.1),
(221, 'MOG083548', 'HB I4 C/R STD 1', 'A-291339-01', '8E5G6211AA', 'I4 C/R STD 1', 'STD 1', 20000, 16.4, 1.1),
(222, 'MOG083518', 'HB 5R0(AP2)C/RSTDC', 'A-170930-00', '13213-5R0-0230', '5R0(AP2)C/RSTDC', 'STD C', 20000, 10.3, 1.1),
(223, 'MOG083561', 'HB AP4 C/R STD F', 'K-171720-05', '13216-5A2-A011-M1', 'AP4 C/R STD F', 'STD F', 20000, 16.4, 1.1),
(224, 'MOG083550', 'HB 123Y C/R STD 3', 'A-146362', '13281-70031-3', '123Y C/R STD 3', 'STD 3', 1000, 17.17, 1.1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ordenes_abiertas_asupervisor`
--

CREATE TABLE `ordenes_abiertas_asupervisor` (
  `id_ordenAbierta` int(11) NOT NULL,
  `empleado_revision` int(10) UNSIGNED NOT NULL,
  `empleado_aprobacion` int(10) UNSIGNED NOT NULL,
  `orden_abierta` varchar(20) NOT NULL,
  `mog_id_mog` int(11) NOT NULL,
  `fecha_apertura` date NOT NULL,
  `hora_apertura` varchar(20) NOT NULL,
  `activo` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ordenes_slitter`
--

CREATE TABLE `ordenes_slitter` (
  `id_ordenSlitter` int(11) NOT NULL,
  `orden_Slitter` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `no_Parte_Material` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ancho` float NOT NULL,
  `ancho2` float NOT NULL,
  `no_Tiras` int(11) NOT NULL,
  `numero_Lote` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `activo_op` tinyint(1) NOT NULL,
  `final` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `orden_coil`
--

CREATE TABLE `orden_coil` (
  `id_orden_coil` int(11) NOT NULL,
  `orden_coil` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `no_lote` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `material` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `ancho` float NOT NULL,
  `activo_operador` tinyint(1) NOT NULL,
  `activo_supervisor` tinyint(1) NOT NULL,
  `final` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `orden_coil`
--

INSERT INTO `orden_coil` (`id_orden_coil`, `orden_coil`, `no_lote`, `material`, `ancho`, `activo_operador`, `activo_supervisor`, `final`) VALUES
(2, 'COI018709', 'J2222801-31', 'Y7007', 18, 1, 0, 0),
(3, 'COI018710', 'E9128301-31', 'J2619', 8.5, 1, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `piezas_procesadas`
--

CREATE TABLE `piezas_procesadas` (
  `idpiezas_procesadas` int(10) UNSIGNED NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `linea` varchar(10) NOT NULL,
  `rango_canasta_1` int(10) UNSIGNED DEFAULT NULL,
  `rango_canasta_2` int(10) UNSIGNED DEFAULT NULL,
  `cantidad_piezas_procesadas` double DEFAULT NULL,
  `sobrante_inicial` int(10) UNSIGNED DEFAULT NULL,
  `piezasxfila` int(11) NOT NULL,
  `filas` int(11) NOT NULL,
  `niveles` int(11) NOT NULL,
  `canastas` int(11) NOT NULL,
  `niveles_completos` int(11) NOT NULL,
  `filas_completas` int(11) NOT NULL,
  `cambioMOG` int(11) NOT NULL,
  `sobrante` int(11) NOT NULL,
  `cantPzaGood` int(11) NOT NULL,
  `sobra_fin` int(11) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT NULL,
  `dasiddas` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `piezas_procesadas`
--

INSERT INTO `piezas_procesadas` (`idpiezas_procesadas`, `registro_rbp_id_registro_rbp`, `linea`, `rango_canasta_1`, `rango_canasta_2`, `cantidad_piezas_procesadas`, `sobrante_inicial`, `piezasxfila`, `filas`, `niveles`, `canastas`, `niveles_completos`, `filas_completas`, `cambioMOG`, `sobrante`, `cantPzaGood`, `sobra_fin`, `activo`, `dasiddas`) VALUES
(1, 31, 'TP08', 1, 29, 6555, 0, 45, 5, 1, 29, 0, 0, 0, 0, 6525, 0, 1, 2),
(2, 31, 'TP08', 30, 39, 2025, 0, 45, 5, 1, 9, 0, 0, 0, 0, 2025, 0, 1, 2),
(3, 31, 'TP08', 40, 91, 11505, 0, 45, 5, 1, 51, 0, 0, 0, 0, 11475, 0, 1, 2),
(4, 32, 'TP08', 1, 19, 4283, 0, 45, 5, 1, 19, 0, 0, 0, 0, 4275, 0, 1, 4),
(5, 32, 'TP08', 20, 90, 15768, 0, 45, 5, 1, 70, 0, 0, 0, 0, 15750, 0, 1, 4),
(6, 33, 'TP08', 1, 50, 11262, 0, 45, 5, 1, 50, 0, 0, 0, 0, 11250, 0, 1, 7),
(7, 34, 'TP08', 1, 109, 19136, 0, 35, 5, 1, 108, 0, 4, 0, 0, 19040, 140, 1, 8),
(8, 33, 'TP08', 51, 89, 8793, 0, 45, 5, 1, 39, 0, 0, 0, 0, 8775, 0, 1, 10),
(9, 35, 'TP01', 1, 22, 5046, 0, 45, 5, 1, 22, 0, 0, 0, 0, 4950, 0, 1, 11),
(10, 37, 'TP03', 1, 25, 4764, 0, 38, 5, 1, 25, 0, 0, 0, 0, 4750, 0, 1, 13),
(11, 36, 'TP03', 1, 26, 4938, 0, 38, 5, 1, 25, 0, 4, 0, 16, 4918, 168, 1, 15),
(12, 37, 'TP03', 26, 105, 15224, 179, 38, 5, 1, 79, 0, 4, 0, 27, 15189, 179, 1, 13),
(13, 36, 'TP03', 26, 79, 10282, 16, 38, 5, 1, 53, 0, 4, 0, 25, 10231, 177, 1, 15),
(14, 36, 'TP03', 79, 104, 4975, 25, 38, 5, 1, 26, 0, 0, 0, 0, 4915, 0, 1, 15),
(15, 37, 'TP03', 106, 105, 0, 0, 38, 5, 1, 0, 0, 0, 0, 0, 0, 0, 1, 17),
(16, 39, 'TP01', 1, 27, 5693, 0, 42, 5, 1, 27, 0, 0, 0, 0, 5670, 0, 1, 21),
(17, 40, 'TP01', 1, 97, 20313, 126, 42, 5, 1, 96, 0, 3, 0, 0, 20286, 126, 1, 22),
(18, 41, 'TP01', 1, 96, 20183, 0, 42, 5, 1, 96, 0, 0, 0, 0, 20160, 0, 1, 24),
(19, 39, 'TP01', 28, 57, 6346, 0, 42, 5, 1, 30, 0, 0, 0, 0, 6300, 0, 1, 21),
(20, 39, 'TP01', 58, 96, 8222, 0, 42, 5, 1, 39, 0, 0, 0, 0, 8190, 0, 1, 21),
(21, 42, 'TP03', 1, 10, 2100, 0, 42, 5, 1, 10, 0, 0, 0, 0, 2100, 0, 1, 26),
(22, 44, 'TP05', 1, 56, 15421, 0, 55, 5, 1, 56, 0, 0, 0, 0, 15400, 0, 1, 28),
(23, 43, 'TP05', 1, 81, 20362, 0, 50, 5, 1, 81, 0, 0, 0, 0, 20250, 0, 1, 30),
(24, 44, 'TP05', 57, 73, 4698, 0, 55, 5, 1, 17, 0, 0, 0, 0, 4675, 0, 1, 28),
(25, 46, 'TP01', 1, 103, 21699, 0, 42, 5, 1, 103, 0, 0, 0, 0, 21630, 0, 1, 32),
(26, 45, 'TP01', 1, 60, 12575, 162, 42, 5, 1, 59, 0, 3, 0, 36, 12552, 162, 1, 34),
(27, 45, 'TP01', 61, 97, 7827, 0, 42, 5, 1, 37, 0, 0, 0, 0, 7770, 0, 1, 34),
(28, 47, 'TP05', 1, 54, 15056, 0, 55, 5, 1, 54, 0, 0, 0, 0, 14850, 0, 1, 36),
(29, 47, 'TP05', 55, 73, 5303, 0, 55, 5, 1, 19, 0, 0, 0, 0, 5225, 0, 1, 36),
(30, 48, 'TP05', 1, 73, 20115, 0, 55, 5, 1, 73, 0, 0, 0, 0, 20075, 0, 1, 38),
(31, 3, 'TP01', 1, 3, 660, 0, 55, 5, 1, 2, 0, 2, 0, 0, 660, 110, 1, 40),
(32, 49, 'TP03', 1, 2, 550, 0, 55, 5, 1, 2, 0, 0, 0, 0, 550, 0, 1, 42),
(33, 50, 'TP04', 1, 21, 3682, 0, 35, 5, 1, 21, 0, 0, 0, 0, 3675, 0, 1, 44),
(34, 51, 'TP05', 1, 55, 15207, 0, 55, 5, 1, 55, 0, 0, 0, 0, 15125, 0, 1, 46),
(35, 51, 'TP05', 56, 73, 4994, 0, 55, 5, 1, 18, 0, 0, 0, 0, 4950, 0, 1, 46),
(36, 52, 'TP05', 1, 44, 12176, 0, 55, 5, 1, 44, 0, 0, 0, 0, 12100, 0, 1, 46),
(37, 52, 'TP05', 45, 73, 8013, 0, 55, 5, 1, 29, 0, 0, 0, 0, 7975, 0, 1, 46),
(38, 53, 'TP07', 1, 68, 14305, 0, 42, 5, 1, 68, 0, 0, 0, 0, 14280, 0, 1, 48),
(39, 53, 'TP07', 69, 96, 5905, 0, 42, 5, 1, 28, 0, 0, 0, 0, 5880, 0, 1, 48),
(40, 54, 'TP07', 1, 11, 2317, 0, 42, 5, 1, 11, 0, 0, 0, 0, 2310, 0, 1, 50),
(41, 54, 'TP07', 12, 24, 2733, 0, 42, 5, 1, 13, 0, 0, 0, 0, 2730, 0, 1, 50),
(42, 54, 'TP07', 25, 44, 4210, 0, 42, 5, 1, 20, 0, 0, 0, 0, 4200, 0, 1, 50),
(43, 54, 'TP07', 45, 54, 2125, 0, 42, 5, 1, 10, 0, 0, 0, 0, 2100, 0, 1, 50),
(44, 54, 'TP07', 55, 96, 8830, 0, 42, 5, 1, 42, 0, 0, 0, 0, 8820, 0, 1, 50),
(45, 56, 'TP10', 1, 22, 4481, 48, 42, 5, 1, 21, 0, 1, 0, 6, 4458, 48, 1, 52),
(46, 56, 'TP10', 23, 97, 15819, 0, 42, 5, 1, 75, 0, 0, 0, 0, 15750, 0, 1, 52),
(47, 57, 'TP10', 1, 96, 20198, 155, 42, 5, 1, 95, 0, 3, 0, 29, 20105, 155, 1, 52),
(48, 59, 'TP01', 1, 20, 5010, 0, 50, 5, 1, 20, 0, 0, 0, 0, 5000, 0, 1, 56),
(49, 58, 'TP10', 1, 31, 6556, 0, 42, 5, 1, 31, 0, 0, 0, 0, 6510, 0, 1, 55),
(50, 58, 'TP10', 32, 84, 11153, 0, 42, 5, 1, 53, 0, 0, 0, 0, 11130, 0, 1, 55),
(51, 59, 'TP01', 21, 82, 15430, 152, 50, 5, 1, 61, 0, 3, 0, 2, 15402, 152, 1, 56),
(52, 58, 'TP10', 85, 90, 1108, 0, 42, 5, 1, 5, 0, 1, 0, 11, 1103, 53, 1, 55),
(53, 60, 'TP01', 1, 79, 9501, 0, 30, 4, 1, 79, 0, 0, 0, 0, 9480, 0, 1, 58),
(54, 60, 'TP01', 80, 160, 9813, 86, 30, 4, 1, 80, 0, 2, 0, 26, 9686, 86, 1, 62),
(55, 61, 'TP41', 1, 53, 22313, 0, 60, 7, 1, 53, 0, 0, 0, 0, 22260, 0, 1, 60),
(56, 62, 'TP41', 1, 47, 19548, 202, 60, 7, 1, 46, 0, 3, 0, 22, 19522, 202, 1, 64),
(57, 63, 'TP41', 1, 49, 20617, 0, 60, 7, 1, 49, 0, 0, 0, 0, 20580, 0, 1, 66),
(58, 64, 'TP03', 1, 89, 20115, 0, 45, 5, 1, 89, 0, 0, 0, 0, 20025, 0, 1, 68),
(59, 65, 'TP03', 1, 64, 14298, 0, 45, 5, 1, 63, 0, 2, 0, 10, 14275, 100, 1, 70),
(60, 65, 'TP03', 64, 76, 2853, 10, 45, 5, 1, 12, 0, 2, 0, 40, 2820, 130, 1, 70),
(61, 66, 'TP10', 1, 81, 16255, 0, 40, 5, 1, 81, 0, 0, 0, 0, 16200, 0, 1, 72),
(62, 66, 'TP10', 82, 100, 3837, 0, 40, 5, 1, 19, 0, 0, 0, 0, 3800, 0, 1, 72),
(63, 69, 'TP02', 1, 96, 20289, 0, 42, 5, 1, 96, 0, 0, 0, 0, 20160, 0, 1, 74),
(64, 68, 'TP02', 1, 73, 15354, 0, 42, 5, 1, 73, 0, 0, 0, 0, 15330, 0, 1, 76),
(65, 68, 'TP02', 74, 96, 4844, 0, 42, 5, 1, 23, 0, 0, 0, 0, 4830, 0, 1, 76),
(66, 70, 'TP09', 1, 86, 18089, 0, 42, 5, 1, 86, 0, 0, 0, 0, 18060, 0, 1, 78),
(67, 71, 'TP09', 1, 84, 17689, 0, 42, 5, 1, 84, 0, 0, 0, 0, 17640, 0, 1, 80),
(68, 70, 'TP09', 87, 96, 2102, 0, 42, 5, 1, 10, 0, 0, 0, 0, 2100, 0, 1, 78),
(69, 71, 'TP09', 85, 107, 4778, 152, 42, 5, 1, 22, 0, 3, 0, 26, 4772, 152, 1, 80),
(70, 72, 'TP04', 1, 44, 11025, 0, 50, 5, 1, 44, 0, 0, 0, 0, 11000, 0, 1, 82),
(71, 73, 'TP04', 1, 63, 15793, 0, 50, 5, 1, 63, 0, 0, 0, 0, 15750, 0, 1, 84),
(72, 72, 'TP04', 45, 80, 9029, 0, 50, 5, 1, 36, 0, 0, 0, 0, 9000, 0, 1, 82),
(73, 73, 'TP04', 64, 80, 4276, 0, 50, 5, 1, 17, 0, 0, 0, 0, 4250, 0, 1, 84),
(76, 75, 'TP41', 1, 4, 1683, 0, 60, 7, 1, 4, 0, 0, 0, 0, 1680, 0, 1, 87),
(77, 75, 'TP41', 5, 49, 18946, 0, 60, 7, 1, 45, 0, 0, 0, 0, 18900, 0, 1, 87),
(78, 76, 'TP41', 1, 48, 20298, 0, 60, 7, 1, 48, 0, 0, 0, 0, 20160, 0, 1, 87),
(79, 77, 'TP41', 1, 38, 15987, 0, 60, 7, 1, 38, 0, 0, 0, 0, 15960, 0, 1, 89),
(80, 77, 'TP41', 39, 52, 5702, 225, 60, 7, 1, 13, 0, 3, 0, 45, 5685, 225, 1, 89),
(81, 78, 'TP09', 1, 66, 13875, 0, 42, 5, 1, 66, 0, 0, 0, 0, 13860, 0, 1, 91),
(82, 78, 'TP09', 67, 96, 6325, 0, 42, 5, 1, 30, 0, 0, 0, 0, 6300, 0, 1, 91),
(83, 79, 'TP09', 1, 80, 16851, 0, 42, 5, 1, 80, 0, 0, 0, 0, 16800, 0, 1, 93),
(84, 79, 'TP09', 81, 104, 4957, 102, 42, 5, 1, 23, 0, 2, 0, 18, 4932, 102, 1, 95),
(85, 80, 'TP05', 1, 21, 6048, 135, 55, 5, 1, 20, 0, 2, 0, 25, 5635, 135, 1, 97),
(86, 81, 'TP05', 1, 24, 6539, 198, 55, 5, 1, 23, 0, 3, 0, 33, 6523, 198, 1, 99),
(87, 80, 'TP05', 22, 74, 14567, 140, 55, 5, 1, 52, 0, 2, 0, 30, 14440, 140, 1, 97),
(88, 81, 'TP05', 25, 74, 13832, 0, 55, 5, 1, 50, 0, 0, 0, 0, 13750, 0, 1, 99),
(89, 82, 'TP02', 1, 100, 20049, 0, 40, 5, 1, 100, 0, 0, 0, 0, 20000, 0, 1, 102),
(90, 83, 'TP02', 1, 87, 15234, 79, 35, 5, 1, 86, 0, 2, 0, 9, 15129, 79, 1, 103),
(91, 83, 'TP01', 88, 115, 4930, 0, 35, 5, 1, 28, 0, 0, 0, 0, 4900, 0, 1, 103),
(92, 84, 'TP04', 1, 63, 17407, 0, 55, 5, 1, 63, 0, 0, 0, 0, 17325, 0, 1, 105),
(93, 84, 'TP04', 64, 73, 2758, 0, 55, 5, 1, 10, 0, 0, 0, 0, 2750, 0, 1, 105),
(94, 85, 'TP41', 1, 10, 4263, 0, 60, 7, 1, 10, 0, 0, 0, 0, 4200, 0, 1, 107),
(95, 85, 'TP41', 11, 49, 16411, 0, 60, 7, 1, 39, 0, 0, 0, 0, 16380, 0, 1, 107),
(96, 86, 'TP05', 1, 80, 20029, 0, 50, 5, 1, 80, 0, 0, 0, 0, 20000, 0, 1, 109),
(97, 87, 'TP10', 1, 45, 9501, 0, 42, 5, 1, 45, 0, 0, 0, 0, 9450, 0, 1, 111),
(98, 87, 'TP10', 46, 96, 10733, 0, 42, 5, 1, 51, 0, 0, 0, 0, 10710, 0, 1, 111),
(99, 88, 'TP05', 1, 25, 6274, 0, 50, 5, 1, 25, 0, 0, 0, 0, 6250, 0, 1, 113),
(100, 88, 'TP05', 26, 80, 13767, 0, 50, 5, 1, 55, 0, 0, 0, 0, 13750, 0, 1, 113),
(101, 89, 'TP09', 1, 37, 7793, 0, 42, 5, 1, 37, 0, 0, 0, 0, 7770, 0, 1, 115),
(102, 89, 'TP09', 38, 96, 12413, 0, 42, 5, 1, 59, 0, 0, 0, 0, 12390, 0, 1, 115),
(103, 90, 'TP41', 1, 37, 9160, 150, 50, 5, 1, 36, 0, 3, 0, 0, 9150, 150, 1, 117),
(104, 90, 'TP41', 38, 81, 11055, 0, 50, 5, 1, 44, 0, 0, 0, 0, 11000, 0, 1, 117),
(105, 91, 'TP41', 1, 8, 3369, 0, 60, 7, 1, 8, 0, 0, 0, 0, 3360, 0, 1, 119),
(106, 91, 'TP41', 9, 50, 17667, 0, 60, 7, 1, 42, 0, 0, 0, 0, 17640, 0, 1, 119),
(107, 92, 'TP03', 1, 31, 5599, 0, 35, 5, 1, 31, 0, 0, 0, 0, 5425, 0, 1, 121),
(108, 92, 'TP03', 32, 111, 14067, 138, 35, 5, 1, 79, 0, 3, 0, 33, 13963, 138, 1, 121),
(109, 93, 'TP02', 1, 67, 10167, 0, 30, 5, 1, 67, 0, 0, 0, 0, 10050, 0, 1, 123),
(110, 94, 'TP01', 1, 106, 18566, 146, 35, 5, 1, 105, 0, 4, 0, 6, 18521, 146, 1, 125),
(111, 94, 'TP01', 107, 116, 1619, 0, 35, 5, 1, 9, 0, 0, 0, 29, 1604, 29, 1, 125),
(112, 95, 'TP03', 1, 96, 20187, 0, 42, 5, 1, 96, 0, 0, 0, 0, 20160, 0, 1, 127),
(113, 96, 'TP01', 1, 67, 11829, 0, 35, 5, 1, 67, 0, 0, 0, 0, 11725, 0, 1, 129),
(114, 97, 'TP02', 1, 34, 5067, 0, 30, 5, 1, 33, 0, 3, 0, 22, 5062, 112, 1, 131),
(115, 97, 'TP02', 34, 64, 4694, 22, 30, 5, 1, 31, 0, 0, 0, 0, 4628, 0, 1, 131),
(116, 97, 'TP02', 65, 133, 10404, 0, 30, 5, 1, 69, 0, 0, 0, 0, 10350, 0, 1, 131),
(117, 99, 'TP11', 1, 84, 17513, 32, 42, 5, 1, 83, 0, 0, 0, 32, 17462, 32, 1, 133),
(118, 99, 'TP11', 85, 96, 2532, 0, 42, 5, 1, 12, 0, 0, 0, 0, 2520, 0, 1, 133),
(119, 100, 'TP11', 1, 41, 8635, 0, 42, 5, 1, 41, 0, 0, 0, 0, 8610, 0, 1, 135),
(120, 100, 'TP11', 42, 75, 7003, 66, 42, 5, 1, 33, 0, 1, 0, 24, 6996, 66, 1, 135),
(121, 100, 'TP11', 76, 96, 4418, 0, 42, 5, 1, 21, 0, 0, 0, 0, 4410, 0, 1, 135),
(122, 101, 'TP09', 1, 27, 6680, 175, 50, 5, 1, 26, 0, 3, 0, 25, 6675, 175, 1, 137),
(123, 101, 'TP09', 28, 40, 3137, 109, 50, 5, 1, 12, 0, 2, 0, 9, 3109, 109, 1, 137),
(124, 102, 'TP10', 1, 63, 15793, 0, 50, 5, 1, 63, 0, 0, 0, 0, 15750, 0, 1, 139),
(125, 104, 'TP02', 1, 100, 14952, 58, 30, 5, 1, 99, 0, 1, 0, 28, 14908, 58, 1, 141),
(126, 103, 'TP04', 1, 73, 18237, 159, 50, 5, 1, 72, 0, 3, 0, 9, 18159, 159, 1, 143),
(127, 103, 'TP04', 74, 82, 2265, 0, 50, 5, 1, 9, 0, 0, 0, 0, 2250, 0, 1, 143),
(128, 105, 'TP09', 1, 68, 14349, 0, 42, 5, 1, 68, 0, 0, 0, 0, 14280, 0, 1, 145),
(129, 105, 'TP09', 69, 96, 5903, 0, 42, 5, 1, 28, 0, 0, 0, 0, 5880, 0, 1, 145),
(130, 106, 'TP02', 1, 52, 9804, 79, 38, 5, 1, 51, 0, 2, 0, 3, 9769, 79, 1, 147),
(131, 107, 'TP11', 1, 38, 7837, 47, 42, 5, 1, 37, 0, 1, 0, 5, 7817, 47, 1, 149),
(132, 107, 'TP11', 39, 97, 12439, 0, 42, 5, 1, 59, 0, 0, 0, 0, 12390, 0, 1, 149),
(133, 108, 'TP01', 1, 106, 20192, 0, 38, 5, 1, 106, 0, 0, 0, 0, 20140, 0, 1, 151),
(134, 109, 'TP01', 1, 58, 10054, 0, 35, 5, 1, 57, 0, 1, 0, 29, 10039, 64, 1, 155),
(135, 109, 'TP01', 58, 87, 5262, 29, 35, 5, 1, 30, 0, 0, 0, 0, 5221, 0, 1, 155),
(136, 111, 'TP05', 1, 44, 10989, 0, 50, 5, 1, 43, 0, 4, 0, 10, 10960, 210, 1, 157),
(137, 112, 'TP09', 1, 37, 6867, 0, 38, 5, 1, 36, 0, 0, 0, 10, 6850, 10, 1, 157),
(138, 112, 'TP02', 37, 106, 13393, 10, 38, 5, 1, 70, 0, 0, 0, 0, 13290, 0, 1, 161),
(139, 111, 'TP08', 44, 80, 9306, 10, 50, 5, 1, 37, 0, 0, 0, 0, 9240, 0, 1, 161),
(140, 110, 'TP02', 1, 53, 10084, 0, 38, 5, 1, 53, 0, 0, 0, 0, 10070, 0, 1, 161),
(141, 113, 'TP08', 1, 9, 2254, 0, 50, 5, 1, 9, 0, 0, 0, 0, 2250, 0, 1, 162),
(142, 114, 'TP01', 1, 23, 3430, 0, 30, 5, 1, 22, 0, 4, 0, 0, 3420, 120, 1, 164),
(143, 115, 'TP04', 1, 10, 1860, 0, 40, 5, 1, 9, 0, 1, 0, 6, 1846, 46, 1, 166),
(144, 116, 'TP05', 1, 101, 22740, 0, 45, 5, 1, 100, 0, 4, 0, 0, 22680, 180, 1, 166),
(145, 117, 'TP02', 1, 49, 7281, 0, 30, 5, 1, 48, 0, 1, 0, 11, 7241, 41, 1, 168),
(146, 118, 'TP03', 1, 46, 5457, 0, 30, 4, 1, 45, 0, 0, 0, 14, 5414, 14, 1, 168),
(147, 118, 'TP03', 46, 59, 1693, 14, 30, 4, 1, 14, 0, 0, 0, 0, 1666, 0, 1, 168),
(148, 119, 'TP11', 1, 31, 6410, 0, 42, 5, 1, 30, 0, 2, 0, 5, 6389, 89, 1, 170),
(149, 119, 'TP11', 31, 95, 13676, 5, 42, 5, 1, 65, 0, 0, 0, 0, 13645, 0, 1, 170),
(150, 120, 'TP10', 1, 73, 20157, 0, 55, 5, 1, 73, 0, 0, 0, 0, 20075, 0, 1, 172),
(151, 121, 'TP09', 1, 63, 12478, 70, 40, 5, 1, 62, 0, 1, 0, 30, 12470, 70, 1, 172),
(152, 121, 'TP09', 64, 82, 3824, 0, 40, 5, 1, 19, 0, 0, 0, 0, 3800, 0, 1, 172),
(153, 122, 'TP01', 1, 24, 6011, 0, 50, 5, 1, 24, 0, 0, 0, 0, 6000, 0, 1, 172),
(154, 122, 'TP01', 25, 80, 14026, 0, 50, 5, 1, 56, 0, 0, 0, 0, 14000, 0, 1, 172),
(155, 123, 'TP02', 1, 51, 6215, 0, 30, 4, 1, 51, 0, 0, 0, 0, 6120, 0, 1, 172),
(156, 123, 'TP02', 52, 75, 2996, 0, 30, 4, 1, 24, 0, 0, 0, 0, 2880, 0, 1, 172),
(157, 123, 'TP02', 76, 85, 1201, 0, 30, 4, 1, 10, 0, 0, 0, 0, 1200, 0, 1, 172),
(158, 124, 'TP10', 1, 48, 13240, 0, 55, 5, 1, 48, 0, 0, 0, 0, 13200, 0, 1, 172),
(159, 124, 'TP10', 49, 73, 6895, 0, 55, 5, 1, 25, 0, 0, 0, 0, 6875, 0, 1, 172),
(160, 125, 'TP10', 1, 42, 11580, 0, 55, 5, 1, 42, 0, 0, 0, 0, 11550, 0, 1, 174),
(161, 125, 'TP10', 43, 73, 8772, 0, 55, 5, 1, 31, 0, 0, 0, 0, 8525, 0, 1, 174),
(162, 126, 'TP08', 1, 40, 6069, 71, 30, 5, 1, 39, 0, 2, 0, 11, 5921, 71, 1, 174),
(163, 126, 'TP08', 41, 68, 4258, 77, 30, 5, 1, 27, 0, 2, 0, 17, 4127, 77, 1, 174),
(164, 128, 'TH02', 1, 1, 600, 0, 26, 7, 4, 0, 3, 0, 0, 24, 570, 570, 1, 175),
(165, 129, 'TH13', 1, 6, 3411, 11, 32, 7, 3, 5, 0, 0, 0, 11, 3371, 11, 1, 176),
(166, 129, 'TH13', 6, 30, 16489, 11, 32, 7, 3, 24, 0, 6, 0, 0, 16309, 192, 1, 176),
(168, 131, 'TH02', 1, 3, 4264, 1134, 43, 9, 4, 2, 2, 8, 0, 16, 4230, 1134, 1, 177),
(169, 131, 'TH02', 3, 10, 10738, 1134, 43, 9, 4, 7, 2, 3, 0, 18, 10623, 921, 1, 177),
(170, 131, 'TH02', 10, 13, 4998, 921, 43, 9, 4, 3, 2, 2, 0, 35, 4618, 895, 1, 177),
(171, 132, 'TH12', 1, 1, 720, 0, 36, 7, 4, 0, 2, 6, 0, 0, 720, 720, 1, 178),
(172, 132, 'TH12', 2, 20, 19263, 0, 36, 7, 4, 18, 3, 4, 0, 33, 19077, 933, 1, 178),
(173, 134, 'TH11', 1, 21, 20003, 0, 35, 7, 4, 20, 1, 0, 0, 11, 19856, 256, 1, 179),
(174, 135, 'TH13', 1, 5, 2767, 0, 32, 7, 3, 4, 0, 2, 0, 11, 2763, 75, 1, 180),
(175, 135, 'TH13', 21, 37, 11494, 533, 32, 7, 3, 16, 2, 2, 0, 21, 11274, 533, 1, 180),
(177, 139, 'TH03', 1, 1, 148, 0, 40, 8, 4, 0, 0, 3, 0, 8, 128, 128, 1, 181),
(179, 139, 'TH03', 1, 8, 9385, 128, 40, 8, 4, 7, 1, 2, 0, 32, 9264, 432, 1, 182),
(180, 139, 'TH03', 8, 16, 10423, 432, 40, 8, 4, 8, 1, 2, 0, 22, 10230, 422, 1, 182),
(181, 141, 'TH10', 1, 9, 4215, 0, 22, 5, 4, 8, 1, 3, 0, 0, 3696, 176, 1, 185),
(182, 142, 'TH06', 1, 10, 6750, 0, 24, 7, 4, 10, 0, 0, 0, 0, 6720, 0, 1, 187),
(183, 142, 'TH06', 11, 30, 13377, 0, 24, 7, 4, 19, 2, 3, 0, 13, 13189, 421, 1, 188),
(184, 143, 'TH17', 1, 11, 7473, 0, 29, 8, 3, 10, 2, 0, 0, 0, 7424, 464, 1, 189),
(185, 143, 'TH17', 11, 29, 12219, 464, 29, 8, 3, 18, 0, 1, 0, 6, 12099, 35, 1, 189),
(186, 143, 'TH17', 29, 29, 363, 35, 29, 8, 3, 0, 1, 4, 0, 7, 320, 355, 1, 189),
(187, 144, 'TH12', 1, 14, 13726, 0, 36, 7, 4, 13, 2, 0, 0, 24, 13632, 528, 1, 190),
(188, 147, 'TP41', 1, 48, 20191, 0, 60, 7, 1, 48, 0, 0, 0, 0, 20160, 0, 1, 191),
(189, 148, 'TP41', 1, 88, 18507, 0, 42, 5, 1, 88, 0, 0, 0, 0, 18480, 0, 1, 192),
(190, 148, 'TP07', 89, 98, 2109, 0, 42, 5, 1, 10, 0, 0, 0, 0, 2100, 0, 1, 192),
(191, 149, 'TP11', 1, 34, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(192, 149, 'TP11', 35, 68, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(193, 149, 'TP11', 69, 102, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(194, 149, 'TP11', 103, 136, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(195, 149, 'TP11', 137, 170, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(196, 149, 'TP11', 171, 204, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(197, 149, 'TP11', 205, 238, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(198, 149, 'TP11', 239, 272, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(199, 149, 'TP11', 273, 306, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(200, 149, 'TP11', 307, 340, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(201, 149, 'TP11', 341, 374, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(202, 149, 'TP11', 375, 408, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(203, 149, 'TP11', 409, 442, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(204, 149, 'TP11', 443, 476, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(205, 149, 'TP11', 477, 510, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(206, 149, 'TP11', 511, 544, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(207, 149, 'TP11', 545, 578, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(208, 149, 'TP11', 579, 612, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(209, 149, 'TP11', 613, 646, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(210, 149, 'TP11', 647, 680, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(211, 149, 'TP11', 681, 714, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(212, 149, 'TP11', 715, 748, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(213, 149, 'TP11', 749, 782, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(214, 149, 'TP11', 783, 816, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(215, 149, 'TP11', 817, 850, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(216, 149, 'TP11', 851, 884, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(217, 149, 'TP11', 885, 918, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(218, 149, 'TP11', 919, 952, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(219, 149, 'TP11', 953, 986, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(220, 149, 'TP11', 987, 1020, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(221, 149, 'TP11', 1021, 1054, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(222, 149, 'TP11', 1055, 1088, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(223, 149, 'TP11', 1089, 1122, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(224, 149, 'TP11', 1123, 1156, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(225, 149, 'TP11', 1157, 1190, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(226, 149, 'TP11', 1191, 1224, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(227, 149, 'TP11', 1225, 1258, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(228, 149, 'TP11', 1259, 1292, 9359, 0, 55, 5, 1, 34, 0, 0, 0, 0, 9350, 0, 1, 194),
(229, 150, 'TP05', 1, 13, 2740, 0, 42, 5, 1, 13, 0, 0, 0, 0, 2730, 0, 1, 196),
(230, 152, 'TH05', 1, 7, 5087, 0, 26, 7, 4, 6, 3, 4, 0, 7, 5025, 657, 1, 197),
(231, 152, 'TH05', 7, 28, 14863, 657, 26, 7, 4, 21, 0, 1, 0, 23, 14680, 49, 1, 197),
(232, 154, 'TP11', 1, 26, 7169, 0, 55, 5, 1, 26, 0, 0, 0, 0, 7150, 0, 1, 200),
(233, 154, 'TP11', 27, 51, 6894, 0, 55, 5, 1, 25, 0, 0, 0, 0, 6875, 0, 1, 200),
(234, 155, 'TP01', 1, 50, 6019, 0, 30, 4, 1, 50, 0, 0, 0, 0, 6000, 0, 1, 201),
(235, 156, 'TP08', 1, 62, 10869, 0, 35, 5, 1, 62, 0, 0, 0, 0, 10850, 0, 1, 202),
(236, 156, 'TP08', 63, 115, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(237, 156, 'TP08', 116, 168, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(238, 156, 'TP08', 169, 221, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(239, 156, 'TP08', 222, 274, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(240, 156, 'TP08', 275, 327, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(241, 156, 'TP08', 328, 380, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(242, 156, 'TP08', 381, 433, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(243, 156, 'TP08', 434, 486, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(244, 156, 'TP08', 487, 539, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(245, 156, 'TP08', 540, 592, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(246, 156, 'TP08', 593, 645, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(247, 156, 'TP08', 646, 698, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(248, 156, 'TP08', 699, 751, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(249, 156, 'TP08', 752, 804, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(250, 156, 'TP08', 805, 857, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(251, 156, 'TP08', 858, 910, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(252, 156, 'TP08', 911, 963, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(253, 156, 'TP08', 964, 1016, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(254, 156, 'TP08', 1017, 1069, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(255, 156, 'TP08', 1070, 1122, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(256, 156, 'TP08', 1123, 1175, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(257, 156, 'TP08', 1176, 1228, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(258, 156, 'TP08', 1229, 1281, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(259, 156, 'TP08', 1282, 1334, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(260, 156, 'TP08', 1335, 1387, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(261, 156, 'TP08', 1388, 1440, 9286, 0, 35, 5, 1, 53, 0, 0, 0, 0, 9275, 0, 1, 202),
(262, 157, 'TH10', 1, 6, 7469, 0, 42, 8, 4, 5, 2, 1, 0, 15, 7449, 729, 1, 204),
(263, 158, 'TH05', 1, 6, 3746, 0, 26, 7, 4, 5, 0, 2, 0, 10, 3702, 62, 1, 205),
(264, 158, 'TH05', 6, 28, 16554, 62, 26, 7, 4, 22, 2, 6, 0, 20, 16494, 540, 1, 205),
(265, 159, 'TP08', 1, 43, 7530, 150, 35, 5, 1, 42, 0, 4, 0, 10, 7500, 150, 1, 207),
(266, 159, 'TP08', 44, 115, 12615, 0, 35, 5, 1, 72, 0, 0, 0, 0, 12600, 0, 1, 207),
(267, 160, 'TP41', 1, 35, 14718, 0, 60, 7, 1, 35, 0, 0, 0, 0, 14700, 0, 1, 208),
(268, 160, 'TP41', 36, 40, 2109, 0, 60, 7, 1, 5, 0, 0, 0, 0, 2100, 0, 1, 208),
(269, 160, 'TP41', 41, 48, 3369, 0, 60, 7, 1, 8, 0, 0, 0, 0, 3360, 0, 1, 208),
(270, 161, 'TP01', 1, 57, 6875, 0, 30, 4, 1, 57, 0, 0, 0, 0, 6840, 0, 1, 209),
(271, 162, 'TP10', 1, 27, 5670, 0, 42, 5, 1, 27, 0, 0, 0, 0, 5670, 0, 1, 210),
(272, 162, 'TP10', 28, 75, 10093, 0, 42, 5, 1, 48, 0, 0, 0, 0, 10080, 0, 1, 210),
(273, 162, 'TP10', 76, 96, 4736, 0, 45, 5, 1, 21, 0, 0, 0, 0, 4725, 0, 1, 210),
(274, 163, 'TP04', 74, 73, 20116, 0, 55, 5, 1, 73, 0, 0, 0, 0, 20075, 0, 1, 212),
(275, 165, 'TP04', 74, 73, 20095, 0, 55, 5, 1, 73, 0, 0, 0, 0, 20075, 0, 1, 213),
(276, 166, 'TP41', 1, 48, 20206, 0, 60, 7, 1, 48, 0, 0, 0, 0, 20160, 0, 1, 214),
(277, 167, 'TH07', 1, 5, 4972, 0, 37, 7, 4, 4, 2, 4, 0, 14, 4824, 680, 1, 215),
(278, 167, 'TH07', 5, 10, 4949, 680, 37, 7, 4, 5, 1, 0, 0, 23, 4782, 282, 1, 215),
(279, 167, 'TH07', 20, 29, 9938, 777, 37, 7, 4, 9, 3, 0, 0, 0, 9819, 777, 1, 215),
(280, 168, 'TH02', 1, 2, 2868, 0, 44, 9, 4, 1, 3, 0, 0, 0, 2772, 1188, 1, 216),
(281, 168, 'TH02', 2, 9, 11157, 1188, 44, 9, 4, 7, 2, 5, 0, 9, 10921, 1021, 1, 216),
(282, 169, 'TP09', 1, 47, 13090, 0, 55, 5, 1, 47, 0, 0, 0, 0, 12925, 0, 1, 218),
(283, 169, 'TP09', 48, 73, 7216, 0, 55, 5, 1, 26, 0, 0, 0, 0, 7150, 0, 1, 218),
(284, 170, 'TP08', 1, 37, 7421, 0, 40, 5, 1, 37, 0, 0, 0, 0, 7400, 0, 1, 219),
(285, 170, 'TP08', 38, 47, 2013, 0, 40, 5, 1, 10, 0, 0, 0, 0, 2000, 0, 1, 219),
(286, 170, 'TP08', 48, 76, 5808, 0, 40, 5, 1, 29, 0, 0, 0, 0, 5800, 0, 1, 219),
(287, 171, 'TP03', 1, 86, 15074, 0, 35, 5, 1, 86, 0, 0, 0, 0, 15050, 0, 1, 220),
(288, 171, 'TP03', 87, 115, 5093, 0, 35, 5, 1, 29, 0, 0, 0, 0, 5075, 0, 1, 221),
(289, 172, 'TP01', 1, 73, 8853, 0, 30, 4, 1, 73, 0, 0, 0, 0, 8760, 0, 1, 223),
(290, 172, 'TP01', 74, 94, 2526, 0, 30, 4, 1, 21, 0, 0, 0, 0, 2520, 0, 1, 223),
(291, 173, 'TP04', 1, 32, 6725, 0, 42, 5, 1, 32, 0, 0, 0, 0, 6720, 0, 1, 224),
(292, 173, 'TP04', 33, 48, 3371, 0, 42, 5, 1, 16, 0, 0, 0, 0, 3360, 0, 1, 224),
(293, 174, 'TP09', 1, 20, 3082, 0, 30, 5, 1, 20, 0, 0, 0, 0, 3000, 0, 1, 226),
(294, 180, 'TH17', 1, 4, 2642, 70, 30, 7, 4, 3, 0, 2, 0, 10, 2590, 70, 1, 230),
(295, 180, 'TH17', 25, 46, 18354, 644, 30, 7, 4, 21, 3, 0, 0, 14, 18214, 644, 1, 231),
(296, 181, 'TH17', 5, 5, 816, 0, 23, 5, 4, 1, 1, 2, 0, 17, 638, 178, 1, 234),
(297, 186, 'TH10', 1, 2, 583, 0, 20, 5, 3, 1, 2, 4, 0, 3, 583, 283, 1, 238),
(298, 187, 'TH10', 1, 6, 5954, 0, 50, 5, 4, 5, 3, 4, 0, 4, 5954, 954, 1, 239),
(299, 190, 'TG01', 1, 22, 9680, 0, 22, 5, 4, 22, 0, 0, 0, 0, 9680, 0, 1, 240),
(300, 191, 'TH10', 6, 11, 14755, 2255, 50, 10, 5, 5, 4, 5, 0, 5, 14755, 2255, 1, 241),
(301, 192, 'TH10', 21, 26, 2987, 485, 20, 5, 5, 5, 4, 4, 0, 5, 2985, 485, 1, 242),
(302, 194, 'TH10', 1, 3, 2325, 0, 40, 4, 5, 2, 4, 2, 0, 5, 2325, 725, 1, 243),
(303, 196, 'TH10', 1, 6, 2986, 0, 20, 5, 5, 5, 4, 4, 0, 6, 2986, 486, 1, 244),
(304, 197, 'TH10', 1, 6, 28040, 3040, 40, 25, 5, 5, 3, 1, 0, 0, 28040, 3040, 1, 245),
(305, 198, 'TH10', 1, 6, 8822, 1322, 60, 5, 5, 5, 4, 2, 0, 2, 8822, 1322, 1, 246),
(306, 199, 'TH10', 1, 6, 7455, 0, 50, 5, 5, 5, 4, 4, 0, 5, 7455, 1205, 1, 247),
(307, 200, 'TH10', 1, 6, 5821, 0, 10, 20, 5, 5, 4, 2, 0, 1, 5821, 821, 1, 248),
(308, 204, 'TH10', 1, 43, 98327, 0, 55, 21, 2, 42, 1, 2, 0, 42, 98327, 1307, 1, 250),
(309, 205, 'TH10', 1, 5, 3914, 0, 20, 10, 4, 4, 3, 5, 0, 14, 3914, 714, 1, 251),
(310, 206, 'TH17', 1, 22, 239381, 0, 23, 21, 23, 21, 12, 12, 0, 20, 239381, 6092, 1, 252),
(311, 207, 'TH10', 1, 11, 11012, 0, 10, 10, 10, 10, 9, 9, 0, 8, 10998, 998, 1, 253),
(312, 208, 'TH10', 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 253),
(313, 211, 'TH10', 1, 53, 7281288, 0, 52, 52, 51, 52, 40, 40, 0, 40, 7281288, 110280, 1, 254),
(314, 213, 'TP01', 1, 21, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(315, 213, 'TP01', 22, 42, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(316, 213, 'TP01', 43, 63, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(317, 213, 'TP01', 64, 84, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(318, 213, 'TP01', 85, 105, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(319, 213, 'TP01', 106, 126, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(320, 213, 'TP01', 127, 147, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(321, 213, 'TP01', 148, 168, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(322, 213, 'TP01', 169, 189, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(323, 213, 'TP01', 190, 210, 2021, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(324, 213, 'TP01', 211, 231, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(325, 213, 'TP01', 232, 252, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(326, 213, 'TP01', 253, 273, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(327, 213, 'TP01', 274, 294, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(328, 213, 'TP01', 295, 315, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(329, 213, 'TP01', 316, 336, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(330, 213, 'TP01', 337, 357, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(331, 213, 'TP01', 358, 378, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(332, 213, 'TP01', 379, 399, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(333, 213, 'TP01', 400, 420, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(334, 213, 'TP01', 421, 441, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(335, 213, 'TP01', 442, 462, 2022, 0, 20, 5, 1, 20, 0, 1, 0, 1, 2021, 21, 1, 255),
(336, 213, 'TP01', 463, 463, 2, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(337, 213, 'TP01', 464, 464, 2, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(338, 213, 'TP01', 465, 465, 2, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(339, 213, 'TP01', 466, 466, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(340, 213, 'TP01', 467, 467, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(341, 213, 'TP01', 468, 468, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(342, 213, 'TP01', 469, 469, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(343, 213, 'TP01', 470, 470, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(344, 213, 'TP01', 471, 471, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(345, 213, 'TP01', 472, 472, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(346, 213, 'TP01', 473, 473, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(347, 213, 'TP01', 474, 474, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(348, 213, 'TP01', 475, 475, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(349, 213, 'TP01', 476, 476, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(350, 213, 'TP01', 477, 477, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(351, 213, 'TP01', 478, 478, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(352, 213, 'TP01', 479, 479, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(353, 213, 'TP01', 480, 480, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(354, 213, 'TP01', 481, 481, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(355, 213, 'TP01', 482, 482, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(356, 213, 'TP01', 483, 483, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(357, 213, 'TP01', 484, 484, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(358, 213, 'TP01', 485, 485, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(359, 213, 'TP01', 486, 486, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(360, 213, 'TP01', 487, 487, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(361, 213, 'TP01', 488, 488, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(362, 213, 'TP01', 489, 489, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(363, 213, 'TP01', 490, 490, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(364, 213, 'TP01', 491, 491, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(365, 213, 'TP01', 492, 492, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(366, 213, 'TP01', 493, 493, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(367, 213, 'TP01', 494, 494, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(368, 213, 'TP01', 495, 495, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(369, 213, 'TP01', 496, 496, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(370, 213, 'TP01', 497, 497, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(371, 213, 'TP01', 498, 498, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(372, 213, 'TP01', 499, 499, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(373, 213, 'TP01', 500, 500, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(374, 213, 'TP01', 501, 501, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(375, 213, 'TP01', 502, 502, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(376, 213, 'TP01', 503, 503, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(377, 213, 'TP01', 504, 504, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(378, 213, 'TP01', 505, 505, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(379, 213, 'TP01', 506, 506, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(380, 213, 'TP01', 507, 507, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(381, 213, 'TP01', 508, 508, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(382, 213, 'TP01', 509, 509, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(383, 213, 'TP01', 510, 510, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(384, 213, 'TP01', 511, 511, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(385, 213, 'TP01', 512, 512, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(386, 213, 'TP01', 513, 513, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(387, 213, 'TP01', 514, 514, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(388, 213, 'TP01', 515, 515, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(389, 213, 'TP01', 516, 516, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(390, 213, 'TP01', 517, 517, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(391, 213, 'TP01', 518, 518, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(392, 213, 'TP01', 519, 519, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(393, 213, 'TP01', 520, 520, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(394, 213, 'TP01', 521, 521, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(395, 213, 'TP01', 522, 522, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(396, 213, 'TP01', 523, 523, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(397, 213, 'TP01', 524, 524, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(398, 213, 'TP01', 525, 525, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(399, 213, 'TP01', 526, 526, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(400, 213, 'TP01', 527, 527, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(401, 213, 'TP01', 528, 528, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(402, 213, 'TP01', 529, 529, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(403, 213, 'TP01', 530, 530, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(404, 213, 'TP01', 531, 531, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(405, 213, 'TP01', 532, 532, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(406, 213, 'TP01', 533, 533, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(407, 213, 'TP01', 534, 534, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(408, 213, 'TP01', 535, 535, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(409, 213, 'TP01', 536, 536, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(410, 213, 'TP01', 537, 537, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(411, 213, 'TP01', 538, 538, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 255),
(423, 215, 'TH17', 6, 11, 6884, 1144, 41, 7, 4, 5, 3, 6, 0, 37, 6884, 1144, 1, 257),
(424, 151, 'TP11', 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 260),
(425, 151, 'TP11', 2, 2, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 261),
(426, 151, 'TP11', 3, 3, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 262),
(427, 151, 'TP11', 4, 4, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 262),
(428, 151, 'TP11', 5, 5, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 263),
(429, 151, 'TP11', 6, 6, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 263),
(430, 214, 'TP11', 1, 1, 25, 0, 5, 5, 1, 1, 0, 0, 0, 0, 25, 0, 1, 264),
(431, 214, 'TP11', 2, 2, 25, 0, 5, 5, 1, 1, 0, 0, 0, 0, 25, 0, 1, 264);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `piezas_procesadas_fg`
--

CREATE TABLE `piezas_procesadas_fg` (
  `id_piezas_procesadas_fg` int(10) UNSIGNED NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `total_piezas_recibidas` int(10) DEFAULT NULL,
  `total_piezas_aprobadas` int(10) DEFAULT NULL,
  `cambio_mog` int(10) UNSIGNED DEFAULT NULL,
  `total_scrap` int(11) NOT NULL,
  `verificacion` int(10) NOT NULL,
  `verificacion2` int(11) DEFAULT NULL,
  `sobrante_final` int(10) UNSIGNED DEFAULT NULL,
  `sobrante_final_grading_mas` int(10) UNSIGNED DEFAULT NULL,
  `sobrante_final_grading_menos` int(10) UNSIGNED DEFAULT NULL,
  `id_empleado` int(10) UNSIGNED NOT NULL,
  `num_canasta_completa` int(11) NOT NULL,
  `pza_canasta_completa` int(11) NOT NULL,
  `totalpiezas_procesadas` int(11) NOT NULL,
  `idmogcambio` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='\r\n';

--
-- Volcado de datos para la tabla `piezas_procesadas_fg`
--

INSERT INTO `piezas_procesadas_fg` (`id_piezas_procesadas_fg`, `registro_rbp_id_registro_rbp`, `total_piezas_recibidas`, `total_piezas_aprobadas`, `cambio_mog`, `total_scrap`, `verificacion`, `verificacion2`, `sobrante_final`, `sobrante_final_grading_mas`, `sobrante_final_grading_menos`, `id_empleado`, `num_canasta_completa`, `pza_canasta_completa`, `totalpiezas_procesadas`, `idmogcambio`) VALUES
(1, 16, 0, 100, 0, 0, 0, 0, 0, 0, 0, 149, 2, 50, 100, NULL),
(2, 12, 0, 5417, 0, 758, 0, 0, 153, 0, 0, 149, 28, 188, 6175, NULL),
(3, 4, 0, 4557, 0, 6812, 0, 0, 57, 0, 0, 149, 20, 225, 11369, NULL),
(4, 25, 0, 317, 0, 24, 0, 0, 29, 0, 0, 149, 6, 48, 341, NULL),
(5, 31, 0, 20025, 0, 60, 0, 0, 0, 0, 0, 755, 89, 225, 20085, NULL),
(6, 32, 0, 20025, 0, 26, 0, 0, 0, 0, 0, 758, 89, 225, 20051, NULL),
(7, 34, 0, 19040, 0, 96, 0, 0, 140, 0, 0, 758, 108, 175, 19136, NULL),
(8, 33, 0, 20025, 0, 30, 0, 0, 0, 0, 0, 745, 89, 225, 20055, NULL),
(9, 37, 0, 19912, 0, 49, 0, 0, 152, 0, 0, 756, 104, 190, 19961, NULL),
(10, 40, 0, 20286, 0, 27, 0, 0, 126, 0, 0, 745, 96, 210, 20313, NULL),
(11, 41, 0, 20160, 0, 23, 0, 0, 0, 0, 0, 145, 96, 210, 20183, NULL),
(12, 43, 0, 20250, 0, 112, 0, 0, 0, 0, 0, 149, 81, 250, 20362, NULL),
(13, 44, 0, 20075, 0, 44, 0, 0, 0, 0, 0, 746, 73, 275, 20119, NULL),
(14, 46, 0, 21630, 0, 69, 0, 0, 0, 0, 0, 745, 103, 210, 21699, NULL),
(15, 45, 0, 20286, 0, 80, 0, 0, 126, 0, 0, 745, 96, 210, 20366, NULL),
(16, 47, 0, 20075, 0, 284, 0, 0, 0, 0, 0, 739, 73, 275, 20359, NULL),
(17, 48, 0, 20075, 0, 40, 0, 0, 0, 0, 0, 739, 73, 275, 20115, NULL),
(18, 51, 0, 20075, 0, 126, 0, 0, 0, 0, 0, 739, 73, 275, 20201, NULL),
(19, 52, 0, 20075, 0, 114, 0, 0, 0, 0, 0, 739, 73, 275, 20189, NULL),
(20, 53, 0, 20160, 0, 50, 0, 0, 0, 0, 0, 740, 96, 210, 20210, NULL),
(21, 54, 0, 20160, 0, 55, 0, 0, 0, 0, 0, 741, 96, 210, 20215, NULL),
(22, 56, 0, 20208, 0, 92, 0, 0, 48, 0, 0, 752, 96, 210, 20300, NULL),
(23, 57, 0, 20105, 0, 93, 0, 0, 155, 0, 0, 751, 95, 210, 20198, NULL),
(24, 59, 0, 20402, 0, 38, 0, 0, 152, 0, 0, 145, 81, 250, 20440, NULL),
(25, 60, 0, 19166, 0, 148, 0, 0, 86, 0, 0, 145, 159, 120, 19314, NULL),
(26, 61, 0, 22260, 0, 53, 0, 0, 0, 0, 0, 148, 53, 420, 22313, NULL),
(27, 62, 0, 19522, 0, 26, 0, 0, 202, 0, 0, 739, 46, 420, 19548, NULL),
(28, 63, 0, 20580, 0, 37, 0, 0, 0, 0, 0, 149, 49, 420, 20617, NULL),
(29, 64, 0, 20025, 0, 90, 0, 0, 0, 0, 0, 753, 89, 225, 20115, NULL),
(30, 65, 0, 17005, 0, 46, 0, 0, 130, 0, 0, 756, 75, 225, 17051, NULL),
(31, 66, 0, 20000, 0, 92, 0, 0, 0, 0, 0, 169, 100, 200, 20092, NULL),
(32, 69, 0, 20160, 0, 129, 0, 0, 0, 0, 0, 170, 96, 210, 20289, NULL),
(33, 68, 0, 20160, 0, 38, 0, 0, 0, 0, 0, 148, 96, 210, 20198, NULL),
(34, 70, 0, 20160, 0, 31, 0, 0, 0, 0, 0, 755, 96, 210, 20191, NULL),
(35, 71, 0, 22412, 0, 55, 0, 0, 152, 0, 0, 755, 106, 210, 22467, NULL),
(36, 72, 0, 20000, 0, 54, 0, 0, 0, 0, 0, 745, 80, 250, 20054, NULL),
(37, 73, 0, 20000, 0, 69, 0, 0, 0, 0, 0, 745, 80, 250, 20069, NULL),
(38, 75, 0, 20580, 0, 49, 0, 0, 0, 0, 0, 148, 49, 420, 20629, NULL),
(39, 76, 0, 20160, 0, 138, 0, 0, 0, 0, 0, 743, 48, 420, 20298, NULL),
(40, 77, 0, 21645, 0, 44, 0, 0, 225, 0, 0, 743, 51, 420, 21689, NULL),
(41, 78, 0, 20160, 0, 40, 0, 0, 0, 0, 0, 747, 96, 210, 20200, NULL),
(42, 79, 0, 21732, 0, 76, 0, 0, 102, 0, 0, 755, 103, 210, 21808, NULL),
(43, 81, 0, 20273, 0, 98, 0, 0, 198, 0, 0, 739, 73, 275, 20371, NULL),
(44, 80, 0, 20075, 0, 540, 0, 0, 0, 0, 0, 739, 73, 275, 20615, NULL),
(45, 82, 0, 20000, 0, 49, 0, 0, 0, 0, 0, 740, 100, 200, 20049, NULL),
(46, 83, 0, 20029, 0, 135, 0, 0, 79, 0, 0, 744, 114, 175, 20164, NULL),
(47, 84, 0, 20075, 0, 90, 0, 0, 0, 0, 0, 145, 73, 275, 20165, NULL),
(48, 85, 0, 20580, 0, 94, 0, 0, 0, 0, 0, 740, 49, 420, 20674, NULL),
(49, 86, 0, 20000, 0, 29, 0, 0, 0, 0, 0, 746, 80, 250, 20029, NULL),
(50, 87, 0, 20160, 0, 74, 0, 0, 0, 0, 0, 757, 96, 210, 20234, NULL),
(51, 88, 0, 20000, 0, 41, 0, 0, 0, 0, 0, 738, 80, 250, 20041, NULL),
(52, 89, 0, 20160, 0, 46, 0, 0, 0, 0, 0, 747, 96, 210, 20206, NULL),
(53, 90, 0, 20150, 0, 65, 0, 0, 150, 0, 0, 739, 80, 250, 20215, NULL),
(54, 91, 0, 21000, 0, 36, 0, 0, 0, 0, 0, 738, 50, 420, 21036, NULL),
(55, 92, 0, 19388, 0, 278, 0, 0, 138, 0, 0, 742, 110, 175, 19666, NULL),
(56, 93, 0, 10050, 0, 117, 0, 0, 0, 0, 0, 174, 67, 150, 10167, NULL),
(57, 94, 0, 20125, 0, 60, 0, 0, 0, 0, 0, 738, 115, 175, 20185, NULL),
(58, 95, 0, 20160, 0, 27, 0, 0, 0, 0, 0, 755, 96, 210, 20187, NULL),
(59, 96, 0, 11725, 0, 104, 0, 0, 0, 0, 0, 753, 67, 175, 11829, NULL),
(60, 97, 0, 19950, 0, 103, 0, 0, 0, 0, 0, 756, 133, 150, 20053, NULL),
(61, 99, 0, 19982, 0, 63, 0, 0, 32, 0, 0, 740, 95, 210, 20045, NULL),
(62, 100, 0, 19992, 0, 40, 0, 0, 42, 0, 0, 740, 95, 210, 20032, NULL),
(63, 101, 0, 9759, 0, 33, 0, 0, 9, 0, 0, 755, 39, 250, 9792, NULL),
(64, 102, 0, 15750, 0, 43, 0, 0, 0, 0, 0, 743, 63, 250, 15793, NULL),
(65, 104, 0, 14908, 0, 44, 0, 0, 58, 0, 0, 746, 99, 150, 14952, NULL),
(66, 103, 0, 20400, 0, 93, 0, 0, 150, 0, 0, 745, 81, 250, 20493, NULL),
(67, 105, 0, 20160, 0, 92, 0, 0, 0, 0, 0, 744, 96, 210, 20252, NULL),
(68, 106, 0, 9769, 0, 35, 0, 0, 79, 0, 0, 747, 51, 190, 9804, NULL),
(69, 107, 0, 20202, 0, 69, 0, 0, 42, 0, 0, 148, 96, 210, 20271, NULL),
(70, 108, 0, 20140, 0, 52, 0, 0, 0, 0, 0, 745, 106, 190, 20192, NULL),
(71, 112, 0, 20140, 0, 110, 0, 0, 0, 0, 0, 745, 106, 190, 20250, NULL),
(72, 111, 0, 20000, 0, 85, 0, 0, 0, 0, 0, 738, 80, 250, 20085, NULL),
(73, 110, 0, 10070, 0, 14, 0, 0, 0, 0, 0, 745, 53, 190, 10084, NULL),
(74, 114, 0, 3420, 0, 10, 0, 0, 120, 0, 0, 745, 22, 150, 3430, NULL),
(75, 115, 0, 1846, 0, 14, 0, 0, 46, 0, 0, 746, 9, 200, 1860, NULL),
(76, 116, 0, 22680, 0, 60, 0, 0, 180, 0, 0, 737, 100, 225, 22740, NULL),
(77, 117, 0, 7241, 0, 40, 0, 0, 41, 0, 0, 741, 48, 150, 7281, NULL),
(78, 118, 0, 7080, 0, 56, 0, 0, 0, 0, 0, 757, 59, 120, 7136, NULL),
(79, 119, 0, 19950, 0, 47, 0, 0, 0, 0, 0, 753, 95, 210, 19997, NULL),
(80, 120, 0, 20075, 0, 82, 0, 0, 0, 0, 0, 758, 73, 275, 20157, NULL),
(81, 121, 0, 16270, 0, 32, 0, 0, 70, 0, 0, 748, 81, 200, 16302, NULL),
(82, 122, 0, 20000, 0, 37, 0, 0, 0, 0, 0, 756, 80, 250, 20037, NULL),
(83, 123, 0, 10200, 0, 212, 0, 0, 0, 0, 0, 737, 85, 120, 10412, NULL),
(84, 124, 0, 20075, 0, 60, 0, 0, 0, 0, 0, 755, 73, 275, 20135, NULL),
(85, 125, 0, 20075, 0, 277, 0, 0, 0, 0, 0, 737, 73, 275, 20352, NULL),
(86, 126, 0, 10048, 0, 279, 0, 0, 148, 0, 0, 746, 66, 150, 10327, NULL),
(87, 128, 600, 570, 0, 30, 0, 0, 570, 0, 0, 66, 0, 728, 600, NULL),
(88, 129, 20000, 19680, 0, 220, 100, 0, 192, 0, 0, 69, 29, 672, 19900, NULL),
(89, 131, 20000, 19471, 0, 529, 0, 0, 895, 0, 0, 42, 12, 1548, 20000, NULL),
(90, 132, 20000, 19983, 0, 186, -169, 0, 933, 0, 0, 42, 19, 1008, 20169, NULL),
(91, 134, 20000, 20003, 0, 147, -150, 0, 256, 0, 0, 47, 20, 980, 20150, NULL),
(92, 139, 20000, 19622, 0, 334, 44, 0, 422, 0, 0, 34, 15, 1280, 19956, NULL),
(93, 141, 4215, 3696, 0, 519, 0, 0, 176, 0, 0, 42, 8, 440, 4215, NULL),
(94, 142, 20000, 19909, 0, 218, 0, -127, 421, 0, 0, 123, 29, 672, 20127, NULL),
(95, 143, 20000, 19843, 0, 212, 0, -55, 355, 0, 0, 58, 28, 696, 20055, NULL),
(96, 147, 0, 20160, 0, 31, 0, 0, 0, 0, 0, 747, 48, 420, 20191, NULL),
(97, 148, 0, 20580, 0, 36, 0, 0, 0, 0, 0, 740, 98, 210, 20616, NULL),
(98, 152, 20000, 19705, 0, 245, 0, 50, 49, 0, 0, 66, 27, 728, 19950, NULL),
(99, 154, 0, 14025, 0, 38, 0, 0, 0, 0, 0, 745, 51, 275, 14063, NULL),
(100, 155, 0, 6000, 0, 19, 0, 0, 0, 0, 0, 757, 50, 120, 6019, NULL),
(101, 158, 20000, 20196, 0, 104, 0, -300, 540, 0, 0, 35, 27, 728, 20300, NULL),
(102, 159, 0, 20100, 0, 45, 0, 0, 150, 0, 0, 738, 114, 175, 20145, NULL),
(103, 160, 0, 20160, 0, 36, 0, 0, 0, 0, 0, 149, 48, 420, 20196, NULL),
(104, 161, 0, 6840, 0, 35, 0, 0, 0, 0, 0, 750, 57, 120, 6875, NULL),
(105, 163, 0, 20075, 0, 41, 0, 0, 0, 0, 0, 737, 73, 275, 20116, NULL),
(106, 165, 0, 20075, 0, 20, 0, 0, 0, 0, 0, 738, 73, 275, 20095, NULL),
(107, 166, 0, 20160, 0, 46, 0, 0, 0, 0, 0, 742, 48, 420, 20206, NULL),
(108, 167, 20000, 29785, 0, 434, -393, 9826, 777, 0, 0, 42, 28, 1036, 30219, NULL),
(109, 168, 14000, 13693, 0, 332, 0, -25, 1021, 0, 0, 131, 8, 1584, 14025, NULL),
(110, 169, 0, 20075, 0, 231, 0, 0, 0, 0, 0, 751, 73, 275, 20306, NULL),
(111, 171, 0, 20125, 0, 42, 0, 0, 0, 0, 0, 740, 115, 175, 20167, NULL),
(112, 172, 0, 11280, 0, 99, 0, 0, 0, 0, 0, 745, 94, 120, 11379, NULL),
(113, 173, 0, 10080, 0, 16, 0, 0, 0, 0, 0, 746, 48, 210, 10096, NULL),
(114, 174, 0, 3000, 0, 82, 0, 0, 0, 0, 0, 749, 20, 150, 3082, NULL),
(115, 180, 10000, 38444, 0, 192, 714, 29350, 644, 0, 0, 49, 45, 840, 38636, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `piezas_procesadas_grading`
--

CREATE TABLE `piezas_procesadas_grading` (
  `id_piezasGrading` int(11) NOT NULL,
  `stdE` varchar(20) DEFAULT NULL,
  `sobrante_inicial` int(11) DEFAULT NULL,
  `piezasxfila` int(11) DEFAULT NULL,
  `filas` int(11) DEFAULT NULL,
  `nivel` int(11) DEFAULT NULL,
  `canastas` int(11) DEFAULT NULL,
  `niveles_completos` int(11) DEFAULT NULL,
  `filas_completas` int(11) DEFAULT NULL,
  `sobrante` int(11) DEFAULT NULL,
  `cant_piezas_buenas` int(11) DEFAULT NULL,
  `id_piezasProcesadas` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `procesos`
--

CREATE TABLE `procesos` (
  `id_proceso` int(11) UNSIGNED NOT NULL,
  `descripcion` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `procesos`
--

INSERT INTO `procesos` (`id_proceso`, `descripcion`) VALUES
(1, 'MAQUINADO'),
(2, 'PRENSA'),
(3, 'T. SUPERFICIES'),
(4, 'EMPAQUE'),
(5, 'B/G GENERAL'),
(6, 'SLITTER'),
(7, 'COILING'),
(8, 'GRADING'),
(9, 'B/G EMPAQUE'),
(10, 'B/G PRENSA'),
(11, 'B/G ASSY'),
(12, 'B/G FORMING'),
(13, 'B/G COINING'),
(14, 'B/G GRINDING'),
(15, 'B/G CHAMFER'),
(16, 'B/G PLATINADO'),
(17, 'B/G ASSY/SEAL RING');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `razon_rechazo1`
--

CREATE TABLE `razon_rechazo1` (
  `id_razon_rechazo` int(10) UNSIGNED NOT NULL,
  `categoria_rechazo_id_categoria_rechazo` int(10) UNSIGNED NOT NULL,
  `nombre_rechazo` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `razon_rechazo1`
--

INSERT INTO `razon_rechazo1` (`id_razon_rechazo`, `categoria_rechazo_id_categoria_rechazo`, `nombre_rechazo`) VALUES
(1, 1, 'Desprendimiento de material'),
(2, 1, 'Rayón'),
(3, 1, 'Contaminación'),
(4, 1, 'Óxido'),
(5, 1, 'Otros'),
(6, 1, 'Rayón espalda'),
(7, 1, 'Rayón aleacion'),
(8, 1, 'Otros'),
(9, 1, 'Rayón espalda'),
(10, 1, 'Rayón aleación'),
(11, 1, 'Espesor'),
(12, 1, 'Otros'),
(13, 1, 'Rayón espalda'),
(14, 1, 'Golpe espalda'),
(15, 1, 'Dimensiones'),
(16, 1, 'Extensión'),
(17, 1, 'Deformidad'),
(18, 1, 'Torsión'),
(19, 1, 'Rebaba'),
(20, 1, 'Sello fuera de posición'),
(21, 1, 'Sello incompleto'),
(22, 1, 'Sello desvanecido'),
(23, 1, 'Área de contacto'),
(24, 1, 'Falla de alimentador'),
(25, 1, 'Bad mark'),
(26, 1, 'Óxido'),
(27, 1, 'Piezas caídas'),
(28, 1, 'Dandori'),
(29, 1, 'Inspección de calidad'),
(30, 1, 'Otros'),
(31, 1, 'Alimentador'),
(32, 1, 'Acabado de ancho'),
(33, 1, 'Verificador de ancho'),
(34, 1, 'Corte de esquinas'),
(35, 1, 'Perforación de orificio'),
(36, 1, 'Alivio de uña'),
(37, 1, 'Expulsión de uña'),
(38, 1, 'Canal'),
(39, 1, 'Chaflán de orificio'),
(40, 1, 'Cepillado'),
(41, 1, 'Corte de altura'),
(42, 1, 'Autochecador - altura'),
(43, 1, 'Autochecador - proceso'),
(44, 1, 'Pokayoke boring / broach'),
(45, 1, 'Espesor'),
(46, 1, 'Golpe cara interna'),
(47, 1, 'Daño en chaflán interno'),
(48, 1, 'Golpe chaflán ancho'),
(49, 1, 'Golpe en chaflan de canal'),
(50, 1, 'Golpe chaflán altura'),
(51, 1, 'Golpe espalda'),
(52, 1, 'Rebaba en perforación'),
(53, 1, 'Corte irregular de canal'),
(54, 1, 'Rebaba en orificio'),
(55, 1, 'Rebaba en uña'),
(56, 1, 'Rebaba en canal'),
(57, 1, 'Rayón cara interna'),
(58, 1, 'Rayón espalda'),
(59, 1, 'Rayón cara lateral'),
(60, 1, 'Óxido'),
(61, 1, 'Mal manejo'),
(62, 1, 'Dandori'),
(63, 1, 'Inspección de calidad'),
(64, 1, 'Otros'),
(65, 1, 'Desprendimiento de recubrimiento'),
(66, 1, 'Corriente anormal'),
(67, 1, 'Espesor'),
(68, 1, 'Corrosión electrolítica'),
(69, 1, 'Puntos negros'),
(70, 1, 'Adhesión de contaminante'),
(71, 1, 'Recubrimiento en espalda'),
(72, 1, 'Recubrimiento incompleto'),
(73, 1, 'Colapso'),
(74, 1, 'Rayón cara interna'),
(75, 1, 'Rayón espalda'),
(76, 1, 'Falla en el equipo'),
(77, 1, 'Óxido'),
(78, 1, 'Piezas caídas'),
(79, 1, 'Inspección de calidad'),
(80, 1, 'Burbujas'),
(81, 1, 'Blister'),
(82, 1, 'Sobrante'),
(83, 1, 'Daño por lavadora'),
(84, 1, 'Otros'),
(85, 1, 'Mal manejo'),
(86, 1, 'Rayón de empalme'),
(87, 1, 'Rayón por tesa'),
(88, 1, 'Desprendimiento de pintura'),
(89, 1, 'Pintura fuera de especificación'),
(90, 1, 'Rayón por falla de flujo de máquina'),
(91, 1, 'Óxido'),
(92, 1, 'Piezas caídas'),
(93, 1, 'Dandori'),
(94, 1, 'Inspección de calidad'),
(95, 1, 'Desprendimiento de impresión'),
(96, 1, 'Falta de carácter o 2D matriz'),
(97, 1, 'Impresión fuera de especificación'),
(98, 1, 'Otros'),
(99, 1, 'Después de empacar'),
(100, 1, 'Inspeccion de calidad'),
(101, 1, 'Otros'),
(102, 1, 'Bad Mark'),
(103, 1, 'Burbuja en polímero'),
(104, 1, 'Oxido bimetal'),
(105, 1, 'Poro en aleación'),
(106, 1, 'Rayón aleación'),
(107, 1, 'Rayón espalda'),
(108, 1, 'Scrap Cambio Rollo'),
(109, 1, 'Variación espesor'),
(110, 1, 'Hege'),
(111, 1, 'Rayón espalda'),
(112, 1, 'Rayón aleación'),
(113, 1, 'Otros'),
(114, 1, 'Chaflanes fuera de especificación'),
(115, 1, 'Mal corte de chaflán'),
(116, 1, 'Rebaba en polimero'),
(117, 1, 'Rebaba en chaflán'),
(118, 1, 'Contaminación aleación'),
(119, 1, 'Rayón espalda'),
(120, 1, 'Rayón aleación'),
(121, 1, 'Espesor'),
(122, 1, 'Otros'),
(123, 1, 'Ajuste de navajas'),
(124, 1, 'Ajustes de dandori'),
(125, 1, 'Ajustes de ancho'),
(126, 1, 'Canales'),
(127, 1, 'Chaflán fuera de especificación'),
(128, 1, 'Deformidad'),
(129, 1, 'Diámetro exterior'),
(130, 1, 'Diámetro interior'),
(131, 1, 'Espesor fuera especificación'),
(132, 1, 'Golpe en chaflán'),
(133, 1, 'Mal Corte chaflán'),
(134, 1, 'Mal corte en unión'),
(135, 1, 'Mal pulido'),
(136, 1, 'Marca de rebaba aleación'),
(137, 1, 'Marca de rebaba espalda'),
(138, 1, 'Muesca fuera especificación'),
(139, 1, 'Óxido'),
(140, 1, 'Piezas caídas'),
(141, 1, 'Piezas de inspección'),
(142, 1, 'Piezas iniciales (30 Piezas)'),
(143, 1, 'Piezas unidas (Tipo Cadena)'),
(144, 1, 'Rayón en superficie externa'),
(145, 1, 'Rayón en superficie Interna'),
(146, 1, 'Rebaba en chaflán'),
(147, 1, 'Rebaba en orificio'),
(148, 1, 'Rebaba polímero'),
(149, 1, 'Sello fuera posición'),
(150, 1, 'Sello desvanecido'),
(151, 1, 'Sello incompleto'),
(152, 1, 'Unión fuera de especificación'),
(153, 1, 'Otros'),
(154, 1, 'Recubrimiento interior'),
(155, 1, 'Falla de barril'),
(156, 1, 'Óxido'),
(157, 1, 'Piezas caídas'),
(158, 1, 'Inspección de calidad'),
(159, 1, 'Daño por lavadora'),
(160, 1, 'Otros'),
(161, 1, 'Óxido'),
(162, 1, 'Piezas caídas'),
(163, 1, 'Inspección de calidad'),
(164, 1, 'Otros'),
(165, 1, 'Después de empacar'),
(166, 1, 'Inspección de calidad'),
(167, 1, 'Otros'),
(168, 1, 'Marca por ensamble'),
(169, 1, 'R/G golpeado'),
(170, 1, 'Rechazo fuera de especificación'),
(171, 1, 'Otros'),
(172, 1, 'Sello defectuoso'),
(173, 1, 'Falla después de ensamble');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registrocausasparo`
--

CREATE TABLE `registrocausasparo` (
  `idregistrocausasparo` int(11) NOT NULL,
  `empleado_idempleado` int(10) UNSIGNED DEFAULT NULL,
  `causas_paro_idcausas_paro` int(10) UNSIGNED NOT NULL,
  `tiempo` int(11) NOT NULL,
  `detalle` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `hora_inicio` varchar(15) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` date NOT NULL,
  `linea` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL,
  `das_id_das` int(10) NOT NULL,
  `mog_idmog` int(11) NOT NULL,
  `hora_fin` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `registrocausasparo`
--

INSERT INTO `registrocausasparo` (`idregistrocausasparo`, `empleado_idempleado`, `causas_paro_idcausas_paro`, `tiempo`, `detalle`, `hora_inicio`, `fecha`, `linea`, `das_id_das`, `mog_idmog`, `hora_fin`) VALUES
(1, 746, 99, 26, '', '07:58', '2020-11-19', 'TP10', 53, 58, NULL),
(2, 145, 94, 27, '', '08:24', '2020-11-19', 'TP01', 54, 59, NULL),
(3, 746, 103, 22, 'BREAK ', '10:30', '2020-11-19', 'TP10', 55, 58, NULL),
(4, 145, 100, 28, '', '11:36', '2020-11-19', 'TP01', 56, 59, NULL),
(5, 746, 96, 40, '', '12:05', '2020-11-19', 'TP10', 55, 58, NULL),
(6, 149, 73, 22, '', '08:14', '2020-11-21', 'TP01', 57, 60, NULL),
(7, 149, 90, 15, '', '08:54', '2020-11-21', 'TP01', 58, 60, NULL),
(8, 148, 96, 28, '', '10:34', '2020-11-21', 'TP41', 60, 61, NULL),
(9, 149, 103, 28, 'BREAK', '10:55', '2020-11-21', 'TP01', 58, 60, NULL),
(10, 743, 100, 40, '', '13:41', '2020-12-08', 'TP41', 89, 77, NULL),
(11, 740, 73, 66, '', '07:17', '2020-12-18', 'TP41', 106, 85, NULL),
(12, 66, 36, 26, 'E AJUSTE INICIAL', '11:17', '2021-08-03', 'TH02', 175, 128, NULL),
(13, 66, 24, 122, '', '11:43', '2021-08-03', 'TH02', 175, 128, NULL),
(14, 69, 8, 23, '', '08:40', '2021-08-04', 'TH13', 176, 129, NULL),
(15, 131, 25, 26, '', '09:44', '2021-08-18', 'TH03', 181, 139, NULL),
(16, 42, 9, 12, '', '11:11', '2021-08-19', 'TH10', 185, 141, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registrocausasparocoiling`
--

CREATE TABLE `registrocausasparocoiling` (
  `idregistrocausasparo` int(11) NOT NULL,
  `empleado_idempleado` int(10) UNSIGNED NOT NULL,
  `causas_paro_idcausas_paro` int(10) UNSIGNED NOT NULL,
  `tiempo` int(11) NOT NULL,
  `detalle` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `hora_inicio` varchar(15) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` date NOT NULL,
  `linea` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL,
  `das_id_das` int(10) NOT NULL,
  `ordencoil_idordenc` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registrocausasparoslitter`
--

CREATE TABLE `registrocausasparoslitter` (
  `idregistrocausasparo` int(11) NOT NULL,
  `empleado_idempleado` int(10) UNSIGNED NOT NULL,
  `causas_paro_idcausas_paro` int(10) UNSIGNED NOT NULL,
  `tiempo` int(11) NOT NULL,
  `detalle` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `hora_inicio` varchar(15) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` date NOT NULL,
  `linea` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL,
  `das_id_das` int(10) NOT NULL,
  `ordenesslitter_idordenes` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_rbp`
--

CREATE TABLE `registro_rbp` (
  `id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `orden_manufactura` varchar(30) DEFAULT NULL,
  `proceso` varchar(30) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL,
  `activo_op` tinyint(1) NOT NULL,
  `aduana` tinyint(1) DEFAULT NULL,
  `mog_id_mog` int(11) DEFAULT NULL,
  `secuencia` int(11) NOT NULL,
  `loteTM` varchar(20) DEFAULT NULL,
  `sortingSupervisor` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `registro_rbp`
--

INSERT INTO `registro_rbp` (`id_registro_rbp`, `orden_manufactura`, `proceso`, `estado`, `activo_op`, `aduana`, `mog_id_mog`, `secuencia`, `loteTM`, `sortingSupervisor`) VALUES
(1, 'PRS001526', 'PRENSA', 1, 1, 1, 1, 10, 'TM310627', NULL),
(2, 'PRS001527', 'PRENSA', 1, 1, 1, 2, 10, 'TM310631', NULL),
(3, 'PRS002356', 'PRENSA', 1, 1, 1, 3, 10, 'TM303518', NULL),
(4, 'PRS001478', 'PRENSA', 0, 0, 0, 4, 10, 'TM310397', NULL),
(5, 'PRS002357', 'PRENSA', 1, 1, 1, 5, 10, 'TM303522', NULL),
(6, 'PRS002358', 'PRENSA', 1, 1, 1, 6, 10, 'TM303526', NULL),
(7, 'PRS002329', 'PRENSA', 1, 1, 1, 7, 10, 'TM303399', NULL),
(8, 'PRS002821', 'PRENSA', 1, 1, 1, 8, 10, 'TM305924', NULL),
(9, 'PRS006974', 'PRENSA', 1, 1, 1, 9, 10, 'TM3061339', NULL),
(10, 'PRS001346', 'PRENSA', 1, 1, 1, 10, 10, 'TM309535', NULL),
(11, 'PRS003624', 'PRENSA', 1, 1, 1, 11, 10, 'TM3090161', NULL),
(12, 'PRS012456', 'PRENSA', 0, 0, 0, 12, 10, 'TM317070072', NULL),
(13, 'PRS002987', 'PRENSA', 1, 1, 1, 13, 10, 'TM306800', NULL),
(14, 'PRS001689', 'PRENSA', 1, 1, 1, 14, 10, 'TM311464', NULL),
(15, 'PRS007874', 'PRENSA', 1, 1, 1, 15, 10, 'TM3080279', NULL),
(16, 'PRS001245', 'PRENSA', 0, 0, 0, 16, 10, 'TM309088', NULL),
(17, 'PRS001246', 'PRENSA', 1, 1, 1, 17, 10, 'TM309092', NULL),
(18, 'PRS012345', 'PRENSA', 1, 1, 1, 18, 10, 'TM317061791', NULL),
(19, 'PRS012349', 'PRENSA', 1, 1, 1, 19, 10, 'TM317061811', NULL),
(20, 'PRS031712', 'PRENSA', 1, 1, 1, 20, 10, 'TM320102713', NULL),
(21, 'PRS031665', 'PRENSA', 1, 1, 1, 21, 10, 'TM320102511', NULL),
(22, 'PRS023587', 'PRENSA', 1, 1, 1, 22, 10, 'TM319031700', NULL),
(23, 'PRS004752', 'PRENSA', 1, 1, 1, 23, 10, 'TM3010580', NULL),
(24, 'PRS001425', 'PRENSA', 1, 1, 1, 24, 10, 'TM310180', NULL),
(25, 'PRS005896', 'PRENSA', 0, 0, 0, 25, 10, 'TM3040510', NULL),
(26, 'PRS031635', 'PRENSA', 1, 1, 1, 26, 10, 'TM320102234', NULL),
(27, 'PRS031499', 'PRENSA', 1, 1, 1, 27, 10, 'TM320101507', NULL),
(28, 'PRS031679', 'PRENSA', 1, 1, 1, 28, 10, 'TM320102575', NULL),
(29, 'PRS031683', 'PRENSA', 1, 1, 1, 29, 10, 'TM320102591', NULL),
(30, 'PRS031707', 'PRENSA', 1, 1, 1, 30, 10, 'TM320102693', NULL),
(31, 'PRS031730', 'PRENSA', 0, 0, 0, 31, 10, 'TM320102793', NULL),
(32, 'PRS031794', 'PRENSA', 0, 0, 0, 32, 10, 'TM320103156', NULL),
(33, 'PRS031795', 'PRENSA', 0, 0, 0, 33, 10, 'TM320103161', NULL),
(34, 'PRS031792', 'PRENSA', 0, 0, 0, 34, 10, 'TM320103148', NULL),
(35, 'PRS002366', 'PRENSA', 1, 1, 1, 35, 10, 'TM303562', NULL),
(36, 'PRS031790', 'PRENSA', 1, 1, 1, 36, 10, 'TM320103138', NULL),
(37, 'PRS031791', 'PRENSA', 0, 0, 0, 37, 10, 'TM320103143', NULL),
(38, 'PRS031691', 'PRENSA', 1, 1, 1, 38, 10, 'TM320102625', NULL),
(39, 'PRS031769', 'PRENSA', 1, 1, 1, 39, 10, 'TM320103031', NULL),
(40, 'PRS031797', 'PRENSA', 0, 0, 0, 40, 10, 'TM320103170', NULL),
(41, 'PRS031772', 'PRENSA', 0, 0, 0, 41, 10, 'TM320103047', NULL),
(42, 'PRS003698', 'PRENSA', 1, 1, 1, 42, 10, 'TM3090596', NULL),
(43, 'PRS031696', 'PRENSA', 0, 0, 0, 43, 10, 'TM320102648', NULL),
(44, 'PRS031361', 'PRENSA', 0, 0, 0, 44, 10, 'TM320100700', NULL),
(45, 'PRS031851', 'PRENSA', 0, 0, 0, 45, 10, 'TM320110104', NULL),
(46, 'PRS031853', 'PRENSA', 0, 0, 0, 46, 10, 'TM320110112', NULL),
(47, 'PRS031904', 'PRENSA', 0, 0, 0, 47, 10, 'TM320110545', NULL),
(48, 'PRS031905', 'PRENSA', 0, 0, 0, 48, 10, 'TM320110550', NULL),
(49, 'PRS004989', 'PRENSA', 1, 1, 1, 49, 10, 'TM3020512', NULL),
(50, 'PRS031835', 'PRENSA', 1, 1, 1, 50, 10, 'TM320110033', NULL),
(51, 'PRS031953', 'PRENSA', 0, 0, 0, 51, 10, 'TM320110799', NULL),
(52, 'PRS031967', 'PRENSA', 0, 0, 0, 52, 10, 'TM320110952', NULL),
(53, 'PRS031959', 'PRENSA', 0, 0, 0, 53, 10, 'TM320110825', NULL),
(54, 'PRS031826', 'PRENSA', 0, 0, 0, 54, 10, 'TM320103305', NULL),
(55, 'PRS031650', 'PRENSA', 1, 1, 1, 55, 10, 'TM320102401', NULL),
(56, 'PRS032011', 'PRENSA', 0, 0, 0, 56, 10, 'TM320111151', NULL),
(57, 'PRS032012', 'PRENSA', 0, 0, 0, 57, 10, 'TM320111155', NULL),
(58, 'PRS032051', 'PRENSA', 1, 1, 1, 58, 10, 'TM320111459', NULL),
(59, 'PRS031999', 'PRENSA', 0, 0, 0, 59, 10, 'TM320111091', NULL),
(60, 'PRS032083', 'PRENSA', 0, 0, 0, 60, 10, 'TM320111660', NULL),
(61, 'PRS032077', 'PRENSA', 0, 0, 0, 61, 10, 'TM320111626', NULL),
(62, 'PRS032054', 'PRENSA', 0, 0, 0, 62, 10, 'TM320111490', NULL),
(63, 'PRS032075', 'PRENSA', 0, 0, 0, 63, 10, 'TM320111618', NULL),
(64, 'PRS032136', 'PRENSA', 0, 0, 0, 64, 10, 'TM320111991', NULL),
(65, 'PRS032162', 'PRENSA', 0, 0, 0, 65, 10, 'TM320112111', NULL),
(66, 'PRS032105', 'PRENSA', 1, 0, 1, 66, 10, 'TM320111779', NULL),
(67, 'PRS031834', 'PRENSA', 1, 1, 1, 67, 10, 'TM320110029', NULL),
(68, 'PRS032258', 'PRENSA', 0, 0, 0, 68, 10, 'TM320112626', NULL),
(69, 'PRS032182', 'PRENSA', 0, 0, 0, 69, 10, 'TM320112199', NULL),
(70, 'PRS032339', 'PRENSA', 0, 0, 0, 70, 10, 'TM320113067', NULL),
(71, 'PRS032295', 'PRENSA', 0, 0, 0, 71, 10, 'TM320112838', NULL),
(72, 'PRS032309', 'PRENSA', 0, 0, 0, 72, 10, 'TM320112934', NULL),
(73, 'PRS032346', 'PRENSA', 0, 0, 0, 73, 10, 'TM320113102', NULL),
(74, 'PRS000555', 'PRENSA', 1, 1, 1, 74, 10, 'T4D', NULL),
(75, 'PRS032359', 'PRENSA', 0, 0, 0, 75, 10, 'TM320120042', NULL),
(76, 'PRS032345', 'PRENSA', 0, 0, 0, 76, 10, 'TM320113098', NULL),
(77, 'PRS032344', 'PRENSA', 0, 0, 0, 77, 10, 'TM320113094', NULL),
(78, 'PRS032326', 'PRENSA', 0, 0, 0, 78, 10, 'TM320113013', NULL),
(79, 'PRS032394', 'PRENSA', 0, 0, 0, 79, 10, 'TM320120211', NULL),
(80, 'PRS032378', 'PRENSA', 0, 0, 0, 80, 10, 'TM320120141', NULL),
(81, 'PRS032379', 'PRENSA', 0, 0, 0, 81, 10, 'TM320120146', NULL),
(82, 'PRS032269', 'PRENSA', 0, 0, 0, 82, 10, 'TM320112727', NULL),
(83, 'PRS032384', 'PRENSA', 0, 0, 0, 83, 10, 'TM320120167', NULL),
(84, 'PRS032411', 'PRENSA', 0, 0, 0, 84, 10, 'TM320120312', NULL),
(85, 'PRS032543', 'PRENSA', 0, 0, 0, 85, 10, 'TM320121149', NULL),
(86, 'PRS032527', 'PRENSA', 0, 0, 0, 86, 10, 'TM320121034', NULL),
(87, 'PRS032561', 'PRENSA', 0, 0, 0, 87, 10, 'TM320121229', NULL),
(88, 'PRS032554', 'PRENSA', 0, 0, 0, 88, 10, 'TM320121195', NULL),
(89, 'PRS032586', 'PRENSA', 0, 0, 0, 89, 10, 'TM321010130', NULL),
(90, 'PRS032654', 'PRENSA', 0, 0, 0, 90, 10, 'TM321010530', NULL),
(91, 'PRS032682', 'PRENSA', 0, 0, 0, 91, 10, 'TM321010714', NULL),
(92, 'PRS032557', 'PRENSA', 0, 0, 0, 92, 10, 'TM320121210', NULL),
(93, 'PRS032611', 'PRENSA', 0, 0, 0, 93, 10, 'TM321010288', NULL),
(94, 'PRS032659', 'PRENSA', 0, 0, 0, 94, 10, 'TM321010550', NULL),
(95, 'PRS032501', 'PRENSA', 0, 0, 0, 95, 10, 'TM320120881', NULL),
(96, 'PRS032629', 'PRENSA', 0, 0, 0, 96, 10, 'TM321010371', NULL),
(97, 'PRS032666', 'PRENSA', 0, 0, 0, 97, 10, 'TM321010583', NULL),
(98, 'PRS032739', 'PRENSA', 1, 1, 1, 98, 10, 'TM321011059', NULL),
(99, 'PRS032784', 'PRENSA', 0, 0, 0, 99, 10, 'TM321011254', NULL),
(100, 'PRS032811', 'PRENSA', 0, 0, 0, 100, 10, 'TM321011417', NULL),
(101, 'PRS032765', 'PRENSA', 0, 0, 0, 101, 10, 'TM321011169', NULL),
(102, 'PRS032823', 'PRENSA', 0, 0, 0, 102, 10, 'TM321011469', NULL),
(103, 'PRS032726', 'PRENSA', 0, 0, 0, 103, 10, 'TM321010991', NULL),
(104, 'PRS032810', 'PRENSA', 0, 0, 0, 104, 10, 'TM321011413', NULL),
(105, 'PRS033273', 'PRENSA', 0, 0, 0, 105, 10, 'TM321021239', NULL),
(106, 'PRS033245', 'PRENSA', 0, 0, 0, 106, 10, 'TM321021027', NULL),
(107, 'PRS033378', 'PRENSA', 0, 0, 0, 107, 10, 'TM321021907', NULL),
(108, 'PRS033984', 'PRENSA', 0, 0, 0, 108, 10, 'TM321033519', NULL),
(109, 'PRS035180', 'PRENSA', 1, 1, 1, 109, 10, 'TM321060576', NULL),
(110, 'PRS035325', 'PRENSA', 0, 0, 0, 110, 10, 'TM321061362', NULL),
(111, 'PRS035245', 'PRENSA', 0, 0, 0, 111, 10, 'TM321060894', NULL),
(112, 'PRS035306', 'PRENSA', 0, 0, 0, 112, 10, 'TM321061243', NULL),
(113, 'PRS035334', 'PRENSA', 1, 1, 1, 113, 10, 'TM321061406', NULL),
(114, 'PRS035446', 'PRENSA', 0, 0, 0, 114, 10, 'TM321061936', NULL),
(115, 'PRS035563', 'PRENSA', 0, 0, 0, 115, 10, 'TM321062522', NULL),
(116, 'PRS035544', 'PRENSA', 0, 0, 0, 116, 10, 'TM321062421', NULL),
(117, 'PRS035584', 'PRENSA', 0, 0, 0, 117, 10, 'TM321062649', NULL),
(118, 'PRS035565', 'PRENSA', 0, 0, 0, 118, 10, 'TM321062540', NULL),
(119, 'PRS035547', 'PRENSA', 0, 0, 0, 119, 10, 'TM321062442', NULL),
(120, 'PRS035976', 'PRENSA', 0, 0, 0, 120, 10, 'TM321071698', NULL),
(121, 'PRS035936', 'PRENSA', 0, 0, 0, 121, 10, 'TM321071392', NULL),
(122, 'PRS035789', 'PRENSA', 0, 0, 0, 122, 10, 'TM321070496', NULL),
(123, 'PRS035950', 'PRENSA', 0, 0, 0, 123, 10, 'TM321071515', NULL),
(124, 'PRS035934', 'PRENSA', 0, 0, 0, 124, 10, 'TM321071382', NULL),
(125, 'PRS035985', 'PRENSA', 0, 0, 0, 125, 10, 'TM321071738', NULL),
(126, 'PRS035909', 'PRENSA', 0, 0, 0, 126, 10, 'TM321071218', NULL),
(127, 'HBL037124', 'MAQUINADO', 1, 1, 1, 127, 20, 'TM321071857', NULL),
(128, 'HBL037304', 'MAQUINADO', 0, 0, 0, 128, 20, 'TM321072956', NULL),
(129, 'HBL037336', 'MAQUINADO', 0, 0, 0, 129, 20, 'TM321080020', NULL),
(130, 'HBL037205', 'MAQUINADO', 1, 1, 1, 130, 20, 'TM321072383', NULL),
(131, 'HBL037010', 'MAQUINADO', 0, 0, 0, 131, 20, 'TM321071187', NULL),
(132, 'HBL037321', 'MAQUINADO', 0, 0, 1, 132, 20, 'TM321073043', NULL),
(133, 'HBL037382', 'MAQUINADO', 1, 1, 1, 133, 20, 'TM321080263', NULL),
(134, 'HBL037417', 'MAQUINADO', 1, 0, 1, 134, 20, 'TM321080432', NULL),
(135, 'HBL037423', 'MAQUINADO', 1, 1, 1, 135, 20, 'TM321080461', NULL),
(136, 'HBL037186', 'MAQUINADO', 1, 1, 1, 136, 20, 'TM321072253', NULL),
(137, 'HBL037435', 'MAQUINADO', 1, 1, 1, 137, 20, 'TM321080522', NULL),
(138, 'HBL037394', 'MAQUINADO', 1, 1, 1, 138, 20, 'TM321080324', NULL),
(139, 'HBL037454', 'MAQUINADO', 0, 0, 0, 139, 20, 'TM321080641', NULL),
(140, 'PRS036305', 'PRENSA', 1, 1, 1, 140, 10, 'TM321080534', NULL),
(141, 'HBL037021', 'MAQUINADO', 0, 0, 0, 141, 20, 'TM321071235', NULL),
(142, 'HBL037516', 'MAQUINADO', 0, 0, 0, 142, 20, 'TM321081084', NULL),
(143, 'HBL037426', 'MAQUINADO', 0, 0, 0, 143, 20, 'TM321080479', NULL),
(144, 'HBL037557', 'MAQUINADO', 1, 1, 1, 144, 20, 'TM321081280', NULL),
(145, 'PLT014274', 'B/G PLATINADO', 1, 1, 1, 145, 40, 'TM321071622', NULL),
(146, 'PLT014292', 'B/G PLATINADO', 1, 1, 1, 146, 40, 'TM321071800', NULL),
(147, 'PRS036419', 'PRENSA', 0, 0, 0, 147, 10, 'TM321081259', NULL),
(148, 'PRS036521', 'PRENSA', 0, 0, 0, 148, 10, 'TM321081833', NULL),
(149, 'PRS036159', 'PRENSA', 1, 1, 1, 149, 10, 'TM321072848', NULL),
(150, 'PRS036504', 'PRENSA', 1, 1, 1, 150, 10, 'TM321081736', NULL),
(151, 'PRS036448', 'PRENSA', 1, 1, 1, 151, 10, 'TM321081397', NULL),
(152, 'HBL037505', 'MAQUINADO', 0, 0, 0, 152, 20, 'TM321081030', NULL),
(153, 'HBL037634', 'MAQUINADO', 1, 1, 1, 153, 20, 'TM321081725', NULL),
(154, 'PRS036535', 'PRENSA', 0, 0, 0, 154, 10, 'TM321081937', NULL),
(155, 'PRS036582', 'PRENSA', 0, 0, 0, 155, 10, 'TM321082183', NULL),
(156, 'PRS036454', 'PRENSA', 1, 1, 1, 156, 10, 'TM321081459', NULL),
(157, 'HBL037293', 'MAQUINADO', 1, 1, 1, 157, 20, 'TM321072861', NULL),
(158, 'HBL037586', 'MAQUINADO', 0, 0, 0, 158, 20, 'TM321081456', NULL),
(159, 'PRS036059', 'PRENSA', 0, 0, 0, 159, 10, 'TM321072279', NULL),
(160, 'PRS036529', 'PRENSA', 0, 0, 0, 160, 10, 'TM321081887', NULL),
(161, 'PRS036581', 'PRENSA', 0, 0, 0, 161, 10, 'TM321082178', NULL),
(162, 'PRS036537', 'PRENSA', 1, 1, 1, 162, 10, 'TM321081945', NULL),
(163, 'PRS036669', 'PRENSA', 0, 0, 0, 163, 10, 'TM321082746', NULL),
(164, 'HBL037736', 'MAQUINADO', 1, 1, 1, 164, 20, 'TM321082302', NULL),
(165, 'PRS036670', 'PRENSA', 0, 0, 0, 165, 10, 'TM321082751', NULL),
(166, 'PRS036548', 'PRENSA', 0, 0, 0, 166, 10, 'TM321081989', NULL),
(167, 'HBL037373', 'MAQUINADO', 0, 0, 1, 167, 20, 'TM321080223', NULL),
(168, 'HBL037294', 'MAQUINADO', 0, 0, 0, 168, 20, 'TM321072865', NULL),
(169, 'PRS036885', 'PRENSA', 0, 0, 0, 169, 10, 'TM321090673', NULL),
(170, 'PRS036958', 'PRENSA', 1, 1, 1, 170, 10, 'TM321091162', NULL),
(171, 'PRS036898', 'PRENSA', 0, 0, 0, 171, 10, 'TM321090787', NULL),
(172, 'PRS036689', 'PRENSA', 0, 0, 0, 172, 10, 'TM321082976', NULL),
(173, 'PRS036865', 'PRENSA', 0, 0, 0, 173, 10, 'TM321090538', NULL),
(174, 'PRS036980', 'PRENSA', 0, 0, 0, 174, 10, 'TM321091336', NULL),
(175, 'PRS036578', 'PRENSA', 1, 1, 1, 175, 10, 'TM321082163', NULL),
(176, 'PRS036848', 'PRENSA', 1, 1, 1, 176, 10, 'TM321090436', NULL),
(177, 'PLT014591', 'B/G PLATINADO', 1, 1, 1, 177, 40, 'TM321083122', NULL),
(178, 'HBL037885', 'MAQUINADO', 1, 1, 1, 178, 20, 'TM321083265', NULL),
(179, 'HBL037911', 'MAQUINADO', 1, 1, 1, 179, 20, 'TM321090059', NULL),
(180, 'HBL037762', 'MAQUINADO', 0, 0, 1, 180, 20, 'TM321082468', NULL),
(181, 'HBL038344', 'MAQUINADO', 1, 1, 1, 181, 20, 'TM321100444', NULL),
(182, 'HBL014525', 'MAQUINADO', 1, 1, 1, 182, 20, 'TM317092536', NULL),
(183, 'HBL002526', 'MAQUINADO', 1, 1, 1, 183, 20, 'TM303954', NULL),
(184, 'HBL005121', 'MAQUINADO', 1, 1, 1, 184, 20, 'TM3020430', NULL),
(185, 'HBL015021', 'MAQUINADO', 1, 1, 1, 185, 20, 'TM317101263', NULL),
(186, 'HBL014025', 'MAQUINADO', 1, 1, 1, 186, 20, 'TM317090006', NULL),
(187, 'HBL022534', 'MAQUINADO', 1, 1, 1, 187, 20, 'TM318112161', NULL),
(188, 'HBL011526', 'MAQUINADO', 1, 1, 1, 188, 20, 'TM317040430', NULL),
(189, 'PLT010206', 'B/G PLATINADO', 1, 1, 1, 189, 40, 'TM319091038', NULL),
(190, 'PLT010315', 'T. SUPERFICIES', 1, 1, 1, 190, 40, 'TM319092600', NULL),
(191, 'HBL015126', 'MAQUINADO', 1, 1, 1, 191, 20, 'TM317101787', NULL),
(192, 'HBL010626', 'MAQUINADO', 1, 1, 1, 192, 20, 'TM317020372', NULL),
(193, 'HBL010505', 'MAQUINADO', 1, 1, 1, 193, 20, 'TM317012058', NULL),
(194, 'HBL002626', 'MAQUINADO', 1, 1, 1, 194, 20, 'TM304430', NULL),
(195, 'HBL002625', 'MAQUINADO', 1, 1, 1, 195, 20, 'TM304425', NULL),
(196, 'HBL009911', 'MAQUINADO', 1, 1, 1, 196, 20, 'TM316120818', NULL),
(197, 'HBL001425', 'MAQUINADO', 1, 1, 1, 24, 20, 'TM310181', NULL),
(198, 'HBL011424', 'MAQUINADO', 1, 1, 1, 197, 20, 'TM317032590', NULL),
(199, 'HBL001516', 'MAQUINADO', 1, 1, 1, 198, 20, 'TM310588', NULL),
(200, 'HBL016225', 'MAQUINADO', 1, 1, 1, 199, 20, 'TM317122355', NULL),
(201, 'HBL016174', 'MAQUINADO', 1, 1, 1, 200, 20, 'TM317122126', NULL),
(202, 'HBL016588', 'MAQUINADO', 1, 1, 1, 201, 20, 'TM318011616', NULL),
(203, 'HBL012574', 'MAQUINADO', 1, 1, 1, 202, 20, 'TM317060920', NULL),
(204, 'HBL014242', 'MAQUINADO', 1, 1, 1, 203, 20, 'TM317091187', NULL),
(205, 'HBL010404', 'MAQUINADO', 1, 1, 1, 204, 20, 'TM317011540', NULL),
(206, 'HBL011242', 'MAQUINADO', 1, 1, 1, 205, 20, 'TM317031711', NULL),
(207, 'HBL021022', 'MAQUINADO', 1, 1, 1, 206, 20, 'TM318083042', NULL),
(208, 'HBL024425', 'MAQUINADO', 1, 1, 1, 207, 20, 'TM319032768', NULL),
(209, 'HBL012575', 'MAQUINADO', 1, 1, 1, 208, 20, 'TM317060925', NULL),
(210, 'BHL010089', 'B/G PRENSA', 1, 1, 1, 209, 10, 'TM322041880', NULL),
(211, 'HBL010225', 'MAQUINADO', 1, 1, 1, 210, 20, 'TM317010655', NULL),
(212, 'PRS038253', NULL, 1, 1, 1, 211, 10, NULL, NULL),
(213, 'PRS048524', 'PRENSA', 1, 1, 1, 212, 10, 'TM323121594', NULL),
(214, 'PRS048525', 'PRENSA', 1, 1, 1, 213, 10, 'TM323121599', NULL),
(215, 'HBL050114', 'MAQUINADO', 1, 1, 1, 213, 20, 'TM323121600', NULL),
(218, 'PRS054689', 'PRENSA', 1, 1, 1, 216, 10, 'TM325012130', NULL),
(220, 'HBL056746', 'MAQUINADO', 1, 1, 1, 216, 20, 'TM325012131', NULL),
(221, 'PRS054688', 'PRENSA', 1, 1, 1, 218, 10, 'TM325012125', NULL),
(222, 'HBL056745', 'MAQUINADO', 1, 1, 1, 218, 20, 'TM325012126', NULL),
(223, 'HBL056743', 'MAQUINADO', 1, 1, 1, 219, 20, 'TM325012101', NULL),
(224, 'HBL056741', 'MAQUINADO', 1, 1, 1, 220, 20, 'TM325012092', NULL),
(225, 'HBL056740', 'MAQUINADO', 1, 1, 1, 221, 20, 'TM325012088', NULL),
(226, 'HBL056723', 'MAQUINADO', 1, 1, 1, 222, 20, 'TM325011944', NULL),
(227, 'HBL056748', 'MAQUINADO', 1, 1, 1, 223, 20, 'TM325012139', NULL),
(228, 'HBL056742', 'MAQUINADO', 1, 1, 1, 224, 20, 'TM325012096', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_x_hora`
--

CREATE TABLE `registro_x_hora` (
  `id_registro_x_hora` int(11) NOT NULL,
  `hora` varchar(20) NOT NULL,
  `cantidadxhora` int(11) NOT NULL,
  `acumulado` int(11) NOT NULL,
  `comentarios` longtext NOT NULL,
  `fecha` date NOT NULL,
  `mog_id_mog` int(11) NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `linea` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `registro_x_hora`
--

INSERT INTO `registro_x_hora` (`id_registro_x_hora`, `hora`, `cantidadxhora`, `acumulado`, `comentarios`, `fecha`, `mog_id_mog`, `registro_rbp_id_registro_rbp`, `linea`) VALUES
(22, '11:23:43', 1000, 1000, 'ASDF', '2025-03-03', 216, 218, 'TP08'),
(23, '11:40:11', 4000, 5000, 'ASFD', '2025-03-03', 216, 218, 'TP08'),
(24, '12:30:44', 0, 5000, 'SI NO SALE 0, SOY TOJO', '2025-03-03', 216, 218, 'TP08');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_x_hora_maq`
--

CREATE TABLE `registro_x_hora_maq` (
  `id_registro_x_hora` int(11) NOT NULL,
  `hora` varchar(20) NOT NULL,
  `cantidadxhora` int(11) NOT NULL,
  `acumulado` int(11) NOT NULL,
  `ok_ng` varchar(2) NOT NULL,
  `fecha` date NOT NULL,
  `linea` varchar(20) NOT NULL,
  `mog_id_mog` int(11) NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `empleado_id_empleado` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `registro_x_hora_maq`
--

INSERT INTO `registro_x_hora_maq` (`id_registro_x_hora`, `hora`, `cantidadxhora`, `acumulado`, `ok_ng`, `fecha`, `linea`, `mog_id_mog`, `registro_rbp_id_registro_rbp`, `empleado_id_empleado`) VALUES
(42, '07:50:22', 5000, 5000, 'OK', '2025-03-04', 'TH17', 216, 220, 49),
(43, '08:00:01', 3000, 8000, 'OK', '2025-03-04', 'TH17', 216, 220, 49);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tiempo`
--

CREATE TABLE `tiempo` (
  `id_tiempos` int(10) UNSIGNED NOT NULL,
  `registro_rbp_id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `turno` int(10) UNSIGNED DEFAULT NULL,
  `hora_inicio` varchar(30) DEFAULT NULL,
  `hora_fin` varchar(30) DEFAULT NULL,
  `horas_trabajadas` varchar(10) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `pza_pro_id` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `tiempo`
--

INSERT INTO `tiempo` (`id_tiempos`, `registro_rbp_id_registro_rbp`, `turno`, `hora_inicio`, `hora_fin`, `horas_trabajadas`, `fecha`, `pza_pro_id`) VALUES
(1, 31, 1, '15:50:45', '15:55:59', '00:05', '2020-10-30', 1),
(2, 31, 1, '15:58:08', '16:00:53', '00:02', '2020-10-30', 2),
(3, 31, 1, '16:01:28', '16:07:39', '00:06', '2020-10-30', 3),
(4, 32, 1, '16:05:09', '16:11:00', '00:06', '2020-10-30', 4),
(5, 32, 2, '16:21:14', '16:24:27', '00:03', '2020-10-30', 5),
(6, 33, 1, '11:48:12', '11:54:26', '00:06', '2020-11-02', 6),
(7, 34, 1, '11:50:39', '12:04:51', '00:14', '2020-11-02', 7),
(8, 33, 1, '11:58:14', '12:09:28', '00:11', '2020-11-02', 8),
(9, 35, 2, '18:07:14', '18:09:39', '00:02', '2020-11-02', 9),
(10, 37, 1, '13:41:17', '13:44:29', '00:03', '2020-11-03', 10),
(11, 36, 1, '13:40:49', '13:47:37', '00:07', '2020-11-03', 11),
(12, 37, 1, '13:45:31', '13:50:14', '00:05', '2020-11-03', 12),
(13, 36, 1, '13:48:23', '13:51:58', '00:03', '2020-11-03', 13),
(14, 36, 1, '13:52:45', '13:55:06', '00:03', '2020-11-03', 14),
(15, 37, 1, '13:55:51', '13:57:28', '00:02', '2020-11-03', 15),
(16, 39, 1, '14:10:15', '14:13:36', '00:03', '2020-11-04', 16),
(17, 40, 1, '14:10:30', '14:14:56', '00:04', '2020-11-04', 17),
(18, 41, 1, '14:20:02', '14:23:00', '00:03', '2020-11-04', 18),
(19, 39, 1, '14:19:18', '14:23:54', '00:04', '2020-11-04', 19),
(20, 39, 1, '14:24:56', '14:28:30', '00:04', '2020-11-04', 20),
(21, 42, 1, '15:04:00', '15:06:03', '00:02', '2020-11-04', 21),
(22, 44, 1, '11:50:29', '11:54:35', '00:04', '2020-11-05', 22),
(23, 43, 1, '11:50:35', '11:58:13', '00:08', '2020-11-05', 23),
(24, 44, 1, '11:55:09', '12:00:18', '00:05', '2020-11-05', 24),
(25, 46, 1, '13:52:03', '13:55:59', '00:03', '2020-11-06', 25),
(26, 45, 1, '13:51:36', '13:56:11', '00:05', '2020-11-06', 26),
(27, 45, 1, '13:56:58', '15:17:57', '01:21', '2020-11-06', 27),
(28, 47, 1, '11:14:36', '11:26:13', '00:12', '2020-11-10', 28),
(29, 47, 1, '11:35:48', '12:01:29', '00:26', '2020-11-10', 29),
(30, 48, 1, '12:09:09', '12:13:28', '00:04', '2020-11-10', 30),
(31, 3, 2, '17:59:39', '18:03:32', '00:04', '2020-11-10', 31),
(32, 49, 2, '18:07:12', '18:09:02', '00:02', '2020-11-10', 32),
(33, 50, 1, '12:23:58', '12:27:28', '00:04', '2020-11-11', 33),
(34, 51, 1, '15:32:06', '15:44:59', '00:12', '2020-11-12', 34),
(35, 51, 1, '15:55:58', '16:03:06', '00:08', '2020-11-12', 35),
(36, 52, 1, '16:07:08', '16:15:15', '00:08', '2020-11-12', 36),
(37, 52, 2, '16:16:02', '16:20:19', '00:04', '2020-11-12', 37),
(38, 53, 1, '15:18:03', '15:23:43', '00:05', '2020-11-13', 38),
(39, 53, 1, '15:26:31', '15:28:43', '00:02', '2020-11-13', 39),
(40, 54, 1, '15:25:34', '15:33:00', '00:08', '2020-11-13', 40),
(41, 54, 1, '15:33:48', '15:36:10', '00:03', '2020-11-13', 41),
(42, 54, 1, '15:36:39', '15:41:21', '00:05', '2020-11-13', 42),
(43, 54, 1, '15:41:59', '15:46:09', '00:05', '2020-11-13', 43),
(44, 54, 1, '15:47:28', '15:51:59', '00:04', '2020-11-13', 44),
(45, 56, 1, '15:38:03', '15:44:15', '00:06', '2020-11-17', 45),
(46, 56, 1, '15:44:55', '15:52:25', '00:08', '2020-11-17', 46),
(47, 57, 1, '15:53:10', '15:57:39', '00:04', '2020-11-17', 47),
(48, 59, 1, '07:35:33', '10:25:57', '02:50', '2020-11-19', 48),
(49, 58, 1, '07:26:05', '10:28:45', '03:02', '2020-11-19', 49),
(50, 58, 1, '10:29:41', '13:04:30', '02:35', '2020-11-19', 50),
(51, 59, 1, '10:44:32', '15:36:48', '04:52', '2020-11-19', 51),
(52, 58, 1, '13:05:49', '16:50:17', '03:45', '2020-11-19', 52),
(53, 60, 1, '08:11:27', '12:53:11', '04:42', '2020-11-21', 53),
(54, 60, 1, '13:01:04', '14:05:18', '01:04', '2020-11-21', 54),
(55, 61, 1, '08:40:17', '14:55:53', '06:15', '2020-11-21', 55),
(56, 62, 1, '15:16:08', '15:21:17', '00:05', '2020-11-23', 56),
(57, 63, 1, '15:28:23', '15:32:24', '00:04', '2020-11-23', 57),
(58, 64, 1, '16:05:04', '16:09:03', '00:04', '2020-11-24', 58),
(59, 65, 1, '16:06:08', '16:12:21', '00:06', '2020-11-24', 59),
(60, 65, 1, '16:14:49', '16:18:05', '00:04', '2020-11-24', 60),
(61, 66, 1, '15:49:31', '15:53:37', '00:04', '2020-11-25', 61),
(62, 66, 1, '15:54:17', '15:56:11', '00:02', '2020-11-25', 62),
(63, 69, 1, '10:18:48', '10:22:30', '00:04', '2020-11-30', 63),
(64, 68, 1, '10:09:56', '11:06:13', '00:57', '2020-11-30', 64),
(65, 68, 1, '11:07:44', '11:12:07', '00:05', '2020-11-30', 65),
(66, 70, 1, '15:33:02', '15:37:05', '00:04', '2020-12-02', 66),
(67, 71, 1, '15:33:08', '15:38:56', '00:05', '2020-12-02', 67),
(68, 70, 1, '15:37:46', '15:41:43', '00:04', '2020-12-02', 68),
(69, 71, 1, '15:41:28', '15:45:21', '00:04', '2020-12-02', 69),
(70, 72, 1, '14:32:15', '14:37:31', '00:05', '2020-12-07', 70),
(71, 73, 1, '14:34:35', '14:39:23', '00:05', '2020-12-07', 71),
(72, 72, 1, '14:38:16', '14:42:39', '00:04', '2020-12-07', 72),
(73, 73, 1, '14:40:25', '14:44:36', '00:04', '2020-12-07', 73),
(76, 75, 1, '11:26:07', '11:29:46', '00:03', '2020-12-08', 76),
(77, 75, 1, '11:33:44', '11:38:27', '00:05', '2020-12-08', 77),
(78, 76, 1, '11:39:42', '11:44:26', '00:05', '2020-12-08', 78),
(79, 77, 1, '13:33:16', '13:38:37', '00:05', '2020-12-08', 79),
(80, 77, 1, '13:39:18', '14:25:04', '00:46', '2020-12-08', 80),
(81, 78, 1, '11:38:10', '11:41:22', '00:03', '2020-12-09', 81),
(82, 78, 1, '11:42:01', '11:46:36', '00:04', '2020-12-09', 82),
(83, 79, 1, '12:32:21', '12:36:20', '00:04', '2020-12-09', 83),
(84, 79, 1, '12:37:22', '12:40:29', '00:03', '2020-12-09', 84),
(85, 80, 1, '11:53:03', '12:02:59', '00:09', '2020-12-10', 85),
(86, 81, 1, '11:59:34', '12:05:22', '00:06', '2020-12-10', 86),
(87, 80, 1, '12:03:50', '12:13:55', '00:10', '2020-12-10', 87),
(88, 81, 1, '12:06:15', '12:08:36', '00:02', '2020-12-10', 88),
(89, 82, 1, '15:45:03', '15:48:04', '00:03', '2020-12-11', 89),
(90, 83, 1, '15:45:31', '15:50:42', '00:05', '2020-12-11', 90),
(91, 83, 1, '15:51:45', '15:54:28', '00:03', '2020-12-11', 91),
(92, 84, 1, '12:27:31', '12:32:35', '00:05', '2020-12-16', 92),
(93, 84, 1, '12:34:20', '12:38:21', '00:04', '2020-12-16', 93),
(94, 85, 1, '07:13:44', '14:35:39', '07:22', '2020-12-18', 94),
(95, 85, 1, '14:36:54', '14:39:13', '00:03', '2020-12-18', 95),
(96, 86, 1, '15:47:29', '15:50:03', '00:03', '2021-01-05', 96),
(97, 87, 1, '15:49:32', '15:55:34', '00:06', '2021-01-05', 97),
(98, 87, 1, '15:56:18', '16:03:24', '00:07', '2021-01-05', 98),
(99, 88, 1, '16:05:54', '16:11:39', '00:06', '2021-01-06', 99),
(100, 88, 1, '16:12:43', '16:16:02', '00:04', '2021-01-06', 100),
(101, 89, 1, '15:47:15', '15:50:00', '00:03', '2021-01-08', 101),
(102, 89, 1, '15:50:33', '15:52:29', '00:02', '2021-01-08', 102),
(103, 90, 1, '12:34:48', '12:39:58', '00:05', '2021-01-13', 103),
(104, 90, 1, '12:40:59', '12:45:07', '00:05', '2021-01-13', 104),
(105, 91, 1, '12:51:12', '12:55:59', '00:04', '2021-01-13', 105),
(106, 91, 1, '12:56:37', '12:59:30', '00:03', '2021-01-13', 106),
(107, 92, 1, '15:30:16', '15:37:13', '00:07', '2021-01-14', 107),
(108, 92, 1, '15:37:52', '15:42:31', '00:05', '2021-01-14', 108),
(109, 93, 1, '15:34:34', '15:45:38', '00:11', '2021-01-14', 109),
(110, 94, 1, '12:01:37', '12:06:50', '00:05', '2021-01-15', 110),
(111, 94, 1, '12:07:18', '12:13:42', '00:06', '2021-01-15', 111),
(112, 95, 1, '12:15:37', '12:18:38', '00:03', '2021-01-15', 112),
(113, 96, 1, '14:58:19', '15:03:38', '00:05', '2021-01-18', 113),
(114, 97, 1, '15:05:25', '15:27:24', '00:22', '2021-01-18', 114),
(115, 97, 1, '15:28:08', '15:33:23', '00:05', '2021-01-18', 115),
(116, 97, 1, '15:34:00', '15:38:45', '00:04', '2021-01-18', 116),
(117, 99, 1, '14:21:57', '14:25:36', '00:04', '2021-01-21', 117),
(118, 99, 1, '14:26:02', '14:29:35', '00:03', '2021-01-21', 118),
(119, 100, 1, '14:43:35', '14:46:42', '00:03', '2021-01-21', 119),
(120, 100, 1, '14:47:38', '14:50:42', '00:03', '2021-01-21', 120),
(121, 100, 1, '14:51:09', '14:54:18', '00:03', '2021-01-21', 121),
(122, 101, 1, '12:31:21', '12:33:51', '00:02', '2021-01-22', 122),
(123, 101, 1, '12:34:09', '12:42:34', '00:08', '2021-01-22', 123),
(124, 102, 1, '12:44:02', '12:46:52', '00:02', '2021-01-22', 124),
(125, 104, 1, '14:52:40', '14:55:37', '00:03', '2021-01-27', 125),
(126, 103, 1, '14:44:00', '15:00:46', '00:16', '2021-01-27', 126),
(127, 103, 1, '15:01:08', '15:04:20', '00:03', '2021-01-27', 127),
(128, 105, 1, '14:42:03', '14:49:11', '00:07', '2021-02-18', 128),
(129, 105, 1, '14:50:30', '14:53:16', '00:03', '2021-02-18', 129),
(130, 106, 1, '14:55:23', '14:57:51', '00:02', '2021-02-18', 130),
(131, 107, 1, '11:40:00', '11:43:43', '00:03', '2021-02-24', 131),
(132, 107, 1, '11:44:15', '11:47:28', '00:03', '2021-02-24', 132),
(133, 108, 1, '10:29:58', '10:37:19', '00:08', '2021-04-19', 133),
(134, 109, 1, '12:34:33', '16:55:18', '04:21', '2021-06-14', 134),
(135, 109, 2, '17:04:55', '17:08:01', '00:04', '2021-06-14', 135),
(136, 111, 1, '08:52:43', '08:58:32', '00:06', '2021-06-15', 136),
(137, 112, 1, '09:01:48', '09:07:58', '00:06', '2021-06-15', 137),
(138, 112, 1, '14:40:50', '14:44:21', '00:04', '2021-06-15', 138),
(139, 111, 1, '14:46:27', '14:50:12', '00:04', '2021-06-15', 139),
(140, 110, 1, '14:52:20', '14:58:11', '00:06', '2021-06-15', 140),
(141, 113, 1, '13:20:09', '13:31:49', '00:11', '2021-06-17', 141),
(142, 114, 2, '21:52:46', '21:57:43', '00:05', '2021-06-21', 142),
(143, 115, 2, '20:01:54', '20:04:40', '00:03', '2021-06-22', 143),
(144, 116, 2, '20:06:12', '20:09:03', '00:03', '2021-06-22', 144),
(145, 117, 2, '04:42:18', '04:45:33', '00:03', '2021-06-23', 145),
(146, 118, 2, '04:50:31', '04:53:11', '00:03', '2021-06-23', 146),
(147, 118, 2, '04:53:38', '04:56:23', '00:03', '2021-06-23', 147),
(148, 119, 1, '08:58:41', '09:04:09', '00:06', '2021-06-30', 148),
(149, 119, 1, '09:04:38', '09:07:13', '00:03', '2021-06-30', 149),
(150, 120, 1, '10:39:33', '10:42:40', '00:03', '2021-07-21', 150),
(151, 121, 1, '15:39:32', '15:42:12', '00:03', '2021-07-21', 151),
(152, 121, 1, '15:42:36', '15:46:08', '00:04', '2021-07-21', 152),
(153, 122, 1, '15:50:12', '15:52:58', '00:02', '2021-07-21', 153),
(154, 122, 1, '15:55:05', '15:58:47', '00:03', '2021-07-21', 154),
(155, 123, 1, '15:59:40', '16:05:48', '00:06', '2021-07-21', 155),
(156, 123, 1, '16:06:22', '16:09:08', '00:03', '2021-07-21', 156),
(157, 123, 1, '16:09:36', '16:12:06', '00:03', '2021-07-21', 157),
(158, 124, 2, '16:23:40', '16:28:41', '00:05', '2021-07-21', 158),
(159, 124, 2, '16:29:03', '16:31:29', '00:02', '2021-07-21', 159),
(160, 125, 1, '08:26:13', '08:34:35', '00:08', '2021-07-22', 160),
(161, 125, 1, '08:35:07', '08:55:30', '00:20', '2021-07-22', 161),
(162, 126, 1, '08:58:00', '09:24:25', '00:26', '2021-07-22', 162),
(163, 126, 1, '09:25:07', '09:32:54', '00:07', '2021-07-22', 163),
(164, 128, 1, '11:14:15', '13:47:59', '02:33', '2021-08-03', 164),
(165, 129, 1, '08:38:23', '10:16:53', '01:38', '2021-08-04', 165),
(166, 129, 1, '11:33:30', '12:43:44', '01:10', '2021-08-04', 166),
(167, 131, 1, '11:33:52', '11:51:56', '00:18', '2021-08-06', 168),
(168, 131, 1, '12:15:34', '12:23:56', '00:08', '2021-08-06', 169),
(169, 131, 1, '12:27:24', '12:31:17', '00:04', '2021-08-06', 170),
(170, 132, 2, '00:40:51', '00:43:28', '00:03', '2021-08-09', 171),
(171, 132, 2, '00:44:11', '00:47:23', '00:03', '2021-08-09', 172),
(172, 134, 2, '20:35:07', '20:38:21', '00:03', '2021-08-10', 173),
(173, 135, 2, '21:02:01', '21:06:05', '00:04', '2021-08-10', 174),
(174, 135, 2, '21:06:44', '21:09:45', '00:03', '2021-08-10', 175),
(175, 139, 1, '09:05:12', '09:43:42', '00:38', '2021-08-18', 177),
(176, 139, 1, '12:39:00', '13:10:29', '00:31', '2021-08-18', 179),
(177, 139, 1, '13:11:08', '13:23:43', '00:12', '2021-08-18', 180),
(178, 141, 1, '11:09:13', '11:25:40', '00:16', '2021-08-19', 181),
(179, 142, 1, '09:58:18', '10:04:13', '00:06', '2021-08-20', 182),
(180, 142, 1, '10:13:38', '10:23:38', '00:10', '2021-08-20', 183),
(181, 143, 1, '13:58:46', '14:04:09', '00:06', '2021-08-20', 184),
(182, 143, 1, '14:04:43', '14:06:48', '00:02', '2021-08-20', 185),
(183, 143, 1, '14:07:19', '14:09:23', '00:02', '2021-08-20', 186),
(184, 144, 1, '15:56:03', '15:58:21', '00:02', '2021-08-20', 187),
(185, 147, 1, '15:06:22', '15:09:40', '00:03', '2021-08-24', 188),
(186, 148, 1, '15:14:27', '15:22:39', '00:08', '2021-08-24', 189),
(187, 148, 1, '15:25:43', '15:39:55', '00:14', '2021-08-24', 190),
(188, 149, 1, '16:13:46', '16:41:30', '00:28', '2021-08-25', 191),
(189, 149, 1, '16:13:46', '16:41:32', '00:28', '2021-08-25', 192),
(190, 149, 1, '16:13:46', '16:41:34', '00:28', '2021-08-25', 193),
(191, 149, 1, '16:13:46', '16:41:35', '00:28', '2021-08-25', 194),
(192, 149, 1, '16:13:46', '16:41:36', '00:28', '2021-08-25', 195),
(193, 149, 1, '16:13:46', '16:41:36', '00:28', '2021-08-25', 196),
(194, 149, 1, '16:13:46', '16:41:37', '00:28', '2021-08-25', 197),
(195, 149, 1, '16:13:46', '16:41:48', '00:28', '2021-08-25', 198),
(196, 149, 1, '16:13:46', '16:41:49', '00:28', '2021-08-25', 199),
(197, 149, 1, '16:13:46', '16:41:49', '00:28', '2021-08-25', 200),
(198, 149, 1, '16:13:46', '16:41:50', '00:28', '2021-08-25', 201),
(199, 149, 1, '16:13:46', '16:41:50', '00:28', '2021-08-25', 202),
(200, 149, 1, '16:13:46', '16:41:51', '00:28', '2021-08-25', 203),
(201, 149, 1, '16:13:46', '16:41:52', '00:28', '2021-08-25', 204),
(202, 149, 1, '16:13:46', '16:41:52', '00:28', '2021-08-25', 205),
(203, 149, 1, '16:13:46', '16:41:56', '00:28', '2021-08-25', 206),
(204, 149, 1, '16:13:46', '16:41:57', '00:28', '2021-08-25', 207),
(205, 149, 1, '16:13:46', '16:41:58', '00:28', '2021-08-25', 208),
(206, 149, 1, '16:13:46', '16:41:58', '00:28', '2021-08-25', 209),
(207, 149, 1, '16:13:46', '16:41:59', '00:28', '2021-08-25', 210),
(208, 149, 1, '16:13:46', '16:42:00', '00:29', '2021-08-25', 211),
(209, 149, 1, '16:13:46', '16:42:04', '00:29', '2021-08-25', 212),
(210, 149, 1, '16:13:46', '16:42:04', '00:29', '2021-08-25', 213),
(211, 149, 1, '16:13:46', '16:42:05', '00:29', '2021-08-25', 214),
(212, 149, 1, '16:13:46', '16:42:05', '00:29', '2021-08-25', 215),
(213, 149, 1, '16:13:46', '16:42:06', '00:29', '2021-08-25', 216),
(214, 149, 1, '16:13:46', '16:42:07', '00:29', '2021-08-25', 217),
(215, 149, 1, '16:13:46', '16:42:07', '00:29', '2021-08-25', 218),
(216, 149, 1, '16:13:46', '16:42:08', '00:29', '2021-08-25', 219),
(217, 149, 1, '16:13:46', '16:42:08', '00:29', '2021-08-25', 220),
(218, 149, 1, '16:13:46', '16:42:09', '00:29', '2021-08-25', 221),
(219, 149, 1, '16:13:46', '16:42:10', '00:29', '2021-08-25', 222),
(220, 149, 1, '16:13:46', '16:42:10', '00:29', '2021-08-25', 223),
(221, 149, 1, '16:13:46', '16:42:23', '00:29', '2021-08-25', 224),
(222, 149, 1, '16:13:46', '16:42:24', '00:29', '2021-08-25', 225),
(223, 149, 1, '16:13:46', '16:42:25', '00:29', '2021-08-25', 226),
(224, 149, 1, '16:13:46', '16:42:25', '00:29', '2021-08-25', 227),
(225, 149, 1, '16:13:46', '16:42:26', '00:29', '2021-08-25', 228),
(226, 150, 2, '17:14:16', '17:24:23', '00:10', '2021-08-25', 229),
(227, 152, 2, '02:13:42', '02:19:40', '00:06', '2021-08-25', 230),
(228, 152, 2, '02:20:16', '02:23:39', '00:03', '2021-08-25', 231),
(229, 154, 1, '08:43:31', '08:57:41', '00:14', '2021-08-26', 232),
(230, 154, 1, '08:59:00', '09:07:57', '00:08', '2021-08-26', 233),
(231, 155, 1, '09:09:33', '09:15:13', '00:06', '2021-08-26', 234),
(232, 156, 1, '09:16:08', '09:24:40', '00:08', '2021-08-26', 235),
(233, 156, 1, '09:26:48', '09:31:49', '00:05', '2021-08-26', 236),
(234, 156, 1, '09:26:48', '09:31:53', '00:05', '2021-08-26', 237),
(235, 156, 1, '09:26:48', '09:31:56', '00:05', '2021-08-26', 238),
(236, 156, 1, '09:26:48', '09:31:56', '00:05', '2021-08-26', 239),
(237, 156, 1, '09:26:48', '09:31:57', '00:05', '2021-08-26', 240),
(238, 156, 1, '09:26:48', '09:31:58', '00:05', '2021-08-26', 241),
(239, 156, 1, '09:26:48', '09:31:58', '00:05', '2021-08-26', 242),
(240, 156, 1, '09:26:48', '09:33:27', '00:07', '2021-08-26', 243),
(241, 156, 1, '09:26:48', '09:33:29', '00:07', '2021-08-26', 244),
(242, 156, 1, '09:26:48', '09:33:30', '00:07', '2021-08-26', 245),
(243, 156, 1, '09:26:48', '09:33:31', '00:07', '2021-08-26', 246),
(244, 156, 1, '09:26:48', '09:35:10', '00:09', '2021-08-26', 247),
(245, 156, 1, '09:26:48', '09:35:11', '00:09', '2021-08-26', 248),
(246, 156, 1, '09:26:48', '09:35:14', '00:09', '2021-08-26', 249),
(247, 156, 1, '09:26:48', '09:35:14', '00:09', '2021-08-26', 250),
(248, 156, 1, '09:26:48', '09:35:15', '00:09', '2021-08-26', 251),
(249, 156, 1, '09:26:48', '09:35:16', '00:09', '2021-08-26', 252),
(250, 156, 1, '09:26:48', '09:35:16', '00:09', '2021-08-26', 253),
(251, 156, 1, '09:26:48', '09:35:17', '00:09', '2021-08-26', 254),
(252, 156, 1, '09:26:48', '09:35:18', '00:09', '2021-08-26', 255),
(253, 156, 1, '09:26:48', '09:35:18', '00:09', '2021-08-26', 256),
(254, 156, 1, '09:26:48', '09:35:19', '00:09', '2021-08-26', 257),
(255, 156, 1, '09:26:48', '09:35:20', '00:09', '2021-08-26', 258),
(256, 156, 1, '09:26:48', '09:35:20', '00:09', '2021-08-26', 259),
(257, 156, 1, '09:26:48', '09:35:21', '00:09', '2021-08-26', 260),
(258, 156, 1, '09:26:48', '09:35:22', '00:09', '2021-08-26', 261),
(259, 157, 1, '14:23:47', '14:35:01', '00:12', '2021-08-26', 262),
(260, 158, 2, '06:16:49', '06:22:10', '00:06', '2021-08-26', 263),
(261, 158, 2, '06:23:24', '06:28:29', '00:05', '2021-08-26', 264),
(262, 159, 1, '08:27:31', '08:31:23', '00:04', '2021-08-27', 265),
(263, 159, 1, '08:31:45', '08:33:44', '00:02', '2021-08-27', 266),
(264, 160, 1, '09:22:40', '09:25:22', '00:03', '2021-08-27', 267),
(265, 160, 1, '09:25:42', '09:54:35', '00:29', '2021-08-27', 268),
(266, 160, 1, '09:55:00', '09:58:18', '00:03', '2021-08-27', 269),
(267, 161, 1, '12:23:02', '12:28:10', '00:05', '2021-08-27', 270),
(268, 162, 1, '12:34:12', '12:37:21', '00:03', '2021-08-27', 271),
(269, 162, 1, '12:37:50', '12:41:05', '00:04', '2021-08-27', 272),
(270, 162, 1, '12:42:43', '12:46:46', '00:04', '2021-08-27', 273),
(271, 163, 1, '08:36:13', '08:49:05', '00:13', '2021-08-30', 274),
(272, 165, 2, '18:27:43', '18:29:53', '00:02', '2021-08-30', 275),
(273, 166, 2, '18:32:29', '18:36:00', '00:04', '2021-08-30', 276),
(274, 167, 1, '12:37:43', '13:58:17', '01:21', '2021-08-31', 277),
(275, 167, 1, '13:59:52', '14:29:34', '00:30', '2021-08-31', 278),
(276, 167, 1, '14:30:35', '15:57:30', '01:27', '2021-08-31', 279),
(277, 168, 1, '07:35:39', '07:43:07', '00:08', '2021-09-02', 280),
(278, 168, 1, '07:44:03', '07:56:19', '00:12', '2021-09-02', 281),
(279, 169, 1, '14:05:54', '14:14:48', '00:09', '2021-09-21', 282),
(280, 169, 1, '14:16:08', '15:21:09', '01:05', '2021-09-21', 283),
(281, 170, 1, '15:23:55', '15:51:07', '00:28', '2021-09-21', 284),
(282, 170, 1, '15:53:12', '16:03:18', '00:10', '2021-09-21', 285),
(283, 170, 1, '16:04:08', '16:12:54', '00:08', '2021-09-21', 286),
(284, 171, 2, '16:15:27', '16:22:16', '00:07', '2021-09-21', 287),
(285, 171, 2, '16:23:09', '16:28:17', '00:05', '2021-09-21', 288),
(286, 172, 1, '13:41:34', '14:21:14', '00:40', '2021-09-22', 289),
(287, 172, 1, '14:21:40', '14:31:02', '00:10', '2021-09-22', 290),
(288, 173, 1, '14:34:36', '15:12:52', '00:38', '2021-09-22', 291),
(289, 173, 1, '15:13:05', '15:14:59', '00:01', '2021-09-22', 292),
(290, 174, 1, '14:19:58', '14:25:30', '00:06', '2021-09-23', 293),
(291, 180, 1, '10:57:51', '11:11:28', '00:14', '2021-09-29', 294),
(292, 180, 1, '11:37:33', '12:04:10', '00:27', '2021-09-29', 295),
(293, 181, 1, '12:21:36', '12:44:10', '00:23', '2021-10-12', 296),
(294, 186, 1, '07:15:02', '07:18:54', '00:03', '2022-05-23', 297),
(295, 187, 1, '13:27:15', '13:30:44', '00:03', '2022-05-23', 298),
(296, 190, 2, '16:24:18', '16:25:25', '00:01', '2022-05-24', 299),
(297, 191, 1, '09:24:01', '09:25:08', '00:01', '2022-05-25', 300),
(298, 192, 1, '11:29:44', '11:34:40', '00:05', '2022-05-25', 301),
(299, 194, 1, '13:47:20', '13:49:58', '00:02', '2022-05-25', 302),
(300, 196, 1, '09:41:43', '09:44:11', '00:03', '2022-05-26', 303),
(301, 197, 1, '09:52:47', '09:54:25', '00:02', '2022-05-26', 304),
(302, 198, 1, '10:18:33', '10:21:03', '00:03', '2022-05-26', 305),
(303, 199, 1, '13:30:17', '13:36:27', '00:06', '2022-05-26', 306),
(304, 200, 1, '08:40:06', '09:25:11', '00:45', '2022-05-30', 307),
(305, 204, 1, '10:27:49', '10:40:01', '00:13', '2022-05-30', 308),
(306, 205, 1, '11:39:46', '11:54:24', '00:15', '2022-05-30', 309),
(307, 206, 1, '13:57:54', '13:59:01', '00:02', '2022-05-30', 310),
(308, 207, 1, '14:10:24', '14:13:31', '00:03', '2022-05-30', 311),
(309, 208, 1, '14:20:21', '14:27:14', '00:07', '2022-05-30', 312),
(310, 211, 1, '10:56:03', '11:00:21', '00:04', '2022-06-14', 313),
(311, 213, 1, '11:00:03', '11:26:30', '00:26', '2024-01-10', 314),
(312, 213, 1, '11:00:03', '11:26:32', '00:26', '2024-01-10', 315),
(313, 213, 1, '11:00:03', '11:26:36', '00:26', '2024-01-10', 316),
(314, 213, 1, '11:00:03', '11:26:36', '00:26', '2024-01-10', 317),
(315, 213, 1, '11:00:03', '11:26:37', '00:26', '2024-01-10', 318),
(316, 213, 1, '11:00:03', '11:26:37', '00:26', '2024-01-10', 319),
(317, 213, 1, '11:00:03', '11:26:37', '00:26', '2024-01-10', 320),
(318, 213, 1, '11:00:03', '11:26:37', '00:26', '2024-01-10', 321),
(319, 213, 1, '11:00:03', '11:26:52', '00:26', '2024-01-10', 322),
(320, 213, 1, '11:00:03', '11:26:53', '00:26', '2024-01-10', 323),
(321, 213, 1, '11:00:03', '11:27:35', '00:27', '2024-01-10', 324),
(322, 213, 1, '11:00:03', '11:27:36', '00:27', '2024-01-10', 325),
(323, 213, 1, '11:00:03', '11:27:37', '00:27', '2024-01-10', 326),
(324, 213, 1, '11:00:03', '11:27:37', '00:27', '2024-01-10', 327),
(325, 213, 1, '11:00:03', '11:27:37', '00:27', '2024-01-10', 328),
(326, 213, 1, '11:00:03', '11:27:38', '00:27', '2024-01-10', 329),
(327, 213, 1, '11:00:03', '11:27:38', '00:27', '2024-01-10', 330),
(328, 213, 1, '11:00:03', '11:27:38', '00:27', '2024-01-10', 331),
(329, 213, 1, '11:00:03', '11:27:39', '00:27', '2024-01-10', 332),
(330, 213, 1, '11:00:03', '11:27:39', '00:27', '2024-01-10', 333),
(331, 213, 1, '11:00:03', '11:27:39', '00:27', '2024-01-10', 334),
(332, 213, 1, '11:00:03', '11:28:00', '00:28', '2024-01-10', 335),
(333, 213, 1, '11:00:03', '11:30:07', '00:30', '2024-01-10', 336),
(334, 213, 1, '11:00:03', '11:30:08', '00:30', '2024-01-10', 337),
(335, 213, 1, '11:00:03', '11:30:11', '00:30', '2024-01-10', 338),
(336, 213, 1, '11:00:03', '11:30:19', '00:30', '2024-01-10', 339),
(337, 213, 1, '11:00:03', '11:30:21', '00:30', '2024-01-10', 340),
(338, 213, 1, '11:00:03', '11:30:21', '00:30', '2024-01-10', 341),
(339, 213, 1, '11:00:03', '11:30:22', '00:30', '2024-01-10', 342),
(340, 213, 1, '11:00:03', '11:30:22', '00:30', '2024-01-10', 343),
(341, 213, 1, '11:00:03', '11:30:22', '00:30', '2024-01-10', 344),
(342, 213, 1, '11:00:03', '11:30:23', '00:30', '2024-01-10', 345),
(343, 213, 1, '11:00:03', '11:30:23', '00:30', '2024-01-10', 346),
(344, 213, 1, '11:00:03', '11:30:23', '00:30', '2024-01-10', 347),
(345, 213, 1, '11:00:03', '11:30:24', '00:30', '2024-01-10', 348),
(346, 213, 1, '11:00:03', '11:30:24', '00:30', '2024-01-10', 349),
(347, 213, 1, '11:00:03', '11:30:25', '00:30', '2024-01-10', 350),
(348, 213, 1, '11:00:03', '11:30:25', '00:30', '2024-01-10', 351),
(349, 213, 1, '11:00:03', '11:30:25', '00:30', '2024-01-10', 352),
(350, 213, 1, '11:00:03', '11:30:26', '00:30', '2024-01-10', 353),
(351, 213, 1, '11:00:03', '11:30:26', '00:30', '2024-01-10', 354),
(352, 213, 1, '11:00:03', '11:30:26', '00:30', '2024-01-10', 355),
(353, 213, 1, '11:00:03', '11:30:27', '00:30', '2024-01-10', 356),
(354, 213, 1, '11:00:03', '11:30:27', '00:30', '2024-01-10', 357),
(355, 213, 1, '11:00:03', '11:30:27', '00:30', '2024-01-10', 358),
(356, 213, 1, '11:00:03', '11:30:33', '00:30', '2024-01-10', 359),
(357, 213, 1, '11:00:03', '11:30:33', '00:30', '2024-01-10', 360),
(358, 213, 1, '11:00:03', '11:30:33', '00:30', '2024-01-10', 361),
(359, 213, 1, '11:00:03', '11:30:34', '00:30', '2024-01-10', 362),
(360, 213, 1, '11:00:03', '11:30:34', '00:30', '2024-01-10', 363),
(361, 213, 1, '11:00:03', '11:30:34', '00:30', '2024-01-10', 364),
(362, 213, 1, '11:00:03', '11:30:35', '00:30', '2024-01-10', 365),
(363, 213, 1, '11:00:03', '11:30:35', '00:30', '2024-01-10', 366),
(364, 213, 1, '11:00:03', '11:30:35', '00:30', '2024-01-10', 367),
(365, 213, 1, '11:00:03', '11:30:58', '00:30', '2024-01-10', 368),
(366, 213, 1, '11:00:03', '11:30:58', '00:30', '2024-01-10', 369),
(367, 213, 1, '11:00:03', '11:30:59', '00:30', '2024-01-10', 370),
(368, 213, 1, '11:00:03', '11:30:59', '00:30', '2024-01-10', 371),
(369, 213, 1, '11:00:03', '11:30:59', '00:30', '2024-01-10', 372),
(370, 213, 1, '11:00:03', '11:31:00', '00:31', '2024-01-10', 373),
(371, 213, 1, '11:00:03', '11:31:00', '00:31', '2024-01-10', 374),
(372, 213, 1, '11:00:03', '11:31:00', '00:31', '2024-01-10', 375),
(373, 213, 1, '11:00:03', '11:31:00', '00:31', '2024-01-10', 376),
(374, 213, 1, '11:00:03', '11:31:04', '00:31', '2024-01-10', 377),
(375, 213, 1, '11:00:03', '11:31:05', '00:31', '2024-01-10', 378),
(376, 213, 1, '11:00:03', '11:31:05', '00:31', '2024-01-10', 379),
(377, 213, 1, '11:00:03', '11:31:05', '00:31', '2024-01-10', 380),
(378, 213, 1, '11:00:03', '11:31:06', '00:31', '2024-01-10', 381),
(379, 213, 1, '11:00:03', '11:31:52', '00:31', '2024-01-10', 382),
(380, 213, 1, '11:00:03', '11:31:53', '00:31', '2024-01-10', 383),
(381, 213, 1, '11:00:03', '11:31:54', '00:31', '2024-01-10', 384),
(382, 213, 1, '11:00:03', '11:31:54', '00:31', '2024-01-10', 385),
(383, 213, 1, '11:00:03', '11:31:55', '00:31', '2024-01-10', 386),
(384, 213, 1, '11:00:03', '11:31:55', '00:31', '2024-01-10', 387),
(385, 213, 1, '11:00:03', '11:31:55', '00:31', '2024-01-10', 388),
(386, 213, 1, '11:00:03', '11:31:56', '00:31', '2024-01-10', 389),
(387, 213, 1, '11:00:03', '11:31:56', '00:31', '2024-01-10', 390),
(388, 213, 1, '11:00:03', '11:31:56', '00:31', '2024-01-10', 391),
(389, 213, 1, '11:00:03', '11:31:56', '00:31', '2024-01-10', 392),
(390, 213, 1, '11:00:03', '11:31:57', '00:31', '2024-01-10', 393),
(391, 213, 1, '11:00:03', '11:31:57', '00:31', '2024-01-10', 394),
(392, 213, 1, '11:00:03', '11:31:57', '00:31', '2024-01-10', 395),
(393, 213, 1, '11:00:03', '11:31:58', '00:31', '2024-01-10', 396),
(394, 213, 1, '11:00:03', '11:31:58', '00:31', '2024-01-10', 397),
(395, 213, 1, '11:00:03', '11:31:58', '00:31', '2024-01-10', 398),
(396, 213, 1, '11:00:03', '11:31:59', '00:31', '2024-01-10', 399),
(397, 213, 1, '11:00:03', '11:31:59', '00:31', '2024-01-10', 400),
(398, 213, 1, '11:00:03', '11:31:59', '00:31', '2024-01-10', 401),
(399, 213, 1, '11:00:03', '11:32:00', '00:32', '2024-01-10', 402),
(400, 213, 1, '11:00:03', '11:32:00', '00:32', '2024-01-10', 403),
(401, 213, 1, '11:00:03', '11:32:00', '00:32', '2024-01-10', 404),
(402, 213, 1, '11:00:03', '11:32:01', '00:32', '2024-01-10', 405),
(403, 213, 1, '11:00:03', '11:32:01', '00:32', '2024-01-10', 406),
(404, 213, 1, '11:00:03', '11:32:01', '00:32', '2024-01-10', 407),
(405, 213, 1, '11:00:03', '11:32:01', '00:32', '2024-01-10', 408),
(406, 213, 1, '11:00:03', '11:32:02', '00:32', '2024-01-10', 409),
(407, 213, 1, '11:00:03', '11:32:02', '00:32', '2024-01-10', 410),
(408, 213, 1, '11:00:03', '11:32:02', '00:32', '2024-01-10', 411),
(420, 215, 1, '11:48:31', '11:55:12', '00:07', '2024-01-10', 423),
(421, 151, 1, '12:27:07', '12:29:45', '00:02', '2024-01-17', 424),
(422, 151, 1, '14:27:07', '14:33:43', '00:06', '2024-01-17', 425),
(423, 151, 1, '14:41:34', '14:42:50', '00:01', '2024-01-17', 426),
(424, 151, 1, '14:41:34', '14:46:55', '00:05', '2024-01-17', 427),
(425, 151, 1, '14:58:20', '15:00:50', '00:02', '2024-01-17', 428),
(426, 151, 1, '14:58:20', '08:51:16', '17:53', '2024-01-17', 429),
(427, 214, 1, '14:42:51', '14:44:33', '00:02', '2024-02-12', 430),
(428, 214, 1, '14:42:51', '08:40:29', '17:58', '2024-02-12', 431);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tiempo_sorteo`
--

CREATE TABLE `tiempo_sorteo` (
  `id_tiempo_sorteo` int(11) NOT NULL,
  `id_empleado` int(10) UNSIGNED NOT NULL,
  `id_registro_rbp` int(10) UNSIGNED NOT NULL,
  `tarjeta_sorteo` int(11) NOT NULL,
  `mog_id_mog` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `hora_inicio` varchar(20) NOT NULL,
  `hora_fin` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `work_center_maquina`
--

CREATE TABLE `work_center_maquina` (
  `id_work_center_maquina` int(10) UNSIGNED NOT NULL,
  `empleado_supervisor_id_empleado_supervisor` int(10) UNSIGNED NOT NULL,
  `nombre_work_center` varchar(20) DEFAULT NULL,
  `codigo_maquina` varchar(20) DEFAULT NULL,
  `procesos_idprocesos` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `work_center_maquina`
--

INSERT INTO `work_center_maquina` (`id_work_center_maquina`, `empleado_supervisor_id_empleado_supervisor`, `nombre_work_center`, `codigo_maquina`, `procesos_idprocesos`) VALUES
(1, 10, 'SLITER TC01', 'TC01', 6),
(2, 11, 'COILING TC11', 'TC11', 7),
(3, 11, 'COILING TC12', 'TC12', 7),
(4, 11, 'COILING TC13', 'TC13', 7),
(5, 11, 'COILING TC14', 'TC14', 7),
(6, 56, 'PRENSA BUSH', 'TB01', 10),
(7, 8, 'PRENSA BUSH', 'TB02B', 10),
(8, 6, 'BUSH FORMING', 'TB02F', 12),
(9, 57, 'PRENSA BUSH', 'TB05', 10),
(10, 58, 'PRENSA BUSH', 'TB06', 10),
(11, 19, 'BUSH COINING', 'TB31', 13),
(12, 60, 'BUSH COINING', 'TB32', 13),
(13, 25, 'BUSH GRINDING', 'TB51', 14),
(14, 54, 'BUSH CHAMFERING', 'TB71', 15),
(15, 62, 'BUSH-ASSY', 'TB91', 11),
(16, 59, 'PRENSA BUSH', 'TB03B', 10),
(17, 61, 'BUSH FORMING', 'TB03F', 12),
(18, 5, 'PRENSA', 'TP41', 2),
(19, 5, 'PRENSA', 'TP01', 2),
(20, 5, 'PRENSA', 'TP02', 2),
(21, 5, 'PRENSA', 'TP03', 2),
(22, 5, 'PRENSA', 'TP04', 2),
(23, 5, 'PRENSA', 'TP05', 2),
(24, 5, 'PRENSA', 'TP06', 2),
(25, 5, 'PRENSA', 'TP07', 2),
(26, 5, 'PRENSA', 'TP08', 2),
(27, 5, 'PRENSA', 'TP09', 2),
(28, 5, 'PRENSA', 'TP10', 2),
(29, 5, 'PRENSA', 'TP11', 2),
(30, 2, 'MAQUINADO', 'TH01', 1),
(31, 2, 'MAQUINADO', 'TH02', 1),
(32, 2, 'MAQUINADO', 'TH03', 1),
(33, 2, 'MAQUINADO', 'TH04', 1),
(34, 2, 'MAQUINADO', 'TH05', 1),
(35, 2, 'MAQUINADO', 'TH06', 1),
(36, 2, 'MAQUINADO', 'TH07', 1),
(37, 2, 'MAQUINADO', 'TH08', 1),
(38, 2, 'MAQUINADO', 'TH09', 1),
(39, 2, 'MAQUINADO', 'TH10', 1),
(40, 2, 'MAQUINADO', 'TH11', 1),
(41, 2, 'MAQUINADO', 'TH12', 1),
(42, 2, 'MAQUINADO', 'TH13', 1),
(43, 2, 'MAQUINADO', 'TH14', 1),
(44, 2, 'MAQUINADO', 'TH16', 1),
(45, 2, 'MAQUINADO', 'TH17', 1),
(46, 2, 'MAQUINADO', 'TH41', 1),
(47, 4, 'PLATINADO TG01', 'TG01', 3),
(48, 4, 'PLATINADO TGP02', 'TG02', 3),
(49, 4, 'PLATINADO TI26', 'TG03', 16),
(50, 4, 'PLATINADO', 'TGP01', 3),
(51, 4, 'PLATINADO TGP02', 'TGP02', 3),
(52, 4, 'PLATINADO TGP03', 'TGP03', 3),
(53, 65, 'Mesa 08', 'TI28', 9),
(55, 6, 'EMPAQUE TIIJ-1', 'TIIJ-1', 4),
(56, 6, 'EMPAQUE TIIJ-2', 'TIIJ-2', 4),
(57, 6, 'EMPAQUE TIIJ-3', 'TIIJ-3', 4),
(58, 6, 'EMPAQUE TIIJ-4', 'TIIJ4', 4),
(59, 6, 'EMPAQUE TIPM-A', 'TIPM-A', 4),
(60, 6, 'EMPAQUE TIPM-A2', 'TIPM-A2', 4),
(61, 6, 'EMPAQUE TIPM-A3', 'TIPM-A3', 4),
(62, 6, 'EMPAQUE TIPM-A4', 'TIPM-A4', 4),
(63, 6, 'EMPAQUE TIPM-A5', 'TIPM-A5', 4),
(64, 6, 'EMPAQUE TIPM-A6', 'TIPM-A6', 4),
(65, 6, 'EMPAQUE TIPM-A7', 'TIPM-A7', 4),
(66, 6, 'EMPAQUE TIPM-A8', 'TIPM-A8', 4),
(67, 1, 'POLÍMERO #1', 'TIGP2', 1),
(68, 13, 'GRADING #1', 'TI11', 8),
(69, 13, 'GRADING #2', 'TI12', 4),
(70, 13, 'GRADING #3', 'TI13', 8),
(71, 13, 'GRADING #4', 'TI14', 4),
(72, 13, 'GRADING #5', 'TI15', 8),
(76, 65, 'Mesa 09', 'TI29', 9),
(77, 65, 'Mesa 10', 'TI30', 9),
(78, 63, 'BUSH-SEAL RING', 'TB92', 17),
(79, 2, 'MAQUINADO', 'TH42', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `catalogo_no_parte`
--
ALTER TABLE `catalogo_no_parte`
  ADD PRIMARY KEY (`id_no_parte`),
  ADD UNIQUE KEY `no_parte` (`no_parte`);

--
-- Indices de la tabla `categoria_rechazo`
--
ALTER TABLE `categoria_rechazo`
  ADD PRIMARY KEY (`id_categoria_rechazo`);

--
-- Indices de la tabla `causas_paro`
--
ALTER TABLE `causas_paro`
  ADD PRIMARY KEY (`idcausas_paro`),
  ADD KEY `fk_proceso_causa` (`procesos_idproceso`);

--
-- Indices de la tabla `cerradoordenes`
--
ALTER TABLE `cerradoordenes`
  ADD PRIMARY KEY (`id_cerrado`),
  ADD KEY `id_registro_rbp` (`id_registro_rbp`),
  ADD KEY `id_empleado` (`id_empleado`),
  ADD KEY `id_mog` (`id_mog`);

--
-- Indices de la tabla `coiling_material_a`
--
ALTER TABLE `coiling_material_a`
  ADD PRIMARY KEY (`id_coiling_mat`),
  ADD KEY `dasprodcoi_iddasprodcoi` (`dasprodcoi_iddasprodcoi`);

--
-- Indices de la tabla `coiling_material_s`
--
ALTER TABLE `coiling_material_s`
  ADD KEY `dasprodcoi_iddasprodcoi` (`dasprodcoi_iddasprodcoi`);

--
-- Indices de la tabla `corriendoactualmente`
--
ALTER TABLE `corriendoactualmente`
  ADD PRIMARY KEY (`id_corriendo`);

--
-- Indices de la tabla `das`
--
ALTER TABLE `das`
  ADD PRIMARY KEY (`id_das`),
  ADD KEY `empleado_id_empleado` (`empleado_id_empleado`),
  ADD KEY `id_keeper` (`id_keeper`),
  ADD KEY `id_inspector` (`id_inspector`);

--
-- Indices de la tabla `das_coiling_totales`
--
ALTER TABLE `das_coiling_totales`
  ADD PRIMARY KEY (`id_dascolitot`),
  ADD KEY `das_iddas` (`das_iddas`);

--
-- Indices de la tabla `das_produccion`
--
ALTER TABLE `das_produccion`
  ADD PRIMARY KEY (`id_dasproduccion`),
  ADD KEY `fk_das` (`das_ida_das`),
  ADD KEY `fk_mog` (`mog_idmog`);

--
-- Indices de la tabla `das_produ_empamesas`
--
ALTER TABLE `das_produ_empamesas`
  ADD PRIMARY KEY (`id_proEmpMes`),
  ADD KEY `mog_idmog` (`mog_idmog`),
  ADD KEY `das_iddas` (`das_iddas`);

--
-- Indices de la tabla `das_prod_bgprensa`
--
ALTER TABLE `das_prod_bgprensa`
  ADD PRIMARY KEY (`id_dasprodbgp`),
  ADD KEY `mog_idmog` (`mog_idmog`),
  ADD KEY `das_id_das` (`das_id_das`);

--
-- Indices de la tabla `das_prod_bgproceso`
--
ALTER TABLE `das_prod_bgproceso`
  ADD PRIMARY KEY (`id_dasprodbgproc`),
  ADD KEY `das_iddas` (`das_iddas`),
  ADD KEY `mog_idmog` (`mog_idmog`);

--
-- Indices de la tabla `das_prod_coi`
--
ALTER TABLE `das_prod_coi`
  ADD PRIMARY KEY (`id_dasprodcoi`),
  ADD KEY `das_idas` (`das_idas`),
  ADD KEY `ordencoil_idordencoil` (`ordencoil_idordencoil`);

--
-- Indices de la tabla `das_prod_empmaq`
--
ALTER TABLE `das_prod_empmaq`
  ADD PRIMARY KEY (`das_prod_emp_id`),
  ADD KEY `das_iddas` (`das_iddas`),
  ADD KEY `mog_idmog` (`mog_idmog`);

--
-- Indices de la tabla `das_prod_grading`
--
ALTER TABLE `das_prod_grading`
  ADD KEY `das_idas` (`das_idas`),
  ADD KEY `Índice 1` (`id_prodGrad`),
  ADD KEY `Índice 2` (`mog_idmog`);

--
-- Indices de la tabla `das_prod_plat`
--
ALTER TABLE `das_prod_plat`
  ADD PRIMARY KEY (`das_prod_plat_id`),
  ADD KEY `mog_idmog` (`mog_idmog`),
  ADD KEY `das_id_das` (`das_id_das`),
  ADD KEY `piezasProcesadas_id_piezadprocesada` (`piezasProcesadas_id_piezadprocesada`);

--
-- Indices de la tabla `das_prod_pren`
--
ALTER TABLE `das_prod_pren`
  ADD PRIMARY KEY (`id_daspropren`),
  ADD KEY `mog_idmog` (`mog_idmog`),
  ADD KEY `das_iddas` (`das_iddas`);

--
-- Indices de la tabla `das_registrer`
--
ALTER TABLE `das_registrer`
  ADD PRIMARY KEY (`iddas_registro`),
  ADD KEY `das_id_das` (`das_id_das`),
  ADD KEY `empleado_id_empleado` (`empleado_id_empleado`);

--
-- Indices de la tabla `das_reg_coiling`
--
ALTER TABLE `das_reg_coiling`
  ADD KEY `empleado_idempleado` (`empleado_idempleado`),
  ADD KEY `das_iddas` (`das_iddas`);

--
-- Indices de la tabla `das_reg_prensa`
--
ALTER TABLE `das_reg_prensa`
  ADD PRIMARY KEY (`id_regprensa`),
  ADD KEY `das_iddas` (`das_iddas`),
  ADD KEY `empleado_idempleado` (`empleado_idempleado`);

--
-- Indices de la tabla `das_reg_slitter`
--
ALTER TABLE `das_reg_slitter`
  ADD PRIMARY KEY (`id_das_reg_Slitter`),
  ADD KEY `empleado_idempleado` (`empleado_idempleado`),
  ADD KEY `das_iddas` (`das_iddas`);

--
-- Indices de la tabla `das_slitter`
--
ALTER TABLE `das_slitter`
  ADD PRIMARY KEY (`id_das_slitter`),
  ADD KEY `das_id_das` (`das_id_das`),
  ADD KEY `ordenSlitter_idordenS` (`ordenSlitter_idordenS`);

--
-- Indices de la tabla `defecto1`
--
ALTER TABLE `defecto1`
  ADD PRIMARY KEY (`id_defecto`),
  ADD KEY `registro_rbp_id_registro_rbp` (`registro_rbp_id_registro_rbp`),
  ADD KEY `razon_rechazo_id_razon_rechazo` (`razon_rechazo_id_razon_rechazo`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`id_empleado`),
  ADD UNIQUE KEY `codigo` (`codigo`),
  ADD UNIQUE KEY `codigo_aleatorio` (`codigo_alea`),
  ADD KEY `fk_proceso` (`id_proceso`);

--
-- Indices de la tabla `empleado_has_registro_rbp`
--
ALTER TABLE `empleado_has_registro_rbp`
  ADD KEY `empleado_has_registro_rbp_FKIndex1` (`empleado_id_empleado`),
  ADD KEY `empleado_has_registro_rbp_FKIndex2` (`registro_rbp_id_registro_rbp`),
  ADD KEY `empleado_has_registro_rbp_FKIndex3` (`empleado_supervisor_id_empleado_supervisor`);

--
-- Indices de la tabla `empleado_supervisor`
--
ALTER TABLE `empleado_supervisor`
  ADD PRIMARY KEY (`id_empleado_supervisor`),
  ADD KEY `empleado_supervisor_FKIndex1` (`empleado_id_empleado`),
  ADD KEY `procesos_idproceso` (`procesos_idproceso`);

--
-- Indices de la tabla `lote_coil`
--
ALTER TABLE `lote_coil`
  ADD PRIMARY KEY (`id_lote_coil`),
  ADD KEY `lote_coil_FKIndex1` (`registro_rbp_id_registro_rbp`),
  ADD KEY `mog_id_mog` (`mog_id_mog`);

--
-- Indices de la tabla `mog`
--
ALTER TABLE `mog`
  ADD PRIMARY KEY (`id_mog`);

--
-- Indices de la tabla `ordenes_abiertas_asupervisor`
--
ALTER TABLE `ordenes_abiertas_asupervisor`
  ADD PRIMARY KEY (`id_ordenAbierta`),
  ADD KEY `empleado_revision` (`empleado_revision`),
  ADD KEY `empleado_aprobacion` (`empleado_aprobacion`);

--
-- Indices de la tabla `ordenes_slitter`
--
ALTER TABLE `ordenes_slitter`
  ADD PRIMARY KEY (`id_ordenSlitter`);

--
-- Indices de la tabla `orden_coil`
--
ALTER TABLE `orden_coil`
  ADD PRIMARY KEY (`id_orden_coil`);

--
-- Indices de la tabla `piezas_procesadas`
--
ALTER TABLE `piezas_procesadas`
  ADD PRIMARY KEY (`idpiezas_procesadas`),
  ADD KEY `piezas_procesadas_FKIndex2` (`registro_rbp_id_registro_rbp`),
  ADD KEY `dasiddas` (`dasiddas`);

--
-- Indices de la tabla `piezas_procesadas_fg`
--
ALTER TABLE `piezas_procesadas_fg`
  ADD PRIMARY KEY (`id_piezas_procesadas_fg`),
  ADD KEY `piezas_procesadas_fg_FKIndex1` (`registro_rbp_id_registro_rbp`),
  ADD KEY `id_empleado` (`id_empleado`),
  ADD KEY `idmogcambio` (`idmogcambio`);

--
-- Indices de la tabla `piezas_procesadas_grading`
--
ALTER TABLE `piezas_procesadas_grading`
  ADD PRIMARY KEY (`id_piezasGrading`),
  ADD KEY `id_piezasProcesadas` (`id_piezasProcesadas`);

--
-- Indices de la tabla `procesos`
--
ALTER TABLE `procesos`
  ADD PRIMARY KEY (`id_proceso`);

--
-- Indices de la tabla `razon_rechazo1`
--
ALTER TABLE `razon_rechazo1`
  ADD PRIMARY KEY (`id_razon_rechazo`),
  ADD KEY `razon_rechazo_FKIndex1` (`categoria_rechazo_id_categoria_rechazo`);

--
-- Indices de la tabla `registrocausasparo`
--
ALTER TABLE `registrocausasparo`
  ADD PRIMARY KEY (`idregistrocausasparo`),
  ADD KEY `fk_empleado` (`empleado_idempleado`),
  ADD KEY `fk_cauasa` (`causas_paro_idcausas_paro`),
  ADD KEY `fk_das_id` (`das_id_das`),
  ADD KEY `mog_idmog` (`mog_idmog`);

--
-- Indices de la tabla `registrocausasparocoiling`
--
ALTER TABLE `registrocausasparocoiling`
  ADD PRIMARY KEY (`idregistrocausasparo`),
  ADD KEY `empleado_idempleado` (`empleado_idempleado`),
  ADD KEY `causas_paro_idcausas_paro` (`causas_paro_idcausas_paro`),
  ADD KEY `das_id_das` (`das_id_das`),
  ADD KEY `ordencoil_idordenc` (`ordencoil_idordenc`);

--
-- Indices de la tabla `registrocausasparoslitter`
--
ALTER TABLE `registrocausasparoslitter`
  ADD PRIMARY KEY (`idregistrocausasparo`),
  ADD KEY `empleado_idempleado` (`empleado_idempleado`),
  ADD KEY `causas_paro_idcausas_paro` (`causas_paro_idcausas_paro`),
  ADD KEY `ordenesslitter_idordenes` (`ordenesslitter_idordenes`),
  ADD KEY `das_id_das` (`das_id_das`);

--
-- Indices de la tabla `registro_rbp`
--
ALTER TABLE `registro_rbp`
  ADD PRIMARY KEY (`id_registro_rbp`),
  ADD KEY `fk_id_mg` (`mog_id_mog`);

--
-- Indices de la tabla `registro_x_hora`
--
ALTER TABLE `registro_x_hora`
  ADD PRIMARY KEY (`id_registro_x_hora`),
  ADD KEY `mog_id_mog` (`mog_id_mog`),
  ADD KEY `registro_rbp_id_registro_rbp` (`registro_rbp_id_registro_rbp`);

--
-- Indices de la tabla `registro_x_hora_maq`
--
ALTER TABLE `registro_x_hora_maq`
  ADD PRIMARY KEY (`id_registro_x_hora`),
  ADD KEY `mog_id_mog` (`mog_id_mog`),
  ADD KEY `registro_rbp_id_registro_rbp` (`registro_rbp_id_registro_rbp`),
  ADD KEY `empleado_id_empleado` (`empleado_id_empleado`);

--
-- Indices de la tabla `tiempo`
--
ALTER TABLE `tiempo`
  ADD PRIMARY KEY (`id_tiempos`),
  ADD KEY `tiempo_FKIndex1` (`registro_rbp_id_registro_rbp`),
  ADD KEY `pza_pro_id` (`pza_pro_id`);

--
-- Indices de la tabla `tiempo_sorteo`
--
ALTER TABLE `tiempo_sorteo`
  ADD PRIMARY KEY (`id_tiempo_sorteo`),
  ADD KEY `fk_em_id` (`id_empleado`),
  ADD KEY `fk_rbp_id` (`id_registro_rbp`),
  ADD KEY `fk_mog_id` (`mog_id_mog`);

--
-- Indices de la tabla `work_center_maquina`
--
ALTER TABLE `work_center_maquina`
  ADD PRIMARY KEY (`id_work_center_maquina`),
  ADD KEY `work_center_maquina_FKIndex1` (`empleado_supervisor_id_empleado_supervisor`),
  ADD KEY `fk_pro` (`procesos_idprocesos`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `catalogo_no_parte`
--
ALTER TABLE `catalogo_no_parte`
  MODIFY `id_no_parte` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=160;

--
-- AUTO_INCREMENT de la tabla `categoria_rechazo`
--
ALTER TABLE `categoria_rechazo`
  MODIFY `id_categoria_rechazo` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `causas_paro`
--
ALTER TABLE `causas_paro`
  MODIFY `idcausas_paro` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=364;

--
-- AUTO_INCREMENT de la tabla `cerradoordenes`
--
ALTER TABLE `cerradoordenes`
  MODIFY `id_cerrado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=318;

--
-- AUTO_INCREMENT de la tabla `coiling_material_a`
--
ALTER TABLE `coiling_material_a`
  MODIFY `id_coiling_mat` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `corriendoactualmente`
--
ALTER TABLE `corriendoactualmente`
  MODIFY `id_corriendo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=694;

--
-- AUTO_INCREMENT de la tabla `das`
--
ALTER TABLE `das`
  MODIFY `id_das` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=265;

--
-- AUTO_INCREMENT de la tabla `das_coiling_totales`
--
ALTER TABLE `das_coiling_totales`
  MODIFY `id_dascolitot` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_produccion`
--
ALTER TABLE `das_produccion`
  MODIFY `id_dasproduccion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=142;

--
-- AUTO_INCREMENT de la tabla `das_produ_empamesas`
--
ALTER TABLE `das_produ_empamesas`
  MODIFY `id_proEmpMes` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_prod_bgprensa`
--
ALTER TABLE `das_prod_bgprensa`
  MODIFY `id_dasprodbgp` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_prod_bgproceso`
--
ALTER TABLE `das_prod_bgproceso`
  MODIFY `id_dasprodbgproc` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_prod_coi`
--
ALTER TABLE `das_prod_coi`
  MODIFY `id_dasprodcoi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_prod_empmaq`
--
ALTER TABLE `das_prod_empmaq`
  MODIFY `das_prod_emp_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_prod_grading`
--
ALTER TABLE `das_prod_grading`
  MODIFY `id_prodGrad` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_prod_plat`
--
ALTER TABLE `das_prod_plat`
  MODIFY `das_prod_plat_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_prod_pren`
--
ALTER TABLE `das_prod_pren`
  MODIFY `id_daspropren` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=140;

--
-- AUTO_INCREMENT de la tabla `das_registrer`
--
ALTER TABLE `das_registrer`
  MODIFY `iddas_registro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT de la tabla `das_reg_prensa`
--
ALTER TABLE `das_reg_prensa`
  MODIFY `id_regprensa` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_reg_slitter`
--
ALTER TABLE `das_reg_slitter`
  MODIFY `id_das_reg_Slitter` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `das_slitter`
--
ALTER TABLE `das_slitter`
  MODIFY `id_das_slitter` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `defecto1`
--
ALTER TABLE `defecto1`
  MODIFY `id_defecto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1032;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `id_empleado` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=762;

--
-- AUTO_INCREMENT de la tabla `empleado_supervisor`
--
ALTER TABLE `empleado_supervisor`
  MODIFY `id_empleado_supervisor` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=68;

--
-- AUTO_INCREMENT de la tabla `lote_coil`
--
ALTER TABLE `lote_coil`
  MODIFY `id_lote_coil` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=212;

--
-- AUTO_INCREMENT de la tabla `mog`
--
ALTER TABLE `mog`
  MODIFY `id_mog` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=225;

--
-- AUTO_INCREMENT de la tabla `ordenes_abiertas_asupervisor`
--
ALTER TABLE `ordenes_abiertas_asupervisor`
  MODIFY `id_ordenAbierta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ordenes_slitter`
--
ALTER TABLE `ordenes_slitter`
  MODIFY `id_ordenSlitter` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `orden_coil`
--
ALTER TABLE `orden_coil`
  MODIFY `id_orden_coil` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `piezas_procesadas`
--
ALTER TABLE `piezas_procesadas`
  MODIFY `idpiezas_procesadas` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=432;

--
-- AUTO_INCREMENT de la tabla `piezas_procesadas_fg`
--
ALTER TABLE `piezas_procesadas_fg`
  MODIFY `id_piezas_procesadas_fg` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=116;

--
-- AUTO_INCREMENT de la tabla `piezas_procesadas_grading`
--
ALTER TABLE `piezas_procesadas_grading`
  MODIFY `id_piezasGrading` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `procesos`
--
ALTER TABLE `procesos`
  MODIFY `id_proceso` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `razon_rechazo1`
--
ALTER TABLE `razon_rechazo1`
  MODIFY `id_razon_rechazo` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=174;

--
-- AUTO_INCREMENT de la tabla `registrocausasparo`
--
ALTER TABLE `registrocausasparo`
  MODIFY `idregistrocausasparo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT de la tabla `registrocausasparocoiling`
--
ALTER TABLE `registrocausasparocoiling`
  MODIFY `idregistrocausasparo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `registrocausasparoslitter`
--
ALTER TABLE `registrocausasparoslitter`
  MODIFY `idregistrocausasparo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `registro_rbp`
--
ALTER TABLE `registro_rbp`
  MODIFY `id_registro_rbp` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=229;

--
-- AUTO_INCREMENT de la tabla `registro_x_hora`
--
ALTER TABLE `registro_x_hora`
  MODIFY `id_registro_x_hora` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `registro_x_hora_maq`
--
ALTER TABLE `registro_x_hora_maq`
  MODIFY `id_registro_x_hora` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT de la tabla `tiempo`
--
ALTER TABLE `tiempo`
  MODIFY `id_tiempos` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=429;

--
-- AUTO_INCREMENT de la tabla `tiempo_sorteo`
--
ALTER TABLE `tiempo_sorteo`
  MODIFY `id_tiempo_sorteo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `work_center_maquina`
--
ALTER TABLE `work_center_maquina`
  MODIFY `id_work_center_maquina` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=80;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `causas_paro`
--
ALTER TABLE `causas_paro`
  ADD CONSTRAINT `fk_proceso_causa` FOREIGN KEY (`procesos_idproceso`) REFERENCES `procesos` (`id_proceso`);

--
-- Filtros para la tabla `cerradoordenes`
--
ALTER TABLE `cerradoordenes`
  ADD CONSTRAINT `cerradoordenes_ibfk_1` FOREIGN KEY (`id_registro_rbp`) REFERENCES `registro_rbp` (`id_registro_rbp`),
  ADD CONSTRAINT `cerradoordenes_ibfk_2` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`),
  ADD CONSTRAINT `cerradoordenes_ibfk_3` FOREIGN KEY (`id_mog`) REFERENCES `mog` (`id_mog`);

--
-- Filtros para la tabla `coiling_material_a`
--
ALTER TABLE `coiling_material_a`
  ADD CONSTRAINT `coiling_material_a_ibfk_1` FOREIGN KEY (`dasprodcoi_iddasprodcoi`) REFERENCES `das_prod_coi` (`id_dasprodcoi`);

--
-- Filtros para la tabla `coiling_material_s`
--
ALTER TABLE `coiling_material_s`
  ADD CONSTRAINT `coiling_material_s_ibfk_1` FOREIGN KEY (`dasprodcoi_iddasprodcoi`) REFERENCES `das_prod_coi` (`id_dasprodcoi`);

--
-- Filtros para la tabla `das`
--
ALTER TABLE `das`
  ADD CONSTRAINT `das_ibfk_1` FOREIGN KEY (`empleado_id_empleado`) REFERENCES `empleado` (`id_empleado`),
  ADD CONSTRAINT `das_ibfk_2` FOREIGN KEY (`id_keeper`) REFERENCES `empleado` (`id_empleado`),
  ADD CONSTRAINT `das_ibfk_3` FOREIGN KEY (`id_inspector`) REFERENCES `empleado` (`id_empleado`);

--
-- Filtros para la tabla `das_coiling_totales`
--
ALTER TABLE `das_coiling_totales`
  ADD CONSTRAINT `das_coiling_totales_ibfk_1` FOREIGN KEY (`das_iddas`) REFERENCES `das` (`id_das`);

--
-- Filtros para la tabla `das_produccion`
--
ALTER TABLE `das_produccion`
  ADD CONSTRAINT `fk_das` FOREIGN KEY (`das_ida_das`) REFERENCES `das` (`id_das`),
  ADD CONSTRAINT `fk_mog` FOREIGN KEY (`mog_idmog`) REFERENCES `mog` (`id_mog`);

--
-- Filtros para la tabla `das_produ_empamesas`
--
ALTER TABLE `das_produ_empamesas`
  ADD CONSTRAINT `das_produ_empamesas_ibfk_1` FOREIGN KEY (`mog_idmog`) REFERENCES `mog` (`id_mog`),
  ADD CONSTRAINT `das_produ_empamesas_ibfk_2` FOREIGN KEY (`das_iddas`) REFERENCES `das` (`id_das`);

--
-- Filtros para la tabla `das_prod_bgprensa`
--
ALTER TABLE `das_prod_bgprensa`
  ADD CONSTRAINT `das_prod_bgprensa_ibfk_1` FOREIGN KEY (`mog_idmog`) REFERENCES `mog` (`id_mog`),
  ADD CONSTRAINT `das_prod_bgprensa_ibfk_2` FOREIGN KEY (`das_id_das`) REFERENCES `das` (`id_das`);

--
-- Filtros para la tabla `das_prod_bgproceso`
--
ALTER TABLE `das_prod_bgproceso`
  ADD CONSTRAINT `das_prod_bgproceso_ibfk_1` FOREIGN KEY (`das_iddas`) REFERENCES `das` (`id_das`),
  ADD CONSTRAINT `das_prod_bgproceso_ibfk_2` FOREIGN KEY (`mog_idmog`) REFERENCES `mog` (`id_mog`);

--
-- Filtros para la tabla `das_prod_coi`
--
ALTER TABLE `das_prod_coi`
  ADD CONSTRAINT `das_prod_coi_ibfk_1` FOREIGN KEY (`das_idas`) REFERENCES `das` (`id_das`),
  ADD CONSTRAINT `das_prod_coi_ibfk_2` FOREIGN KEY (`ordencoil_idordencoil`) REFERENCES `orden_coil` (`id_orden_coil`);

--
-- Filtros para la tabla `das_prod_empmaq`
--
ALTER TABLE `das_prod_empmaq`
  ADD CONSTRAINT `das_prod_empmaq_ibfk_1` FOREIGN KEY (`das_iddas`) REFERENCES `das` (`id_das`),
  ADD CONSTRAINT `das_prod_empmaq_ibfk_2` FOREIGN KEY (`mog_idmog`) REFERENCES `mog` (`id_mog`);

--
-- Filtros para la tabla `das_prod_plat`
--
ALTER TABLE `das_prod_plat`
  ADD CONSTRAINT `das_prod_plat_ibfk_1` FOREIGN KEY (`mog_idmog`) REFERENCES `mog` (`id_mog`),
  ADD CONSTRAINT `das_prod_plat_ibfk_2` FOREIGN KEY (`das_id_das`) REFERENCES `das` (`id_das`),
  ADD CONSTRAINT `das_prod_plat_ibfk_3` FOREIGN KEY (`piezasProcesadas_id_piezadprocesada`) REFERENCES `piezas_procesadas` (`idpiezas_procesadas`);

--
-- Filtros para la tabla `defecto1`
--
ALTER TABLE `defecto1`
  ADD CONSTRAINT `defecto1_ibfk_1` FOREIGN KEY (`registro_rbp_id_registro_rbp`) REFERENCES `registro_rbp` (`id_registro_rbp`),
  ADD CONSTRAINT `defecto1_ibfk_2` FOREIGN KEY (`razon_rechazo_id_razon_rechazo`) REFERENCES `razon_rechazo1` (`id_razon_rechazo`),
  ADD CONSTRAINT `fk_registro_id` FOREIGN KEY (`registro_rbp_id_registro_rbp`) REFERENCES `registro_rbp` (`id_registro_rbp`);

--
-- Filtros para la tabla `lote_coil`
--
ALTER TABLE `lote_coil`
  ADD CONSTRAINT `lote_coil_ibfk_1` FOREIGN KEY (`mog_id_mog`) REFERENCES `mog` (`id_mog`);

--
-- Filtros para la tabla `ordenes_abiertas_asupervisor`
--
ALTER TABLE `ordenes_abiertas_asupervisor`
  ADD CONSTRAINT `ordenes_abiertas_asupervisor_ibfk_1` FOREIGN KEY (`empleado_revision`) REFERENCES `empleado` (`id_empleado`),
  ADD CONSTRAINT `ordenes_abiertas_asupervisor_ibfk_2` FOREIGN KEY (`empleado_aprobacion`) REFERENCES `empleado` (`id_empleado`);

--
-- Filtros para la tabla `piezas_procesadas`
--
ALTER TABLE `piezas_procesadas`
  ADD CONSTRAINT `piezas_procesadas_ibfk_1` FOREIGN KEY (`dasiddas`) REFERENCES `das` (`id_das`);

--
-- Filtros para la tabla `registro_x_hora`
--
ALTER TABLE `registro_x_hora`
  ADD CONSTRAINT `registro_x_hora_ibfk_1` FOREIGN KEY (`mog_id_mog`) REFERENCES `mog` (`id_mog`),
  ADD CONSTRAINT `registro_x_hora_ibfk_2` FOREIGN KEY (`registro_rbp_id_registro_rbp`) REFERENCES `registro_rbp` (`id_registro_rbp`);

--
-- Filtros para la tabla `registro_x_hora_maq`
--
ALTER TABLE `registro_x_hora_maq`
  ADD CONSTRAINT `empleado_id_empleado` FOREIGN KEY (`empleado_id_empleado`) REFERENCES `empleado` (`id_empleado`);

--
-- Filtros para la tabla `tiempo`
--
ALTER TABLE `tiempo`
  ADD CONSTRAINT `tiempo_ibfk_1` FOREIGN KEY (`pza_pro_id`) REFERENCES `piezas_procesadas` (`idpiezas_procesadas`);

--
-- Filtros para la tabla `tiempo_sorteo`
--
ALTER TABLE `tiempo_sorteo`
  ADD CONSTRAINT `fk_em_id` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`),
  ADD CONSTRAINT `fk_mog_id` FOREIGN KEY (`mog_id_mog`) REFERENCES `mog` (`id_mog`),
  ADD CONSTRAINT `fk_rbp_id` FOREIGN KEY (`id_registro_rbp`) REFERENCES `registro_rbp` (`id_registro_rbp`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
