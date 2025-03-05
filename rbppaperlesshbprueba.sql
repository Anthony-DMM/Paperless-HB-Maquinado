-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 05-03-2025 a las 14:22:53
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
-- Base de datos: `rbppaperlesshbprueba`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `actualizarDAS` (IN `id_rbp_encontrado` INT, IN `fecha_ingresada` DATE, IN `soporte_rapido_ingresado` VARCHAR(45), IN `inspector_ingresado` VARCHAR(45), IN `lote_ingresado` VARCHAR(15), IN `turno_seleccionado` INT, IN `id_das_encontrado` INT, IN `operador_ingresado` VARCHAR(45))   BEGIN

update das set das.fecha = fecha_ingresada, das.turno = turno_seleccionado, das.registros_rbp_id_registro_rbp=id_rbp_encontrado, das.estatus=1 where das.id_das = id_das_encontrado;

insert into das_has_catalogo_empleados (das_id_das,id_operador,id_soporte_rapido,id_inspector) VALUES(id_das_encontrado,operador_ingresado,soporte_rapido_ingresado,inspector_ingresado);

update registros_rbp set registros_rbp.lote = lote_ingresado where registros_rbp.id_registro_rbp = id_rbp_encontrado;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `actualizarTablaTiempo` (IN `id_rbp_encontrado` INT, IN `turno_ingresado` INT, IN `hora_fin_ingresada` VARCHAR(8), IN `horas_trabajadas_ingresadas` VARCHAR(8), IN `id_tiempo_encontrado` INT)   BEGIN 
UPDATE tiempos set turno=turno_ingresado, hora_fin=hora_fin_ingresada, horas_trabajadas=horas_trabajadas_ingresadas where tiempos.registros_rbp_id_registro_rbp= id_rbp_encontrado and tiempos.id_tiempos=id_tiempo_encontrado; 
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `borrarregistroRBP` (IN `registroaborrar` INT)   BEGIN
Delete from das WHERE das.registros_rbp_id_registro_rbp IN (SELECT id_registro_rbp FROM registros_rbp where registros_rbp.id_registro_rbp=registroaborrar);

DELETE FROM estatus_rbp WHERE estatus_rbp.registros_rbp_id_registro_rbp IN ( SELECT id_registro_rbp FROM registros_rbp WHERE registros_rbp.mog_id_mog IN ( SELECT id_mog FROM mog WHERE mog.numero_parte_id_numero_parte = registroaborrar ));

delete from registros_rbp where registros_rbp.mog_id_mog IN(select id_mog from mog where mog.numero_parte_id_numero_parte=registroaborrar);

delete from mog where mog.numero_parte_id_numero_parte = registroaborrar;

delete from numero_parte where numero_parte.id_numero_parte= registroaborrar;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `buscarIDRBP` (IN `orden_ingresada` VARCHAR(45), OUT `id_encontrado` INT)   BEGIN
SET id_encontrado=(select registros_rbp.id_registro_rbp from registros_rbp where registros_rbp.orden_manufactura = orden_ingresada);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `buscarOperador` (IN `codigo_empleado` VARCHAR(45), OUT `nombre_empleado` VARCHAR(45))   BEGIN                         
set nombre_empleado = (SELECT CONCAT(catalogo_empleados.nombre, ' ', catalogo_empleados.apellido_paterno, ' ', catalogo_empleados.apellido_materno)
FROM catalogo_empleados
WHERE catalogo_empleados.codigo_aleatorio = codigo_empleado
AND catalogo_empleados.activo = 1
AND catalogo_empleados.catalogo_roles_id_catalogo_roles = 2
AND catalogo_empleados.catalogo_procesos_id_catalogo_proceso = 1);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `dasPreview` (IN `id_das_ingresado` INT, OUT `linea_final` VARCHAR(6), OUT `turno_final` INT, OUT `nombre_operador_final` VARCHAR(45), IN `id_operador_encontrado` VARCHAR(10))   BEGIN 


SELECT CONCAT(catalogo_empleados.nombre, ' ', catalogo_empleados.apellido_paterno, ' ', catalogo_empleados.apellido_materno) INTO nombre_operador_final FROM catalogo_empleados WHERE id_empleado = id_operador_encontrado; 
 
-- Obtener los detalles finales de la línea 
SELECT linea, turno INTO linea_final, turno_final FROM das WHERE id_das = id_das_ingresado; END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getCantidadCambioMog` (IN `id_tiempos_ingresado` INT, OUT `total_mog` INT)   BEGIN
declare cantidad int;

set cantidad=(SELECT SUM(piezas_procesadas.cambio_mog) as total from piezas_procesadas WHERE piezas_procesadas.tiempos_id_tiempos=id_tiempos_ingresado);

if (cantidad is null) THEN 
SET total_mog=0;
ELSE
SET total_mog=cantidad;
end if;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getParosLinea` (IN `id_das` INT)   BEGIN

SELECT registro_causas_paro.hora_inicio, registro_causas_paro.hora_fin, registro_causas_paro.tiempo_paro, catalogo_causas_paro.numero_causa_paro, registro_causas_paro.detalle from registro_causas_paro INNER JOIN catalogo_causas_paro on catalogo_causas_paro.id_catalogo_causas_paro=registro_causas_paro.catalogo_causas_paro_id_catalogo_causas_paro
WHERE registro_causas_paro.das_id_das=id_das;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getScrapTurnos` (IN `id_rbp` INT)   BEGIN

SELECT catalogo_razon_rechazo.numero_razon AS ID, registro_razon_rechazo.cantidad_scrap AS Cantidad, registro_razon_rechazo.turno AS Colum FROM registro_razon_rechazo INNER JOIN catalogo_razon_rechazo WHERE registro_razon_rechazo.registros_rbp_id_registro_rbp = id_rbp AND registro_razon_rechazo.columna_sorting is null and catalogo_razon_rechazo.id_catalogo_razon_rechazo = registro_razon_rechazo.catalogo_razon_rechazo_id_catalogo_razon_rechazo;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarActualizarScrap` (IN `id_rbp` INT, IN `id_razon` INT, IN `cantidad` INT, IN `turno_actual` INT)   BEGIN 
DECLARE exist int DEFAULT 0;

SET exist=(SELECT registro_razon_rechazo.catalogo_razon_rechazo_id_catalogo_razon_rechazo FROM registro_razon_rechazo WHERE registro_razon_rechazo.registros_rbp_id_registro_rbp=id_rbp AND registro_razon_rechazo.catalogo_razon_rechazo_id_catalogo_razon_rechazo=id_razon
and turno = turno_actual);

