# Configuración del marco de trabajo

## BPEL

1. `Help` > `Instal new software` > `Add`
2. Bpel site: http://download.eclipse.org/bpel/site/1.0.5
3. Seleccionar todos los items que aparezcan en la ventana `Available software to install` > `Eclipse BPEL Designer` e instalarlos.

## ODE

1. Nos [descargamos el desplegable `ode.war`](http://www.apache.org/dyn/closer.cgi/ode/apache-ode-war-1.3.6.zip) y lo ubicamos en `webapps` de la distribución de Tomcat que tengamos instalada en nuestro disco duro.
2. Abrimos la perspectiva `Servers` (`Windows` > `Show View` > `Servers`)
3. Sobre la nueva perspectiva, botón derecho > `New` > `Server`.
4. Seleccionamos `Ode v1.x.server`, dentro del paquete Apache, y lo configuramos con la siguiente información:
	- JRE: `java-8-oracle`
	- ODE's home directory: `/opt/apache-tomcat-9.0.12/webapps/ode`
	- Tomcat's home directory: `/opt/apache-tomcat-9.0.12`
	- Server address: `localhost`
	- Port: `8080`
	- VM Arguments: `-Xms128m -Xmx512m -XX:MaxPermSize=256m`
5. Clicando dos veces sobre su nombre seleccionamos `Open launch cofiguration` y accediendo a la pestaña `Classpath` > `Add External JARs` añadimos el archivo `tomcat-juli.jar`, ubicando en `/opt/apache-tomcat-9.0.12/bin`, debajo de `User entries`.

# Despliegue de un proceso en BPEL

1. Creación de un proyecto BPEL nuevo: `File` > `New` > `Other` > `BPEL 2.0` > `BPEL Project`, llamado `ODE_Prueba` y seleccionamos como `Target runtime` objetivo a `Apache ODE 1.x` y su configuración por defecto. 

2. Sobre la carpeta `ODE_Prueba/bpelContent`, seleccionamos del menú `File` > `New` > `Other` > `BPEL 2.0` > `New BPEL process file`: 
	- Creation Mode: `Create a BPEL process from template`
	- Proces Name: `HolaMundo`
	- Namespace: `http://holaMundo`

	Seguidamente configuramos:
	- Template: `Synchronous BPEL Process`
	- Service Name: `HolaMundoService`
	- Port Name: `HolaMundoPort`
	- Service Address: `http://localhost:8080/ode/processes/HolaMundo`
	- Binding Protocol: `SOAP`
	
3. En el archivo `HolaMundo.bpel` arrastrando añadimos la acción `Assign` que se encuentra dentro del apartado `Actions` de la pestaña `Palette`, uniéndolo a la acción `receiveInput` sobre el gráfico del proceso BPEL y luego eliminamos el cuadrado `FIX_ME-Add_Business_Logic_Here` del gráfico.

4. Seleccionamos la actividad `Assign` con el botón derecho del ratón y seleccionamos `Show in properties` del menú desplegable que aparece. En la pestaña de `Properties`, seleccionamos `Details` y clicamos en el botón `New` para poder definir un operador de asignación (`Copy from: to:`) de BPEL. Seleccionando en las ventanas (_from_ `input` > `payload` > `input` _to_ `output` > `payload` > `result`). Por último, a la pregunta de inicialización de la variable le respondemos `yes`.

5. Ahora hay que proporcionar un _puerto_ y una _ligadura_ (_"binding"_) para comunicar con el servicio. Si clicamos dos veces en el archivo `HolaMundoArtifacts.wsdl` que nos muestra el navegador de proyectos, dentro de la carpeta `bpelContent`, aparecerá el _editor de WSDL_.

	Seleccionamos `Show Properties` en el menú que nos aparece con el botón derecho sobre `HolaMundoService`. Clicando en `HolaMundoPort` aparece un menú desplegable que se abre:
	- Name: `HolaMundoPort`
	- Binding: `HolaMundoBinding`
	- Address: `http://localhost:8080/ode/processes/HolaMundo`
	- Protocol: `SOAP`
	
6. Para poder desplegar este SW clicamos en la carpeta `bpelContent` y seleccionamos `File` > `New` > `Others` > `BPEL 2.0` > `BPEL Deployment Descriptor`. Ahora hacemos doble clic en el archivo `deploy.xml`, abriéndonos el editor `ODE Deployment Descriptor Editor`.

	El editor de despliegue BPEL nos mostrará la tabla denominada _Inbound Interfaces_. Esta tabla contiene las interfaces que proporciona el proceso, especificando el _servicio_, _puerto_ y la _ligadura_ que queremos utilizar para cada `PartnerLink` que aparece en las filas de esta tabla. En este ejemplo el _Partner Link_ se llamará `client`.

	Clicaremos en el campo debajo de la columna _Associated Port_ de la tabla anterior y nos aparecerá un cursor de despliegue para seleccionar `HolaMundoPort`. Finalmente, clicaremos en _Related Service_ y se rellenará automáticamente, junto con los otros campos de la fila.

7. Para la ejecución del servicio, nos vamos a la vsta `Servers` de nuestro IDE y arrancamos el servidor ODE y con el botón derecho seleccionamos `Add and Remove`, apareciéndonos un menú partido con `ODE_Prueba` en la columna izquierda (_Available_) que añadiremos a la columna derecha (_Configured_). Si apareciese una ventana de notificación o si el menú partido tuviera su columna izquierda vacía, hay que cerrar el runtime de Eclipse y volver a configurar el servidor _Ode v1.x_, a través del enlace _launch and configuration_ de su menú de configuración. Por último, seleccionamos _Add y Finish_.

	Si escribimos la dirección http://localhost:8080/ode/ en el navegador y seleccionamos el enlace `Deployment Browser` > `Process Services` (http://localhost:8080/ode/deployment/services/), aparecerá la lista de los servicios instalados en el puerto 8080 de nuestra máquina local.

8. Por último, seleccionamos con el botón derecho el archivo `HolaMundoArtifacts.wsdl` > `Web Services` > `Test with Web Services Explorer` y entonces aparecerá el explorador de servicios Web de Eclipse. En la parte izquierda del navegador, hay que expandir el árbol debajo de `file://.../ODE_Prueba/bpelContent/HolaMundoArtifacts.wsdl` hasta que se muestra `process`.

	Ahora hay que clicar en el enlace `process` de la tabla `Operations` para que nos aparezca un cuadro de texto de sustitución para los parámetros de la operación cuyo comportamiento queramos probar y poder llamarla o especificar puntos finales (_endpoints_) adicionales. Aparecerá la captura de pantalla donde escribimos el mensaje "¡Hola ODE!" en la ventana de texto y pulsamos el botón _Go_, entonces nos ha de aparecer el resultado de la ejecución de este servicio simple en la ventana inferior.
