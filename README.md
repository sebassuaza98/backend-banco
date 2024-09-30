## Especificaciones técnicas del proyecto:

- Java: 17 Spring Boot: 3.3.0 Maven: 3.0 BD: Mongo 
- Editor de texto (VSCode) o IDE preferido Docker Configuración de Docker y Maven:

## Docker:
 El proyecto backend se encuentra desplegado en un contenedor Docker. 
## Comandos: 

# Construye la imágene en el archivo docker-compose.yml
`docker-compose build`
# Inicia los contenedor
`docker-compose up`

## El repositorio Git está a continuación: https://github.com/sebassuaza98/backend-ecommers.git

Nota: Asegúrate de tener Docker instalado y en funcionamiento.

## Si no se tiene docker, se puede ejecurar de forma local, una vez clonado el repositorio 
- Abre el directorio raíz del proyecto con el IDE de preferencia
- Ejecutar el comando `mvn install` para instalar las dependencias.

## Ejecucion de la aplicacion 
- Ejecutar `mvn spring-boot:run` en la raiz del proyecto.
El puerto de ejecucion es el : `http://localhost:8080`


## Nota: El backend cuenta con pruebas unitarias. Para ejecutarlas, usa el comando:
`mvn test` 


## Manual de usuario: Se adjunta en el correo electrónico el manual de usuario sobre cómo consumir las peticiones.
- Peticiones HTTP a los endpoints proporcionados en el manual de usuario para interactuar con el backend.


## FLUJO CI/CD
Para ello se usará CodePipeline, que es un servicio de AWS para publicar software mediante entrega continua.

## 1.1 Crear un pipeline para el backend
- Entro en la consola de AWS CodePipeline.
- Hago click en Create canalizacion.
- Nombro el pipeline, por ejemplo: backend-banco-pipeline.
* Fuente de código:
* Selecciona GitHub.
- Autentifico la cuenta de GitHub, este caso seria mi repo `https://github.com/sebassuaza98`
- Selecciono el repositorio backend-banco y la rama principal (main).

## Paso 2: Configuración de CodeBuild para Backend (Java/Maven)
CodeBuild: Hara la compilacion.

## 2.1 Crear un proyecto en CodeBuild
- Se ingreso en AWS CodeBuild y selecciona Crear proyecto.
Nombra el proyecto, por ejemplo: backend-build-banco.
- Fuente de código: GitHub  `https://github.com/sebassuaza98`

## Entorno de compilación:
* Selecciono el  entorno Ubuntu.
* Elijo la imagen gestionada: aws/codebuild/standard:6.0 
* Configuro la versión de Java a 17 y la de Maven a 3.8.4 

## 2.2 crear  el archivo:  buildspec file: maneja el proceso de compilación
- Uso el archivo buildspec.yml 


## 3 Integrar CodeBuild con CodePipeline
En CodePipeline, agrega el proyecto creado, para poder compilarlo.

## Paso 1: Crear un Proyecto de CodeBuild
Antes de integrar, se creo el  proyecto en CodeBuild para compilar el backend. Aquí están los pasos para ello:

## Acceder a AWS CodeBuild:

- Abro la  consola de AWS y busca CodeBuild.
- Creo un nuevo proyecto: banco-build

* Hago click en Crear proyecto.
Nombre del Proyecto: Asigna un nombre.

* Configuración de la fuente:
- GitHub  `https://github.com/sebassuaza98`
Repository: Selecciona tu repositorio backend-banco.
Branch: (main).

## Entorno de Compilación:

- Environment image: Managed image.
- Operating system: Ubuntu.
- Runtime:  Standard.
- Image: Se escoge uan que tenga lam imagen de Java 17, por ejemplo: aws/codebuild/standard:6.0.
- Environment variables: Se agregan variables de entorno de ser necesario.

## Configuración de Buildspec:
- El archivo esta creado en la raiz de proyecto o se puede usar la de por defecto.

## Configuración de Artifacts:
- Aca es donde de guarda  el JAR generado en un
* Artifacts: bucket de S3.
* Role de servicio:
- Se puede usar uno  existente o crea uno nuevo que permita a CodeBuild ejecutar las tareas necesarias.

## Finalmente se crea desde la consola de aws

## Paso 2: Integrar CodeBuild con CodePipeline
- Ya configurado el CodeBuild, vamos a integrarlo con CodePipeline.

* Acceder a AWS CodePipeline:

* Vamos a la consola de AWS y se busca CodePipeline.
- Seleccionamos el  el Pipeline: ´backend-banco-pipeline´

* Agrego una Etapa de Compilación:
- En la vista de detalles del pipeline, se hace click en el botón Edit.
- Al hacer click en + Add stage y nombra esta etapa como Build.

*Configurar la Etapa de Compilación:

- Hago clic en + Add action group dentro de la etapa de Build.
- Action name: Asigna un nombre a la acción.
- Action provider: Selecciona AWS CodeBuild.
- Region: Selecciona la región donde se encuentra tu proyecto de CodeBuild.
- Project name: Selecciona el proyecto que creaste anteriormente (banco-build).

* Configuración de Artifacts:
- En Output artifacts, proporciona un nombre para el artefacto de salida, como BuildOutput. Este será el artefacto que se pasará a la siguiente etapa del pipeline (despliegue).

## Guardar cambios.

## Paso 4: Despliegue
- Backend: se despliega en Elastic Beanstalk
- Elastic Beanstalk puede desplegar automáticamente tu backend en una instancia de EC2 con Java.
- En Elastic Beanstalk, crea una aplicación y un entorno de despliegue de Java.
- En CodePipeline, agrega una etapa de despliegue que suba el archivo .jar generado por CodeBuild a -Elastic Beanstalk.