if(exist > 0) THEN
UPDATE `registro_razon_rechazo` SET `cantidad_scrap`=cantidad
WHERE registro_razon_rechazo.registros_rbp_id_registro_rbp = id_rbp AND registro_razon_rechazo.catalogo_razon_rechazo_id_catalogo_razon_rechazo = id_razon and turno=turno_actual;
ELSE
INSERT INTO `registro_razon_rechazo`(`registros_rbp_id_registro_rbp`, `catalogo_razon_rechazo_id_catalogo_razon_rechazo`, `cantidad_scrap`, `turno`) VALUES (id_rbp ,id_razon,cantidad,turno_actual);
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarDatosGenerales` (IN `mog_ingresada` VARCHAR(35), IN `descripcion_modelo` VARCHAR(45), IN `numero_dibujo_ingresado` VARCHAR(30), IN `numero_parte_ingresada` VARCHAR(30), IN `orden_manufactura_ingresada` VARCHAR(35), IN `estandar` VARCHAR(20), IN `peso_ingresado` FLOAT, IN `cantidad_planeada_ingresada` INT(11), IN `modelo_ingresado` VARCHAR(25), OUT `activada` INT, IN `linea_ingresada` VARCHAR(6), IN `fecha_ingresada` DATE, IN `hora_inicio_ingresada` VARCHAR(15), OUT `id_tiempos_encontrado` INT)   BEGIN
/*Se declaran las dos variables para recibir el último ID al insertar a cada tabla*/
DECLARE ultimo_id_numero_parte int;
DECLARE ultimo_id_mog int;
DECLARE ultimo_id_registro_rbp int;
DECLARE cadena varchar(30);

START TRANSACTION;

insert into numero_parte (numero_parte.modelo,numero_parte.cliente, numero_parte.descripcion,numero_parte.numero_dibujo,numero_parte.numero_parte,numero_parte.std,numero_parte.peso) VALUES (modelo_ingresado,"",descripcion_modelo,numero_dibujo_ingresado,numero_parte_ingresada,estandar,peso_ingresado);

SET ultimo_id_numero_parte = LAST_INSERT_ID();

insert into mog (mog.mog, mog.cantidad_planeada, mog.numero_parte_id_numero_parte) VALUES (mog_ingresada,cantidad_planeada_ingresada, ultimo_id_numero_parte);

SET ultimo_id_mog = LAST_INSERT_ID();

insert into registros_rbp (registros_rbp.orden_manufactura, registros_rbp.catalogo_procesos_id_catalogo_proceso, registros_rbp.mog_id_mog) VALUES (orden_manufactura_ingresada,1,ultimo_id_mog);

set ultimo_id_registro_rbp = LAST_INSERT_ID();

insert into estatus_rbp (estatus_rbp.estatus_rbp, estatus_rbp.estatus_produccion, estatus_rbp.estatus_supervisor, estatus_rbp.estatus_aduana, estatus_rbp.registros_rbp_id_registro_rbp) VALUES(1,1,1,1,ultimo_id_registro_rbp);

insert into tiempos (tiempos.linea, tiempos.fecha, tiempos.hora_inicio, tiempos.registros_rbp_id_registro_rbp) VALUES(linea_ingresada, fecha_ingresada, hora_inicio_ingresada,ultimo_id_registro_rbp);

set id_tiempos_encontrado= LAST_INSERT_ID();

SET cadena=(SELECT estatus_rbp.estatus_rbp from estatus_rbp INNER JOIN registros_rbp on estatus_rbp.registros_rbp_id_registro_rbp=registros_rbp.id_registro_rbp where registros_rbp.id_registro_rbp=ultimo_id_registro_rbp);
IF(cadena is null) THEN
SET activada=0;
ELSE
SET activada=1;
END IF;
COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `llenar_piezas_procesadas` (IN `sobrante_anterior` INT, IN `id_tiempos_encotrado` INT, IN `total_piezas` INT, IN `sobrante` INT, IN `tarjetas_procesadas` INT, OUT `rango_canasta_final1` INT, OUT `rango_canasta_final2` INT, IN `piezas_x_fila` INT, IN `filas` INT, IN `niveles` INT, IN `canastas` INT, IN `niveles_completo` INT, IN `filas_completas` INT, IN `cambio_mog` INT, IN `sobrante_final` INT, IN `numero_operador_encontrado` VARCHAR(10))   BEGIN
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

SET a1 = (piezas_x_fila * filas * niveles * canastas); 
SET a2 = (piezas_x_fila * filas * niveles_completo); 
SET a3 = ((piezas_x_fila * filas_completas) + sobrante_final); 

SET tt = (a1 + a2 + a3); 
SET sobF = (a2 + a3); SET pzab = (tt - sobrante_anterior); 

SET res = (SELECT piezas_procesadas.rango_canasta_1 FROM piezas_procesadas WHERE piezas_procesadas.tiempos_id_tiempos = id_tiempos_encotrado ORDER BY piezas_procesadas.id_piezas_procesadas DESC LIMIT 1); 

IF res IS NULL THEN 
	IF pzab = 0 THEN 
    SET rango1 = 0; 
    SET rango2 = ((0) + rango1); 
    ELSE 
    SET rango1 = 1; 
    SET rango2 = ((tarjetas_procesadas - 1) + rango1); 
    END IF; 
ELSE IF sobrante_anterior = 0 THEN 
SET rango1 = res + 1; 
SET rango2 = ((tarjetas_procesadas - 1) + rango1); 
ELSE 
SET rango1 = res; 
SET rango2 = ((tarjetas_procesadas - 1) + rango1); 
END IF; 
END IF; 

INSERT INTO `piezas_procesadas`( `registros_rbp_id_registro_rbp`, `rango_canasta_1`, `rango_canasta_2`, `cantidad_piezas_procesadas`, `sobrante_inicial`, `piezasxfila`, `filas`, `niveles`, `canastas`, `niveles_completos`, `filas_completas`, `cambio_mog`, `sobrante`, `cantidad_piezas_buenas`, `sobrante_final`, `catalogo_empleados_id_empleado` ) VALUES ( id_rbp_encotrado, rango1, rango2, total_piezas, sobrante_anterior, piezas_x_fila, filas, niveles, canastas, niveles_completo, filas_completas, cambio_mog, sobrante_final, pzab, sobF, numero_operador_encontrado ); 


SET rango_canasta_final1 = (SELECT piezas_procesadas.rango_canasta_1 FROM piezas_procesadas WHERE registros_rbp_id_registro_rbp = id_rbp_encotrado ORDER BY id_piezas_procesadas DESC LIMIT 1); 
SET rango_canasta_final2 = (SELECT piezas_procesadas.rango_canasta_2 FROM piezas_procesadas WHERE registros_rbp_id_registro_rbp = id_rbp_encotrado ORDER BY id_piezas_procesadas DESC LIMIT 1); 

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `login` (IN `codigo_empleado` VARCHAR(20), OUT `existe` INT, OUT `nombre_proceso` VARCHAR(20))   BEGIN    
DECLARE cadena varchar(50);                      
SET cadena= (SELECT numero_empleado from catalogo_empleados INNER join catalogo_roles on catalogo_roles.id_catalogo_roles=catalogo_empleados.catalogo_roles_id_catalogo_roles WHERE catalogo_empleados.codigo_aleatorio=codigo_empleado and catalogo_roles.rol='Supervisor' and catalogo_empleados.activo=true);
set nombre_proceso=(SELECT proceso from catalogo_procesos INNER JOIN catalogo_empleados on catalogo_empleados.catalogo_procesos_id_catalogo_proceso=catalogo_procesos.id_catalogo_proceso where catalogo_empleados.codigo_aleatorio=codigo_empleado);
IF (cadena is null) THEN
SET existe=0;
ELSE
SET existe=1;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `obtener_piezas_x_hora` (IN `id_das_ingresado` INT)   BEGIN

select hora, piezas_x_hora.piezas_x_hora, acumulado, ok_ng, catalogo_empleados.nombre from piezas_x_hora INNER JOIN catalogo_empleados on catalogo_empleados.id_empleado = piezas_x_hora.catalogo_empleados_id_empleado where piezas_x_hora.das_id_das = id_das_ingresado;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `piezasxhora` (IN `id_das_ingresado` INT, IN `acumulado_ingresado` INT, IN `calidad` VARCHAR(3), IN `hora_ingresada` VARCHAR(6), IN `id_empleado_ingresado` INT)   BEGIN
DECLARE ultimo_acumulado INT DEFAULT 0; 
DECLARE nuevo_acumulado INT;  
SELECT piezas_x_hora.acumulado INTO ultimo_acumulado FROM piezas_x_hora WHERE piezas_x_hora.das_id_das = id_das_ingresado ORDER BY piezas_x_hora.id_piezas_x_hora DESC LIMIT 1; 
-- Calcular nuevo acumulado 
IF ultimo_acumulado = 0 THEN 
INSERT INTO piezas_x_hora (hora, piezas_x_hora, acumulado, ok_ng, das_id_das, catalogo_empleados_id_empleado) VALUES (hora_ingresada, acumulado_ingresado, acumulado_ingresado, calidad, id_das_ingresado, id_empleado_ingresado); 
ELSE 
SET nuevo_acumulado = acumulado_ingresado - ultimo_acumulado; INSERT INTO piezas_x_hora (hora, piezas_x_hora, acumulado, ok_ng, das_id_das, catalogo_empleados_id_empleado) VALUES (hora_ingresada, nuevo_acumulado, acumulado_ingresado, calidad, id_das_ingresado, id_empleado_ingresado); 
END IF;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `pruebasuma` (IN `id_das_ingresado` INT)   BEGIN

select hora, piezas_x_hora.piezas_x_hora, acumulado, ok_ng, catalogo_empleados.nombre from piezas_x_hora INNER JOIN catalogo_empleados on catalogo_empleados.id_empleado = piezas_x_hora.catalogo_empleados_id_empleado where piezas_x_hora.das_id_das = id_das_ingresado;

select SUM(piezas_x_hora.piezas_x_hora) AS total from piezas_x_hora where piezas_x_hora.das_id_das = id_das_ingresado;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `registrarCausaParo` (IN `tiempo_paro` INT(3), IN `detalle` VARCHAR(200), IN `hora_inicio` VARCHAR(5), IN `hora_fin` VARCHAR(5), IN `fecha` DATE, IN `numero_empleado` VARCHAR(7), IN `id_das` INT(8), IN `causa_paro` VARCHAR(35))   BEGIN
DECLARE id_causa_paro int;

SET id_causa_paro=(SELECT catalogo_causas_paro.id_catalogo_causas_paro FROM catalogo_causas_paro WHERE Concat(catalogo_causas_paro.numero_causa_paro, " ",catalogo_causas_paro.descripcion) = causa_paro);

INSERT INTO `registro_causas_paro`(`tiempo_paro`, `detalle`, `hora_inicio`, `hora_fin`, `fecha`, `id_operador`,`das_id_das`,`catalogo_causas_paro_id_catalogo_causas_paro`) VALUES (tiempo_paro,detalle,hora_inicio,hora_fin,fecha,numero_empleado,id_das,id_causa_paro);

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `registrarDAS` (IN `id_rbp_encontrado` INT, IN `linea_ingresada` VARCHAR(10), OUT `ultimo_id_das` INT)   BEGIN

INSERT INTO das (das.linea, das.registros_rbp_id_registro_rbp, das.estatus) VALUES(linea_ingresada, id_rbp_encontrado, 0);

SET ultimo_id_das=LAST_INSERT_ID();

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `registrarTablaTiempos` (IN `id_rbp_encontrado` INT, IN `linea_ingresada` VARCHAR(8), IN `fecha_ingresada` DATE, IN `hora_ingresada` VARCHAR(10), OUT `id_tiempo_encontrado` INT)   BEGIN

insert into tiempos (tiempos.linea,tiempos.fecha,tiempos.hora_inicio, tiempos.registros_rbp_id_registro_rbp) values(linea_ingresada, fecha_ingresada, hora_ingresada, id_rbp_encontrado);


set id_tiempo_encontrado =LAST_INSERT_ID();

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerCategoria` (IN `nombre_proceso` VARCHAR(35))   Begin

select categoria from catalogo_categoria_causa_paro c INNER JOIN catalogo_procesos p 
ON c.catalogo_procesos_id_catalogo_proceso = p.id_catalogo_proceso WHERE p.proceso = nombre_proceso;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerCausaParoProceso` (IN `categoria_seleccionada` VARCHAR(45))   BEGIN

SELECT CONCAT(catalogo_causas_paro.numero_causa_paro, ' ',catalogo_causas_paro.descripcion) as descripcion_completa from catalogo_causas_paro INNER JOIN catalogo_categoria_causa_paro on catalogo_causas_paro. catalogo_categoria_causa_paro_id_catalogo_categoria_causa_paro = catalogo_categoria_causa_paro.id_catalogo_categoria_causa_paro where catalogo_categoria_causa_paro.categoria=categoria_seleccionada;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerEstatus` (IN `id_das_ingresado` INT, OUT `estatus_encontrado` TINYINT)   BEGIN

