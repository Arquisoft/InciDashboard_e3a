# InciDashboard_e3a
InciDashboard_e3a

[![Build Status](https://travis-ci.org/Arquisoft/InciDashboard_e3a.svg?branch=master)](https://travis-ci.org/Arquisoft/InciDashboard_e3a)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e89ecf2799b8400580f767eb000d0380)](https://www.codacy.com/app/jelabra/InciDashboard_e3a?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Arquisoft/InciDashboard_e3a&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/Arquisoft/InciDashboard_e3a/branch/master/graph/badge.svg)](https://codecov.io/gh/Arquisoft/InciDashboard_e3a)


## Índice

- [Introducción al proyecto](#incidashboard_e3a)
- [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
    - [Detalles](#detalles)	     
    - [Instrucciones](#instrucciones)	  
 - [Autores](#autores)	
 
 
 ## Cómo ejecutar el proyecto

### Detalles

Funciona con una base de datos MongoDB.

Por tanto, para probar hay que tener funcionando:

1. Desplegar [Kafka](https://kafka.apache.org/quickstart). Para lo que hay que arrancar primero Apache Zookeeper y después Apache Kafka, de esta forma ejecutaremos desde el directorio apache-kafka primero:
		
		* Linux/Mac: bin/zookeeper-server-start.sh config/zookeeper.properties
		* Windows: bin\windows\zookeeper-server-start.bat config\zookeeper.properties

	y después:

		* Linux/Mac: bin/kafka-server-start.sh config/server.properties
   		* Windows: bin\windows\kafka-server-start.bat config\server.properties

2. Lanzar este módulo.

### Instrucciones



# Autores

- Álvaro Cabrero Barros (@espiguinho)
- Saúl Castillo Valdés (@saulcasti)
- Pelayo Díaz Soto (@PelayoDiaz)
- Amelia Fernández Braña (@ameliafb)
- Francisco Javier Riedemann Wistuba (@FJss23)



