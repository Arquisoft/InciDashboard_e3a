# language: es
Característica: Ser capaz de cargar incidencias

	Como operario registrado en el sistema quiero poder cargar las incidencias.

	Escenario: Redireccion correcta
		Dado un operario de nombre "Id1" y contraseña "123456" registrado en el sistema
		Y situado en la página "/login"
		Cuando hago login con usuario "Id1" y password "123456"  introduciendo los datos en los campos
		Y voy a la página "/inci/cargar/"
		Entonces soy redireccionado a la página "/users/operario"