select das.estatus into estatus_encontrado from das where id_das = id_das_ingresado;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerGrupoLinea` (IN `linea_ingresada` VARCHAR(8), OUT `grupo_encontrado` VARCHAR(8))   BEGIN
SELECT grupo into grupo_encontrado FROM catalogo_lineas_produccion where catalogo_lineas_produccion.linea_produccion= linea_ingresada;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerInspector` (IN `codigo_empleado` VARCHAR(20), OUT `nombre_empleado` VARCHAR(45))   BEGIN                         
set nombre_empleado = (SELECT CONCAT(catalogo_empleados.nombre, ' ', catalogo_empleados.apellido_paterno, ' ', catalogo_empleados.apellido_materno)
FROM catalogo_empleados
WHERE catalogo_empleados.numero_empleado = codigo_empleado
AND catalogo_empleados.activo = 1
AND catalogo_empleados.catalogo_roles_id_catalogo_roles = 3
AND catalogo_empleados.catalogo_procesos_id_catalogo_proceso = 1);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerRazonRechazo` (IN `proceso` VARCHAR(20))   BEGIN
if proceso="MAQUINADO" THEN
select catalogo_razon_rechazo.numero_razon, catalogo_razon_rechazo.nombre_rechazo from catalogo_razon_rechazo inner join catalogo_procesos on catalogo_procesos.id_catalogo_proceso=catalogo_razon_rechazo.catalogo_procesos_id_catalogo_proceso where catalogo_razon_rechazo.catalogo_procesos_id_catalogo_proceso=1 or catalogo_razon_rechazo.catalogo_procesos_id_catalogo_proceso = 2 or catalogo_razon_rechazo.catalogo_procesos_id_catalogo_proceso = 3 ORDER BY catalogo_razon_rechazo.numero_razon ASC;

elseif proceso ="PRENSA" THEN
select catalogo_razon_rechazo.numero_razon,catalogo_razon_rechazo.nombre_rechazo from catalogo_razon_rechazo inner join catalogo_procesos on catalogo_procesos.id_catalogo_proceso=catalogo_razon_rechazo.catalogo_procesos_id_catalogo_proceso where catalogo_razon_rechazo.catalogo_procesos_id_catalogo_proceso = 2 or catalogo_razon_rechazo.catalogo_procesos_id_catalogo_proceso = 3;
end if;



END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerSoporteRapido` (IN `codigo_empleado` VARCHAR(20), OUT `nombre_empleado` VARCHAR(45))   BEGIN                         
set nombre_empleado = (SELECT CONCAT(catalogo_empleados.nombre, ' ', catalogo_empleados.apellido_paterno, ' ', catalogo_empleados.apellido_materno)
FROM catalogo_empleados
WHERE catalogo_empleados.numero_empleado = codigo_empleado
AND catalogo_empleados.activo = 1
AND catalogo_empleados.catalogo_roles_id_catalogo_roles = 4
AND catalogo_empleados.catalogo_procesos_id_catalogo_proceso = 1);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `traerTurnosScrap` (IN `id_rbp` INT, OUT `ultimo_turno` INT)   BEGIN    
set ultimo_turno = (SELECT MAX(registro_razon_rechazo.turno) FROM registro_razon_rechazo WHERE registro_razon_rechazo.registros_rbp_id_registro_rbp = id_rbp);

if (ultimo_turno is null) THEN
set ultimo_turno = 0;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `validarLinea` (IN `linea_ingresada` VARCHAR(20), OUT `existe` INT, OUT `supervisor` VARCHAR(45))   BEGIN    
DECLARE cadena varchar(50);                      
SET cadena=(SELECT linea_produccion from catalogo_lineas_produccion where linea_produccion =linea_ingresada);
set supervisor=(select CONCAT(nombre, ' ', apellido_paterno, ' ', apellido_materno) from catalogo_empleados INNER JOIN catalogo_empleados_has_catalogo_lineas_produccion on catalogo_empleados.id_empleado = catalogo_empleados_has_catalogo_lineas_produccion.catalogo_empleados_id_empleado INNER JOIN catalogo_lineas_produccion on catalogo_empleados_has_catalogo_lineas_produccion.catalogo_lineas_produccion_id_catalogo_lineas_produccion = catalogo_lineas_produccion.id_catalogo_lineas_produccion where catalogo_lineas_produccion.linea_produccion=linea_ingresada and catalogo_empleados.activo = 1 LIMIT 1);
IF (cadena is null) THEN
SET existe=0;
ELSE
SET existe=1;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `validarOrdenEscaneada` (IN `orden_ingresada` VARCHAR(20), OUT `existe` INT)   BEGIN    
DECLARE cadena varchar(50);                      
SET cadena=(SELECT orden_manufactura FROM `registros_rbp` where registros_rbp.orden_manufactura = orden_ingresada);
IF (cadena is null) THEN
SET existe=0;
ELSE
SET existe=1;
END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_categoria_causa_paro`
--

CREATE TABLE `catalogo_categoria_causa_paro` (
  `id_catalogo_categoria_causa_paro` int(3) NOT NULL,
  `categoria` varchar(35) NOT NULL,
  `catalogo_procesos_id_catalogo_proceso` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_categoria_causa_paro`
--

INSERT INTO `catalogo_categoria_causa_paro` (`id_catalogo_categoria_causa_paro`, `categoria`, `catalogo_procesos_id_catalogo_proceso`) VALUES
(1, 'Paro Planeado', 1),
(2, 'Ajuste/Cambio Modelo-MOG', 1),
(3, 'Mantenimiento', 1),
(4, 'PRUEBA', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_causas_paro`
--

CREATE TABLE `catalogo_causas_paro` (
  `id_catalogo_causas_paro` int(3) NOT NULL,
  `numero_causa_paro` int(3) NOT NULL,
  `descripcion` varchar(35) NOT NULL,
  `catalogo_categoria_causa_paro_id_catalogo_categoria_causa_paro` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_causas_paro`
--

INSERT INTO `catalogo_causas_paro` (`id_catalogo_causas_paro`, `numero_causa_paro`, `descripcion`, `catalogo_categoria_causa_paro_id_catalogo_categoria_causa_paro`) VALUES
(1, 10, 'Taiso y junta inicial', 1),
(2, 11, 'Resultado de inspección', 1),
(3, 12, 'TPM', 1),
(4, 20, 'Cambio de MOG', 2),
(5, 0, 'Cambio de Modelo', 2),
(6, 21, 'Cambio de Modelo', 2),
(7, 30, 'Reparación', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_empleados`
--

CREATE TABLE `catalogo_empleados` (
  `id_empleado` int(5) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `apellido_paterno` varchar(30) NOT NULL,
  `apellido_materno` varchar(30) NOT NULL,
  `numero_empleado` int(7) NOT NULL,
  `codigo_aleatorio` varchar(13) NOT NULL,
  `activo` tinyint(4) NOT NULL,
  `catalogo_roles_id_catalogo_roles` int(2) NOT NULL,
  `catalogo_procesos_id_catalogo_proceso` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_empleados`
--

INSERT INTO `catalogo_empleados` (`id_empleado`, `nombre`, `apellido_paterno`, `apellido_materno`, `numero_empleado`, `codigo_aleatorio`, `activo`, `catalogo_roles_id_catalogo_roles`, `catalogo_procesos_id_catalogo_proceso`) VALUES
(1, 'David Isaac', 'Garcia', 'Salazar', 1, '1', 1, 1, 1),
(2, 'Azael', 'López', 'Ramírez', 2, '2', 1, 2, 1),
(3, 'Pepito', 'Santana', 'Sanchez', 3, '3', 1, 3, 1),
(4, 'Esmeralda', 'Palacios', 'Patiño', 4, '4', 1, 4, 1),
(5, 'Prueba', 'Garcia', 'Salazar', 5, '5', 1, 1, 2),
(6, 'Lizbeth', 'Noriega', 'Gutierrez', 9, '9', 1, 3, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_empleados_has_catalogo_lineas_produccion`
--

CREATE TABLE `catalogo_empleados_has_catalogo_lineas_produccion` (
  `catalogo_empleados_id_empleado` int(5) NOT NULL,
  `catalogo_lineas_produccion_id_catalogo_lineas_produccion` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_empleados_has_catalogo_lineas_produccion`
--

INSERT INTO `catalogo_empleados_has_catalogo_lineas_produccion` (`catalogo_empleados_id_empleado`, `catalogo_lineas_produccion_id_catalogo_lineas_produccion`) VALUES
(1, 1),
(1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_lineas_produccion`
--

CREATE TABLE `catalogo_lineas_produccion` (
  `id_catalogo_lineas_produccion` int(3) NOT NULL,
  `linea_produccion` varchar(10) NOT NULL,
  `grupo` int(1) DEFAULT NULL,
  `catalogo_procesos_id_catalogo_proceso` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_lineas_produccion`
--

INSERT INTO `catalogo_lineas_produccion` (`id_catalogo_lineas_produccion`, `linea_produccion`, `grupo`, `catalogo_procesos_id_catalogo_proceso`) VALUES
(1, 'TH-07', 1, 1),
(2, 'TH-17', 2, 1),
(3, 'TH-11', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_procesos`
--

CREATE TABLE `catalogo_procesos` (
  `id_catalogo_proceso` int(3) NOT NULL,
  `proceso` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_procesos`
--

INSERT INTO `catalogo_procesos` (`id_catalogo_proceso`, `proceso`) VALUES
(1, 'MAQUINADO'),
(2, 'PRENSA'),
(3, 'COILING');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_razon_rechazo`
--

CREATE TABLE `catalogo_razon_rechazo` (
  `id_catalogo_razon_rechazo` int(4) NOT NULL,
  `nombre_rechazo` varchar(35) NOT NULL,
  `numero_razon` int(4) NOT NULL,
  `estatus` tinyint(4) NOT NULL,
  `catalogo_procesos_id_catalogo_proceso` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_razon_rechazo`
--

INSERT INTO `catalogo_razon_rechazo` (`id_catalogo_razon_rechazo`, `nombre_rechazo`, `numero_razon`, `estatus`, `catalogo_procesos_id_catalogo_proceso`) VALUES
(4, 'Rayón espalda', 9, 1, 3),
(5, 'Mal corte de chaflán', 11, 1, 3),
(6, 'Mal corte de ancho', 12, 1, 3),
(7, 'Torsión', 13, 1, 3),
(8, 'Otros', 14, 1, 3),
(9, 'Golpe espalda', 15, 1, 2),
(10, 'Extensión', 16, 1, 2),
(11, 'Torsión', 17, 1, 2),
(12, 'Sello', 18, 1, 2),
(13, 'Área de contacto', 19, 1, 2),
(14, 'Bad mark', 20, 1, 2),
(15, 'Óxido', 21, 1, 2),
(16, 'Moleteado', 32, 1, 2),
(17, 'Otros', 33, 1, 2),
(18, 'Rebaba en orificio', 34, 1, 1),
(19, 'Rebaba en uña', 35, 1, 1),
(20, 'Rebaba en canal', 36, 1, 1),
(21, 'Corte de esquinas', 37, 1, 1),
(22, 'Orificio', 38, 1, 1),
(23, 'Rayón cara interna', 39, 1, 1),
(24, 'Rayón espalda', 40, 1, 1),
(25, 'Rayón cara lateral', 41, 1, 1),
(26, 'Espesor', 42, 1, 1),
(27, 'Golpe en chaflán interno', 43, 1, 1),
(28, 'Golpe cara lateral', 44, 1, 1),
(29, 'Golpe en chaflan de canal', 45, 1, 1),
(30, 'Golpe chaflán corte altura', 46, 1, 1),
(31, 'Golpe espalda', 47, 1, 1),
(32, 'Alivio de uña', 48, 1, 1),
(33, 'Expulsión de uña', 49, 1, 1),
(34, 'Canal', 50, 1, 1),
(35, 'Chaflán de orificio', 51, 1, 1),
(36, 'Acabado de ancho', 52, 1, 1),
(37, 'Verificador de ancho', 53, 1, 1),
(38, 'Alimentador', 54, 1, 1),
(39, 'Mal manejo', 55, 1, 1),
(40, 'Cepillado', 56, 1, 1),
(41, 'Corte de altura', 57, 1, 1),
(42, 'Autochecador - altura', 58, 1, 1),
(43, 'Autochecador - proceso', 59, 1, 1),
(44, 'Pokayoke boring / broach', 60, 1, 1),
(45, 'Dandori', 61, 1, 1),
(46, 'Inspección de calidad', 62, 1, 1),
(47, 'Otros', 63, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `catalogo_roles`
--

CREATE TABLE `catalogo_roles` (
  `id_catalogo_roles` int(2) NOT NULL,
  `rol` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `catalogo_roles`
--

INSERT INTO `catalogo_roles` (`id_catalogo_roles`, `rol`) VALUES
(1, 'Supervisor'),
(2, 'Operador'),
(3, 'Inspector'),
(4, 'Soporte Rápido');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das`
--

CREATE TABLE `das` (
  `id_das` int(8) NOT NULL,
  `linea` varchar(10) NOT NULL,
  `fecha` date DEFAULT NULL,
  `turno` int(2) DEFAULT NULL,
  `estatus` tinyint(4) NOT NULL,
  `registros_rbp_id_registro_rbp` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `das`
--

INSERT INTO `das` (`id_das`, `linea`, `fecha`, `turno`, `estatus`, `registros_rbp_id_registro_rbp`) VALUES
(106, 'TH-17', NULL, NULL, 0, 4),
(107, 'TH-07', NULL, NULL, 0, 5),
(108, 'TH-07', NULL, NULL, 0, 5),
(109, 'TH-07', NULL, NULL, 0, 5),
(110, 'TH-07', NULL, NULL, 0, 5),
(111, 'TH-07', NULL, NULL, 0, 5),
(112, 'TH-07', NULL, NULL, 0, 5),
(113, 'TH-07', NULL, NULL, 0, 5),
(114, 'TH-07', NULL, NULL, 0, 5),
(115, 'TH-07', NULL, NULL, 0, 5),
(116, 'TH-07', NULL, NULL, 0, 5),
(117, 'TH-07', NULL, NULL, 0, 5),
(118, 'TH-07', NULL, NULL, 0, 5),
(119, 'TH-07', NULL, NULL, 0, 5),
(120, 'TH-07', NULL, NULL, 0, 5),
(121, 'TH-07', NULL, NULL, 0, 5),
(122, 'TH-07', NULL, NULL, 0, 5),
(123, 'TH-07', NULL, NULL, 0, 5),
(124, 'TH-07', NULL, NULL, 0, 5),
(125, 'TH-07', NULL, NULL, 0, 5),
(126, 'TH-07', NULL, NULL, 0, 5),
(127, 'TH-07', NULL, NULL, 0, 5),
(128, 'TH-07', NULL, NULL, 0, 5),
(129, 'TH-07', NULL, NULL, 0, 5),
(130, 'TH-07', NULL, NULL, 0, 5),
(131, 'TH-07', NULL, NULL, 0, 5),
(132, 'TH-07', NULL, NULL, 0, 5),
(133, 'TH-07', NULL, NULL, 0, 5),
(134, 'TH-07', NULL, NULL, 0, 5),
(135, 'TH-07', NULL, NULL, 0, 5),
(136, 'TH-07', NULL, NULL, 0, 5),
(137, 'TH-07', NULL, NULL, 0, 5),
(138, 'TH-07', NULL, NULL, 0, 5),
(139, 'TH-07', NULL, NULL, 0, 5),
(140, 'TH-07', NULL, NULL, 0, 5),
(141, 'TH-07', NULL, NULL, 0, 5),
(142, 'TH-07', NULL, NULL, 0, 5),
(143, 'TH-07', NULL, NULL, 0, 5),
(144, 'TH-07', NULL, NULL, 0, 5),
(145, 'TH-07', NULL, NULL, 0, 5),
(146, 'TH-07', NULL, NULL, 0, 5),
(147, 'TH-07', NULL, NULL, 0, 5),
(148, 'TH-07', NULL, NULL, 0, 5),
(149, 'TH-07', NULL, NULL, 0, 5),
(150, 'TH-07', NULL, NULL, 0, 5),
(151, 'TH-07', NULL, NULL, 0, 5),
(152, 'TH-07', NULL, NULL, 0, 5),
(153, 'TH-07', NULL, NULL, 0, 5),
(154, 'TH-07', '2024-11-28', 2, 1, 5),
(155, 'TH-07', '2024-11-28', 2, 1, 5),
(156, 'TH-07', '2024-11-28', 2, 1, 5),
(157, 'TH-07', '2024-11-28', 1, 1, 5),
(158, 'TH-07', '2024-11-28', 1, 1, 5),
(159, 'TH-07', '2024-11-28', 2, 1, 5),
(160, 'TH-07', '2024-11-28', 2, 1, 5),
(161, 'TH-07', '2024-11-28', 2, 1, 5),
(162, 'TH-07', NULL, NULL, 0, 5),
(163, 'TH-07', '2024-12-04', 1, 1, 4),
(164, 'TH-07', '2024-12-04', 1, 1, 6),
(165, 'th-07', '2024-12-04', 2, 1, 6),
(166, 'TH-07', NULL, NULL, 0, 4),
(167, 'TH-17', NULL, NULL, 0, 4),
(168, 'TH-17', NULL, NULL, 0, 7),
(169, 'TH-17', NULL, NULL, 0, 7),
(170, 'TH-17', NULL, NULL, 0, 7),
(171, 'TH-17', NULL, NULL, 0, 7),
(172, 'TH-17', NULL, NULL, 0, 7),
(173, 'TH-17', '2025-01-28', 1, 1, 7),
(174, 'TH-17', '2025-01-28', 1, 1, 7),
(175, 'TH-17', '2025-01-30', 1, 1, 7),
(176, 'TH-17', NULL, NULL, 0, 7),
(177, 'TH-17', '2025-01-30', 1, 1, 7),
(178, 'TH-17', '2025-01-30', 3, 1, 7),
(179, 'th-17', '2025-01-30', 1, 1, 7),
(180, 'TH-17', '2025-01-30', 1, 1, 7),
(181, 'TH-17', NULL, NULL, 0, 7),
(182, 'TH-17', '2025-02-04', 1, 1, 7),
(183, 'TH-17', NULL, NULL, 0, 7),
(184, 'TH-17', '2025-02-04', 1, 1, 7),
(185, 'TH-17', NULL, NULL, 0, 7),
(186, 'TH-17', NULL, NULL, 0, 7),
(187, 'TH-17', NULL, NULL, 0, 7),
(188, 'TH-17', NULL, NULL, 0, 7),
(189, 'TH-17', NULL, NULL, 0, 7),
(190, 'TH-17', NULL, NULL, 0, 8),
(191, 'TH-17', NULL, NULL, 0, 7),
(192, 'TH-17', NULL, NULL, 0, 7),
(193, 'TH-17', NULL, NULL, 0, 9),
(194, 'TH-17', NULL, NULL, 0, 9),
(195, 'TH-17', NULL, NULL, 0, 9),
(196, 'TH-17', NULL, NULL, 0, 9),
(197, 'TH-17', NULL, NULL, 0, 9),
(198, 'TH-17', NULL, NULL, 0, 9),
(199, 'TH-17', NULL, NULL, 0, 7),
(200, 'TH-17', NULL, NULL, 0, 7),
(201, 'th-17', NULL, NULL, 0, 7),
(202, 'th-17', NULL, NULL, 0, 7),
(203, 'th-17', NULL, NULL, 0, 7),
(204, 'th-17', NULL, NULL, 0, 7),
(205, 'TH-17', '2025-02-14', 1, 1, 7),
(206, 'th-17', NULL, NULL, 0, 7),
(207, 'TH-17', NULL, NULL, 0, 7),
(208, 'TH-17', NULL, NULL, 0, 7),
(209, 'TH-17', NULL, NULL, 0, 7),
(210, 'TH-17', NULL, NULL, 0, 7),
(211, 'th-17', NULL, NULL, 0, 7),
(212, 'TH-17', NULL, NULL, 0, 7),
(213, 'TH-17', NULL, NULL, 0, 7),
(214, 'TH-17', '2025-02-27', 3, 1, 7),
(215, 'TH-17', NULL, NULL, 0, 7),
(216, 'TH-17', NULL, NULL, 0, 7),
(217, 'TH-17', NULL, NULL, 0, 7),
(218, 'TH-17', NULL, NULL, 0, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `das_has_catalogo_empleados`
--

CREATE TABLE `das_has_catalogo_empleados` (
  `das_id_das` int(8) NOT NULL,
  `id_operador` int(5) NOT NULL,
  `id_soporte_rapido` int(5) NOT NULL,
  `id_inspector` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `das_has_catalogo_empleados`
--

INSERT INTO `das_has_catalogo_empleados` (`das_id_das`, `id_operador`, `id_soporte_rapido`, `id_inspector`) VALUES
(154, 2, 4, 3),
(155, 2, 4, 3),
(156, 2, 4, 3),
(157, 2, 4, 3),
(158, 2, 4, 3),
(159, 2, 4, 3),
(160, 2, 4, 3),
(161, 2, 4, 3),
(163, 2, 4, 3),
(164, 2, 4, 3),
(165, 2, 4, 3),
(173, 2, 4, 3),
(174, 2, 4, 3),
(175, 2, 4, 3),
(177, 2, 4, 3),
(178, 2, 4, 3),
(179, 2, 4, 3),
(180, 2, 4, 3),
(182, 2, 4, 3),
(184, 2, 4, 3),
(205, 2, 4, 3),
(214, 2, 4, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados_has_registros_rbp`
--

CREATE TABLE `empleados_has_registros_rbp` (
  `id_operador` int(5) DEFAULT NULL,
  `id_supervisor` int(5) DEFAULT NULL,
  `registros_rbp_id_registro_rbp` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estatus_rbp`
--

CREATE TABLE `estatus_rbp` (
  `id_estatus_rbp` int(11) NOT NULL,
  `estatus_rbp` tinyint(4) NOT NULL,
  `estatus_produccion` tinyint(4) NOT NULL,
  `estatus_supervisor` tinyint(4) NOT NULL,
  `estatus_aduana` tinyint(4) NOT NULL,
  `registros_rbp_id_registro_rbp` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `estatus_rbp`
--

INSERT INTO `estatus_rbp` (`id_estatus_rbp`, `estatus_rbp`, `estatus_produccion`, `estatus_supervisor`, `estatus_aduana`, `registros_rbp_id_registro_rbp`) VALUES
(5, 1, 1, 1, 1, 5),
(6, 1, 1, 1, 1, 6),
(7, 1, 1, 1, 1, 7),
(8, 1, 1, 1, 1, 8),
(9, 1, 1, 1, 1, 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mog`
--

CREATE TABLE `mog` (
  `id_mog` int(7) NOT NULL,
  `mog` varchar(15) NOT NULL,
  `cantidad_planeada` int(5) NOT NULL,
  `numero_parte_id_numero_parte` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `mog`
--

INSERT INTO `mog` (`id_mog`, `mog`, `cantidad_planeada`, `numero_parte_id_numero_parte`) VALUES
(4, 'MOG059691', 20000, 4),
(5, 'MOG075387', 30000, 5),
(6, 'MOG057663', 20000, 6),
(7, 'MOG083559', 0, 7),
(8, 'MOG083558', 0, 8),
(9, 'MOG083172', 0, 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `numero_parte`
--

CREATE TABLE `numero_parte` (
  `id_numero_parte` int(8) NOT NULL,
  `modelo` varchar(35) NOT NULL,
  `cliente` varchar(25) DEFAULT NULL,
  `descripcion` varchar(35) NOT NULL,
  `numero_dibujo` varchar(15) NOT NULL,
  `numero_parte` varchar(20) NOT NULL,
  `std` varchar(5) NOT NULL,
  `peso` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `numero_parte`
--

INSERT INTO `numero_parte` (`id_numero_parte`, `modelo`, `cliente`, `descripcion`, `numero_dibujo`, `numero_parte`, `std`, `peso`) VALUES
(4, 'HB PENTASTARCR STD2', '', 'PENTASTARCR STD2', 'A-215253', '04893952AA', 'STD 2', 19.261),
(5, 'HB PENTASTARCR STD3', '', 'PENTASTARCR STD3', 'A-215253', '04893953AA', 'STD 3', 19.261),
(6, 'HB GTDI M/N L', '', 'GTDI M/N L', 'A-291365-1', '9E5G6A338DA', 'STD 4', 32.9),
(7, 'HB 122Y B/M STD 2', '', '122Y B/M STD 2', 'A-146209-1', '11911-F0010-02', 'STD 2', 10.8),
(8, 'HB HB NP0 C/R STD D', '', 'HB NP0 C/R STD D', 'A-171698-01', '13214-R70-D011-M1', 'STD D', 17.47),
(9, 'HB I3 MPC DRMU STD1', '', 'I3 MPC DRMU STD1', 'A-293434-03', 'PV4E-6333-AA', 'STD 1', 28.5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ordenes_cerradas`
--

CREATE TABLE `ordenes_cerradas` (
  `id_ordenes_cerradas` int(8) NOT NULL,
  `fecha` date NOT NULL,
  `hora_liberacion` varchar(8) NOT NULL,
  `tipo_liberacion` varchar(20) NOT NULL,
  `registros_rbp_id_registro_rbp` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `piezas_procesadas`
--

CREATE TABLE `piezas_procesadas` (
  `id_piezas_procesadas` int(8) NOT NULL,
  `rango_canasta_1` int(2) NOT NULL,
  `rango_canasta_2` int(2) NOT NULL,
  `cantidad_piezas_procesadas` int(5) NOT NULL,
  `sobrante_inicial` int(5) NOT NULL,
  `piezasxfila` int(2) NOT NULL,
  `filas` int(2) NOT NULL,
  `niveles` int(2) NOT NULL,
  `canastas` int(2) NOT NULL,
  `niveles_completos` int(2) NOT NULL,
  `filas_completas` int(2) NOT NULL,
  `cambio_mog` int(5) NOT NULL,
  `sobrante` int(5) NOT NULL,
  `cantidad_piezas_buenas` int(5) NOT NULL,
  `sobrante_final` int(5) NOT NULL,
  `catalogo_empleados_id_empleado` int(5) NOT NULL,
  `tiempos_id_tiempos` int(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `piezas_procesadas`
--

INSERT INTO `piezas_procesadas` (`id_piezas_procesadas`, `rango_canasta_1`, `rango_canasta_2`, `cantidad_piezas_procesadas`, `sobrante_inicial`, `piezasxfila`, `filas`, `niveles`, `canastas`, `niveles_completos`, `filas_completas`, `cambio_mog`, `sobrante`, `cantidad_piezas_buenas`, `sobrante_final`, `catalogo_empleados_id_empleado`, `tiempos_id_tiempos`) VALUES
(4, 1, 10, 20000, 0, 10, 10, 10, 10, 10, 10, 200, 0, 19000, 0, 2, 149);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `piezas_x_hora`
--

CREATE TABLE `piezas_x_hora` (
  `id_piezas_x_hora` int(11) NOT NULL,
  `hora` varchar(8) NOT NULL,
  `piezas_x_hora` int(4) NOT NULL,
  `acumulado` int(5) NOT NULL,
  `ok_ng` varchar(2) NOT NULL,
  `das_id_das` int(8) NOT NULL,
  `catalogo_empleados_id_empleado` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `piezas_x_hora`
--

INSERT INTO `piezas_x_hora` (`id_piezas_x_hora`, `hora`, `piezas_x_hora`, `acumulado`, `ok_ng`, `das_id_das`, `catalogo_empleados_id_empleado`) VALUES
(32, '13:56', 1222, 1222, 'OK', 154, 2),
(33, '08:15', 4, 4, 'OK', 163, 2),
(34, '08:26', 0, 0, 'OK', 164, 2),
(35, '08:28', 0, 0, 'OK', 165, 2),
(36, '11:39', 5925, 5925, 'OK', 166, 2),
(37, '11:54', 7000, 7000, 'OK', 167, 2),
(38, '11:51', 3000, 3000, 'OK', 173, 2),
(39, '11:54', 17000, 20000, 'OK', 173, 2),
(40, '12:00', 10000, 10000, 'OK', 174, 2),
(41, '08:00', 233, 233, 'OK', 175, 2),
(42, '08:39', 2, 2, 'OK', 177, 2),
(43, '09:25', 10000, 10000, 'OK', 178, 2),
(44, '10:04', 1000, 1000, 'OK', 179, 2),
(45, '10:09', 1000, 1000, 'NG', 180, 2),
(46, '08:32', 5000, 5000, 'NG', 182, 2),
(47, '08:33', -3000, 2000, 'OK', 182, 2),
(48, '08:38', 8000, 10000, 'OK', 182, 2),
(49, '08:40', 4500, 14500, 'NG', 182, 2),
(50, '09:17', 3200, 3200, 'OK', 183, 2),
(51, '14:10', 1000, 1000, 'OK', 188, 2),
(52, '07:31', 8000, 8000, 'OK', 214, 2),
(53, '10:35', 1122, 1122, 'OK', 217, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registros_rbp`
--

CREATE TABLE `registros_rbp` (
  `id_registro_rbp` int(11) NOT NULL,
  `orden_manufactura` varchar(13) NOT NULL,
  `lote` varchar(45) DEFAULT NULL,
  `catalogo_procesos_id_catalogo_proceso` int(3) NOT NULL,
  `mog_id_mog` int(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `registros_rbp`
--

INSERT INTO `registros_rbp` (`id_registro_rbp`, `orden_manufactura`, `lote`, `catalogo_procesos_id_catalogo_proceso`, `mog_id_mog`) VALUES
(4, 'HBL041567', 'GER', 1, 4),
(5, 'HBL051467', '1555', 1, 5),
(6, 'HBL040234', 'SDA', 1, 6),
(7, 'HBL056746', '123', 1, 7),
(8, 'HBL056745', NULL, 1, 8),
(9, 'HBL056476', NULL, 1, 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_causas_paro`
--

CREATE TABLE `registro_causas_paro` (
  `id_registro_causas_paro` int(11) NOT NULL,
  `tiempo_paro` int(3) NOT NULL,
  `detalle` varchar(200) NOT NULL,
  `hora_inicio` varchar(8) NOT NULL,
  `hora_fin` varchar(8) NOT NULL,
  `fecha` date NOT NULL,
  `id_operador` int(5) NOT NULL,
  `das_id_das` int(8) NOT NULL,
  `catalogo_causas_paro_id_catalogo_causas_paro` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `registro_causas_paro`
--

INSERT INTO `registro_causas_paro` (`id_registro_causas_paro`, `tiempo_paro`, `detalle`, `hora_inicio`, `hora_fin`, `fecha`, `id_operador`, `das_id_das`, `catalogo_causas_paro_id_catalogo_causas_paro`) VALUES
(10, 33, 'el taiso', '09:29', '10:02', '2025-01-30', 2, 179, 1),
(11, 38, 'Desayuno', '09:25', '10:03', '2025-02-04', 2, 184, 1),
(12, 17, 'casxzazxczxcxzzc', '08:52', '09:09', '2025-02-13', 2, 204, 6),
(13, 227, 'YHHH', '07:25', '11:12', '2025-02-19', 2, 210, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_razon_rechazo`
--

CREATE TABLE `registro_razon_rechazo` (
  `id_registro_scrap` int(11) NOT NULL,
  `cantidad_scrap` int(5) NOT NULL,
  `turno` int(2) NOT NULL,
  `columna_sorting` int(11) DEFAULT NULL,
  `registros_rbp_id_registro_rbp` int(8) NOT NULL,
  `catalogo_razon_rechazo_id_catalogo_razon_rechazo` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tiempos`
--

CREATE TABLE `tiempos` (
  `id_tiempos` int(8) NOT NULL,
  `turno` int(2) DEFAULT NULL,
  `linea` varchar(10) NOT NULL,
  `fecha` date NOT NULL,
  `hora_inicio` varchar(8) NOT NULL,
  `hora_fin` varchar(8) DEFAULT NULL,
  `horas_trabajadas` varchar(8) DEFAULT NULL,
  `registros_rbp_id_registro_rbp` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tiempos`
--

INSERT INTO `tiempos` (`id_tiempos`, `turno`, `linea`, `fecha`, `hora_inicio`, `hora_fin`, `horas_trabajadas`, `registros_rbp_id_registro_rbp`) VALUES
(101, NULL, 'TH-17', '2024-11-28', '10:52', NULL, NULL, 4),
(102, NULL, 'TH-07', '2024-11-28', '11:21', NULL, NULL, 5),
(103, NULL, 'TH-07', '2024-11-28', '11:31', NULL, NULL, 5),
(104, NULL, 'TH-07', '2024-11-28', '11:33', NULL, NULL, 5),
(105, NULL, 'TH-07', '2024-11-28', '11:39', NULL, NULL, 5),
(106, NULL, 'TH-07', '2024-11-28', '11:40', NULL, NULL, 5),
(107, NULL, 'TH-07', '2024-11-28', '11:44', NULL, NULL, 5),
(108, NULL, 'TH-07', '2024-11-28', '11:46', NULL, NULL, 5),
(109, NULL, 'TH-07', '2024-11-28', '11:50', NULL, NULL, 5),
(110, NULL, 'TH-07', '2024-11-28', '11:53', NULL, NULL, 5),
(111, NULL, 'TH-07', '2024-11-28', '12:46', NULL, NULL, 5),
(112, NULL, 'TH-07', '2024-11-28', '12:46', NULL, NULL, 5),
(113, NULL, 'TH-07', '2024-11-28', '12:49', NULL, NULL, 5),
(114, NULL, 'TH-07', '2024-11-28', '12:51', NULL, NULL, 5),
(115, NULL, 'TH-07', '2024-11-28', '12:51', NULL, NULL, 5),
(116, NULL, 'TH-07', '2024-11-28', '12:52', NULL, NULL, 5),
(117, NULL, 'TH-07', '2024-11-28', '12:53', NULL, NULL, 5),
(118, NULL, 'TH-07', '2024-11-28', '12:54', NULL, NULL, 5),
(119, NULL, 'TH-07', '2024-11-28', '12:56', NULL, NULL, 5),
(120, NULL, 'TH-07', '2024-11-28', '12:57', NULL, NULL, 5),
(121, NULL, 'TH-07', '2024-11-28', '12:58', NULL, NULL, 5),
(122, NULL, 'TH-07', '2024-11-28', '13:01', NULL, NULL, 5),
(123, NULL, 'TH-07', '2024-11-28', '13:04', NULL, NULL, 5),
(124, NULL, 'TH-07', '2024-11-28', '13:07', NULL, NULL, 5),
(125, NULL, 'TH-07', '2024-11-28', '13:08', NULL, NULL, 5),
(126, NULL, 'TH-07', '2024-11-28', '13:10', NULL, NULL, 5),
(127, NULL, 'TH-07', '2024-11-28', '13:11', NULL, NULL, 5),
(128, NULL, 'TH-07', '2024-11-28', '13:13', NULL, NULL, 5),
(129, NULL, 'TH-07', '2024-11-28', '13:14', NULL, NULL, 5),
(130, NULL, 'TH-07', '2024-11-28', '13:15', NULL, NULL, 5),
(131, NULL, 'TH-07', '2024-11-28', '13:17', NULL, NULL, 5),
(132, NULL, 'TH-07', '2024-11-28', '13:21', NULL, NULL, 5),
(133, NULL, 'TH-07', '2024-11-28', '13:23', NULL, NULL, 5),
(134, NULL, 'TH-07', '2024-11-28', '13:26', NULL, NULL, 5),
(135, NULL, 'TH-07', '2024-11-28', '13:29', NULL, NULL, 5),
(136, NULL, 'TH-07', '2024-11-28', '13:31', NULL, NULL, 5),
(137, NULL, 'TH-07', '2024-11-28', '13:33', NULL, NULL, 5),
(138, NULL, 'TH-07', '2024-11-28', '13:34', NULL, NULL, 5),
(139, NULL, 'TH-07', '2024-11-28', '13:37', NULL, NULL, 5),
(140, NULL, 'TH-07', '2024-11-28', '13:39', NULL, NULL, 5),
(141, NULL, 'TH-07', '2024-11-28', '13:40', NULL, NULL, 5),
(142, NULL, 'TH-07', '2024-11-28', '13:41', NULL, NULL, 5),
(143, NULL, 'TH-07', '2024-11-28', '13:42', NULL, NULL, 5),
(144, NULL, 'TH-07', '2024-11-28', '13:44', NULL, NULL, 5),
(145, NULL, 'TH-07', '2024-11-28', '13:45', NULL, NULL, 5),
(146, NULL, 'TH-07', '2024-11-28', '13:46', NULL, NULL, 5),
(147, NULL, 'TH-07', '2024-11-28', '13:47', NULL, NULL, 5),
(148, NULL, 'TH-07', '2024-11-28', '13:54', NULL, NULL, 5),
(149, NULL, 'TH-07', '2024-11-28', '13:55', NULL, NULL, 5),
(150, NULL, 'TH-07', '2024-11-28', '14:03', NULL, NULL, 5),
(151, NULL, 'TH-07', '2024-11-28', '14:10', NULL, NULL, 5),
(152, NULL, 'TH-07', '2024-11-28', '14:15', NULL, NULL, 5),
(153, NULL, 'TH-07', '2024-11-28', '14:17', NULL, NULL, 5),
(154, NULL, 'TH-07', '2024-11-28', '14:18', NULL, NULL, 5),
(155, NULL, 'TH-07', '2024-11-28', '14:24', NULL, NULL, 5),
(156, NULL, 'TH-07', '2024-11-28', '14:28', NULL, NULL, 5),
(157, NULL, 'TH-07', '2024-11-28', '14:29', NULL, NULL, 5),
(158, NULL, 'TH-07', '2024-12-04', '08:12', NULL, NULL, 4),
(159, NULL, 'TH-07', '2024-12-04', '08:26', NULL, NULL, 6),
(160, NULL, 'th-07', '2024-12-04', '08:28', NULL, NULL, 6),
(161, NULL, 'TH-07', '2025-01-22', '11:37', NULL, NULL, 4),
(162, NULL, 'TH-17', '2025-01-22', '11:49', NULL, NULL, 4),
(163, NULL, 'TH-17', '2025-01-28', '08:59', NULL, NULL, 7),
(164, NULL, 'TH-17', '2025-01-28', '09:16', NULL, NULL, 7),
(165, NULL, 'TH-17', '2025-01-28', '09:18', NULL, NULL, 7),
(166, NULL, 'TH-17', '2025-01-28', '09:22', NULL, NULL, 7),
(167, NULL, 'TH-17', '2025-01-28', '10:51', NULL, NULL, 7),
(168, NULL, 'TH-17', '2025-01-28', '11:21', NULL, NULL, 7),
(169, NULL, 'TH-17', '2025-01-28', '11:57', NULL, NULL, 7),
(170, NULL, 'TH-17', '2025-01-30', '07:58', NULL, NULL, 7),
(171, NULL, 'TH-17', '2025-01-30', '08:25', NULL, NULL, 7),
(172, NULL, 'TH-17', '2025-01-30', '08:34', NULL, NULL, 7),
(173, NULL, 'TH-17', '2025-01-30', '09:20', NULL, NULL, 7),
(174, NULL, 'th-17', '2025-01-30', '09:27', NULL, NULL, 7),
(175, NULL, 'TH-17', '2025-01-30', '10:08', NULL, NULL, 7),
(176, NULL, 'TH-17', '2025-01-31', '09:00', NULL, NULL, 7),
(177, NULL, 'TH-17', '2025-02-04', '08:01', NULL, NULL, 7),
(178, NULL, 'TH-17', '2025-02-04', '09:06', NULL, NULL, 7),
(179, NULL, 'TH-17', '2025-02-04', '09:21', NULL, NULL, 7),
(180, NULL, 'TH-17', '2025-02-04', '12:40', NULL, NULL, 7),
(181, NULL, 'TH-17', '2025-02-05', '07:38', NULL, NULL, 7),
(182, NULL, 'TH-17', '2025-02-05', '07:52', NULL, NULL, 7),
(183, NULL, 'TH-17', '2025-02-05', '14:09', NULL, NULL, 7),
(184, NULL, 'TH-17', '2025-02-05', '14:19', NULL, NULL, 7),
(185, NULL, 'TH-17', '2025-02-05', '14:19', NULL, NULL, 8),
(186, NULL, 'TH-17', '2025-02-05', '14:20', NULL, NULL, 7),
(187, NULL, 'TH-17', '2025-02-05', '14:23', NULL, NULL, 7),
(188, NULL, 'TH-17', '2025-02-06', '12:45', NULL, NULL, 9),
(189, NULL, 'TH-17', '2025-02-06', '13:54', NULL, NULL, 9),
(190, NULL, 'TH-17', '2025-02-07', '07:52', NULL, NULL, 9),
(191, NULL, 'TH-17', '2025-02-07', '09:22', NULL, NULL, 9),
(192, NULL, 'TH-17', '2025-02-07', '11:39', NULL, NULL, 9),
(193, NULL, 'TH-17', '2025-02-07', '12:19', NULL, NULL, 9),
(194, NULL, 'TH-17', '2025-02-12', '11:12', NULL, NULL, 7),
(195, NULL, 'TH-17', '2025-02-12', '12:08', NULL, NULL, 7),
(196, NULL, 'th-17', '2025-02-13', '07:49', NULL, NULL, 7),
(197, NULL, 'th-17', '2025-02-13', '08:50', NULL, NULL, 7),
(198, NULL, 'th-17', '2025-02-13', '08:51', NULL, NULL, 7),
(199, NULL, 'th-17', '2025-02-13', '08:51', NULL, NULL, 7),
(200, NULL, 'TH-17', '2025-02-14', '07:42', NULL, NULL, 7),
(201, NULL, 'th-17', '2025-02-14', '09:24', NULL, NULL, 7),
(202, NULL, 'TH-17', '2025-02-18', '12:27', NULL, NULL, 7),
(203, NULL, 'TH-17', '2025-02-18', '12:28', NULL, NULL, 7),
(204, NULL, 'TH-17', '2025-02-18', '14:01', NULL, NULL, 7),
(205, NULL, 'TH-17', '2025-02-19', '07:13', NULL, NULL, 7),
(206, NULL, 'th-17', '2025-02-21', '10:34', NULL, NULL, 7),
(207, NULL, 'TH-17', '2025-02-24', '12:26', NULL, NULL, 7),
(208, NULL, 'TH-17', '2025-02-26', '12:12', NULL, NULL, 7),
(209, NULL, 'TH-17', '2025-02-27', '07:30', NULL, NULL, 7),
(210, NULL, 'TH-17', '2025-02-27', '11:27', NULL, NULL, 7),
(211, NULL, 'TH-17', '2025-02-27', '11:33', NULL, NULL, 7),
(212, NULL, 'TH-17', '2025-02-28', '10:19', NULL, NULL, 7),
(213, NULL, 'TH-17', '2025-03-05', '08:44', NULL, NULL, 7);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `catalogo_categoria_causa_paro`
--
ALTER TABLE `catalogo_categoria_causa_paro`
  ADD PRIMARY KEY (`id_catalogo_categoria_causa_paro`),
  ADD KEY `fk_ catalogo_categoria_causa_paro_catalogo_procesos1` (`catalogo_procesos_id_catalogo_proceso`);

--
-- Indices de la tabla `catalogo_causas_paro`
--
ALTER TABLE `catalogo_causas_paro`
  ADD PRIMARY KEY (`id_catalogo_causas_paro`),
  ADD KEY `fk_catalogo_causas_paro_ catalogo_categoria_causa_paro1` (`catalogo_categoria_causa_paro_id_catalogo_categoria_causa_paro`);

--
-- Indices de la tabla `catalogo_empleados`
--
ALTER TABLE `catalogo_empleados`
  ADD PRIMARY KEY (`id_empleado`),
  ADD UNIQUE KEY `numero_empleado_UNIQUE` (`numero_empleado`),
  ADD KEY `fk_catalogo_empleados_catalogo_roles1` (`catalogo_roles_id_catalogo_roles`),
  ADD KEY `fk_catalogo_empleados_catalogo_procesos1` (`catalogo_procesos_id_catalogo_proceso`);

--
-- Indices de la tabla `catalogo_empleados_has_catalogo_lineas_produccion`
--
ALTER TABLE `catalogo_empleados_has_catalogo_lineas_produccion`
  ADD PRIMARY KEY (`catalogo_empleados_id_empleado`,`catalogo_lineas_produccion_id_catalogo_lineas_produccion`),
  ADD KEY `fk_catalogo_empleados_has_catalogo_lineas_produccion_catalogo2` (`catalogo_lineas_produccion_id_catalogo_lineas_produccion`);

--
-- Indices de la tabla `catalogo_lineas_produccion`
--
ALTER TABLE `catalogo_lineas_produccion`
  ADD PRIMARY KEY (`id_catalogo_lineas_produccion`),
  ADD KEY `fk_catalogo_lineas_produccion_catalogo_procesos1` (`catalogo_procesos_id_catalogo_proceso`);

--
-- Indices de la tabla `catalogo_procesos`
--
ALTER TABLE `catalogo_procesos`
  ADD PRIMARY KEY (`id_catalogo_proceso`);

--
-- Indices de la tabla `catalogo_razon_rechazo`
--
ALTER TABLE `catalogo_razon_rechazo`
  ADD PRIMARY KEY (`id_catalogo_razon_rechazo`),
  ADD KEY `fk_catalogo_razon_rechazo_catalogo_procesos1` (`catalogo_procesos_id_catalogo_proceso`);

--
-- Indices de la tabla `catalogo_roles`
--
ALTER TABLE `catalogo_roles`
  ADD PRIMARY KEY (`id_catalogo_roles`);

--
-- Indices de la tabla `das`
--
ALTER TABLE `das`
  ADD PRIMARY KEY (`id_das`),
  ADD KEY `fk_das_registros_rbp1` (`registros_rbp_id_registro_rbp`);

--
-- Indices de la tabla `das_has_catalogo_empleados`
--
ALTER TABLE `das_has_catalogo_empleados`
  ADD PRIMARY KEY (`das_id_das`),
  ADD KEY `fk_das_has_catalogo_empleados_catalogo_empleados1` (`id_operador`),
  ADD KEY `fk_das_has_catalogo_empleados_catalogo_empleados2` (`id_soporte_rapido`),
  ADD KEY `fk_das_has_catalogo_empleados_catalogo_empleados3` (`id_inspector`);

--
-- Indices de la tabla `empleados_has_registros_rbp`
--
ALTER TABLE `empleados_has_registros_rbp`
  ADD PRIMARY KEY (`registros_rbp_id_registro_rbp`),
  ADD KEY `fk_empleados_has_registros_rbp_empleados` (`id_operador`),
  ADD KEY `fk_empleados_has_registros_rbp_empleados2` (`id_supervisor`);

--
-- Indices de la tabla `estatus_rbp`
--
ALTER TABLE `estatus_rbp`
  ADD PRIMARY KEY (`id_estatus_rbp`),
  ADD KEY `fk_estatus_rbp_registros_rbp1` (`registros_rbp_id_registro_rbp`);

--
-- Indices de la tabla `mog`
--
ALTER TABLE `mog`
  ADD PRIMARY KEY (`id_mog`),
  ADD KEY `fk_mog_numero_parte1` (`numero_parte_id_numero_parte`);

--
-- Indices de la tabla `numero_parte`
--
ALTER TABLE `numero_parte`
  ADD PRIMARY KEY (`id_numero_parte`);

--
-- Indices de la tabla `ordenes_cerradas`
--
ALTER TABLE `ordenes_cerradas`
  ADD PRIMARY KEY (`id_ordenes_cerradas`),
  ADD KEY `fk_ordenes_cerradas_registros_rbp1` (`registros_rbp_id_registro_rbp`);

--
-- Indices de la tabla `piezas_procesadas`
--
ALTER TABLE `piezas_procesadas`
  ADD PRIMARY KEY (`id_piezas_procesadas`),
  ADD KEY `fk_piezas_procesadas_catalogo_empleados1` (`catalogo_empleados_id_empleado`),
  ADD KEY `fk_registro_time_tiempos` (`tiempos_id_tiempos`);

--
-- Indices de la tabla `piezas_x_hora`
--
ALTER TABLE `piezas_x_hora`
  ADD PRIMARY KEY (`id_piezas_x_hora`),
  ADD KEY `fk_piezas_x_hora_das1` (`das_id_das`),
  ADD KEY `fk_piezas_x_hora_catalogo_empleados1` (`catalogo_empleados_id_empleado`);

--
-- Indices de la tabla `registros_rbp`
--
ALTER TABLE `registros_rbp`
  ADD PRIMARY KEY (`id_registro_rbp`),
  ADD KEY `fk_registros_rbp_catalogo_procesos1` (`catalogo_procesos_id_catalogo_proceso`),
  ADD KEY `fk_registros_rbp_mog1` (`mog_id_mog`);

--
-- Indices de la tabla `registro_causas_paro`
--
ALTER TABLE `registro_causas_paro`
  ADD PRIMARY KEY (`id_registro_causas_paro`),
  ADD KEY `fk_registro_causas_paro_das1` (`das_id_das`),
  ADD KEY `fk_registro_causas_paro_catalogo_causas_paro1` (`catalogo_causas_paro_id_catalogo_causas_paro`);

--
-- Indices de la tabla `registro_razon_rechazo`
--
ALTER TABLE `registro_razon_rechazo`
  ADD PRIMARY KEY (`id_registro_scrap`),
  ADD KEY `fk_registro_scrap_registros_rbp1` (`registros_rbp_id_registro_rbp`),
  ADD KEY `fk_registro_razon_rechazo_catalogo_razon_rechazo1` (`catalogo_razon_rechazo_id_catalogo_razon_rechazo`);

--
-- Indices de la tabla `tiempos`
--
ALTER TABLE `tiempos`
  ADD PRIMARY KEY (`id_tiempos`),
  ADD KEY `fk_tiempos_registros_rbp1` (`registros_rbp_id_registro_rbp`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `catalogo_categoria_causa_paro`
--
ALTER TABLE `catalogo_categoria_causa_paro`
  MODIFY `id_catalogo_categoria_causa_paro` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `catalogo_causas_paro`
--
ALTER TABLE `catalogo_causas_paro`
  MODIFY `id_catalogo_causas_paro` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `catalogo_empleados`
--
ALTER TABLE `catalogo_empleados`
  MODIFY `id_empleado` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `catalogo_lineas_produccion`
--
ALTER TABLE `catalogo_lineas_produccion`
  MODIFY `id_catalogo_lineas_produccion` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `catalogo_procesos`
--
ALTER TABLE `catalogo_procesos`
  MODIFY `id_catalogo_proceso` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `catalogo_razon_rechazo`
--
ALTER TABLE `catalogo_razon_rechazo`
  MODIFY `id_catalogo_razon_rechazo` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT de la tabla `catalogo_roles`
--
ALTER TABLE `catalogo_roles`
  MODIFY `id_catalogo_roles` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `das`
--
ALTER TABLE `das`
  MODIFY `id_das` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=219;

--
-- AUTO_INCREMENT de la tabla `estatus_rbp`
--
ALTER TABLE `estatus_rbp`
  MODIFY `id_estatus_rbp` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `mog`
--
ALTER TABLE `mog`
  MODIFY `id_mog` int(7) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `numero_parte`
--
ALTER TABLE `numero_parte`
  MODIFY `id_numero_parte` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `ordenes_cerradas`
--
ALTER TABLE `ordenes_cerradas`
  MODIFY `id_ordenes_cerradas` int(8) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `piezas_procesadas`
--
ALTER TABLE `piezas_procesadas`
  MODIFY `id_piezas_procesadas` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `piezas_x_hora`
--
ALTER TABLE `piezas_x_hora`
  MODIFY `id_piezas_x_hora` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT de la tabla `registros_rbp`
--
ALTER TABLE `registros_rbp`
  MODIFY `id_registro_rbp` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `registro_causas_paro`
--
ALTER TABLE `registro_causas_paro`
  MODIFY `id_registro_causas_paro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `registro_razon_rechazo`
--
ALTER TABLE `registro_razon_rechazo`
  MODIFY `id_registro_scrap` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de la tabla `tiempos`
--
ALTER TABLE `tiempos`
  MODIFY `id_tiempos` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=214;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `catalogo_categoria_causa_paro`
--
ALTER TABLE `catalogo_categoria_causa_paro`
  ADD CONSTRAINT `fk_ catalogo_categoria_causa_paro_catalogo_procesos1` FOREIGN KEY (`catalogo_procesos_id_catalogo_proceso`) REFERENCES `catalogo_procesos` (`id_catalogo_proceso`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `catalogo_causas_paro`
--
ALTER TABLE `catalogo_causas_paro`
  ADD CONSTRAINT `fk_catalogo_causas_paro_ catalogo_categoria_causa_paro1` FOREIGN KEY (`catalogo_categoria_causa_paro_id_catalogo_categoria_causa_paro`) REFERENCES `catalogo_categoria_causa_paro` (`id_catalogo_categoria_causa_paro`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `catalogo_empleados`
--
ALTER TABLE `catalogo_empleados`
  ADD CONSTRAINT `fk_catalogo_empleados_catalogo_procesos1` FOREIGN KEY (`catalogo_procesos_id_catalogo_proceso`) REFERENCES `catalogo_procesos` (`id_catalogo_proceso`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_catalogo_empleados_catalogo_roles1` FOREIGN KEY (`catalogo_roles_id_catalogo_roles`) REFERENCES `catalogo_roles` (`id_catalogo_roles`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `catalogo_empleados_has_catalogo_lineas_produccion`
--
ALTER TABLE `catalogo_empleados_has_catalogo_lineas_produccion`
  ADD CONSTRAINT `fk_catalogo_empleados_has_catalogo_lineas_produccion_catalogo1` FOREIGN KEY (`catalogo_empleados_id_empleado`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_catalogo_empleados_has_catalogo_lineas_produccion_catalogo2` FOREIGN KEY (`catalogo_lineas_produccion_id_catalogo_lineas_produccion`) REFERENCES `catalogo_lineas_produccion` (`id_catalogo_lineas_produccion`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `catalogo_lineas_produccion`
--
ALTER TABLE `catalogo_lineas_produccion`
  ADD CONSTRAINT `fk_catalogo_lineas_produccion_catalogo_procesos1` FOREIGN KEY (`catalogo_procesos_id_catalogo_proceso`) REFERENCES `catalogo_procesos` (`id_catalogo_proceso`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `catalogo_razon_rechazo`
--
ALTER TABLE `catalogo_razon_rechazo`
  ADD CONSTRAINT `fk_catalogo_razon_rechazo_catalogo_procesos1` FOREIGN KEY (`catalogo_procesos_id_catalogo_proceso`) REFERENCES `catalogo_procesos` (`id_catalogo_proceso`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `das`
--
ALTER TABLE `das`
  ADD CONSTRAINT `fk_das_registros_rbp1` FOREIGN KEY (`registros_rbp_id_registro_rbp`) REFERENCES `registros_rbp` (`id_registro_rbp`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `das_has_catalogo_empleados`
--
ALTER TABLE `das_has_catalogo_empleados`
  ADD CONSTRAINT `fk_das_has_catalogo_empleados_catalogo_empleados1` FOREIGN KEY (`id_operador`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_das_has_catalogo_empleados_catalogo_empleados2` FOREIGN KEY (`id_soporte_rapido`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_das_has_catalogo_empleados_catalogo_empleados3` FOREIGN KEY (`id_inspector`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_das_has_catalogo_empleados_das1` FOREIGN KEY (`das_id_das`) REFERENCES `das` (`id_das`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `empleados_has_registros_rbp`
--
ALTER TABLE `empleados_has_registros_rbp`
  ADD CONSTRAINT `fk_empleados_has_registros_rbp_empleados` FOREIGN KEY (`id_operador`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_empleados_has_registros_rbp_empleados2` FOREIGN KEY (`id_supervisor`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_empleados_has_registros_rbp_registros_rbp1` FOREIGN KEY (`registros_rbp_id_registro_rbp`) REFERENCES `registros_rbp` (`id_registro_rbp`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `estatus_rbp`
--
ALTER TABLE `estatus_rbp`
  ADD CONSTRAINT `fk_estatus_rbp_registros_rbp1` FOREIGN KEY (`registros_rbp_id_registro_rbp`) REFERENCES `registros_rbp` (`id_registro_rbp`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `mog`
--
ALTER TABLE `mog`
  ADD CONSTRAINT `fk_mog_numero_parte1` FOREIGN KEY (`numero_parte_id_numero_parte`) REFERENCES `numero_parte` (`id_numero_parte`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `ordenes_cerradas`
--
ALTER TABLE `ordenes_cerradas`
  ADD CONSTRAINT `fk_ordenes_cerradas_registros_rbp1` FOREIGN KEY (`registros_rbp_id_registro_rbp`) REFERENCES `registros_rbp` (`id_registro_rbp`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `piezas_procesadas`
--
ALTER TABLE `piezas_procesadas`
  ADD CONSTRAINT `fk_piezas_procesadas_catalogo_empleados1` FOREIGN KEY (`catalogo_empleados_id_empleado`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_registro_time_tiempos` FOREIGN KEY (`tiempos_id_tiempos`) REFERENCES `tiempos` (`id_tiempos`);

--
-- Filtros para la tabla `piezas_x_hora`
--
ALTER TABLE `piezas_x_hora`
  ADD CONSTRAINT `fk_piezas_x_hora_catalogo_empleados1` FOREIGN KEY (`catalogo_empleados_id_empleado`) REFERENCES `catalogo_empleados` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_piezas_x_hora_das1` FOREIGN KEY (`das_id_das`) REFERENCES `das` (`id_das`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `registros_rbp`
--
ALTER TABLE `registros_rbp`
  ADD CONSTRAINT `fk_registros_rbp_catalogo_procesos1` FOREIGN KEY (`catalogo_procesos_id_catalogo_proceso`) REFERENCES `catalogo_procesos` (`id_catalogo_proceso`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_registros_rbp_mog1` FOREIGN KEY (`mog_id_mog`) REFERENCES `mog` (`id_mog`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `registro_causas_paro`
--
ALTER TABLE `registro_causas_paro`
  ADD CONSTRAINT `fk_registro_causas_paro_catalogo_causas_paro1` FOREIGN KEY (`catalogo_causas_paro_id_catalogo_causas_paro`) REFERENCES `catalogo_causas_paro` (`id_catalogo_causas_paro`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_registro_causas_paro_das1` FOREIGN KEY (`das_id_das`) REFERENCES `das` (`id_das`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `registro_razon_rechazo`
--
ALTER TABLE `registro_razon_rechazo`
  ADD CONSTRAINT `fk_registro_razon_rechazo_catalogo_razon_rechazo1` FOREIGN KEY (`catalogo_razon_rechazo_id_catalogo_razon_rechazo`) REFERENCES `catalogo_razon_rechazo` (`id_catalogo_razon_rechazo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_registro_scrap_registros_rbp1` FOREIGN KEY (`registros_rbp_id_registro_rbp`) REFERENCES `registros_rbp` (`id_registro_rbp`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `tiempos`
--
ALTER TABLE `tiempos`
  ADD CONSTRAINT `fk_tiempos_registros_rbp1` FOREIGN KEY (`registros_rbp_id_registro_rbp`) REFERENCES `registros_rbp` (`id_registro_rbp`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
