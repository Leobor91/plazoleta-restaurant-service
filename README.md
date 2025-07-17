# plazoleta-restaurant-service

# 🍽️ Servicio de Restaurante (RestauranteService)

Este microservicio forma parte del sistema de **Plazoleta de Comidas** y es responsable de la gestión de la información de los restaurantes, incluyendo su creación, consulta y asociación con usuarios propietarios.

Está diseñado siguiendo los principios de la Arquitectura Hexagonal (Puertos y Adaptadores) para asegurar una alta cohesión, bajo acoplamiento y facilidad de prueba.

## 🚀 Tecnologías Utilizadas

* **Java 17:** Lenguaje de programación.
* **Spring Boot 3.x.x:** Framework para el desarrollo rápido de microservicios.
* **Spring Data JPA:** Para la persistencia de datos y la interacción con la base de datos.
* **Spring WebFlux (WebClient):** Para la comunicación no bloqueante con servicios externos (ej. Servicio de Autenticación).
* **Gradle:** Herramienta de automatización de construcción.
* **Lombok:** Para reducir el boilerplate code (constructores, getters, setters, etc.).
* **MySQL:** Base de datos relacional para almacenar la información de los restaurantes.
* **Springdoc OpenAPI (Swagger UI):** Para la generación automática y visualización interactiva de la documentación de la API.
* **Jacoco:** Para generar reportes de cobertura de código.

## 🧩 Arquitectura

El servicio sigue la **Arquitectura Hexagonal**, separando claramente la lógica de negocio (dominio) de los detalles de infraestructura (persistencia, comunicación externa, exposición API).

* **`domain/`**: Contiene la lógica de negocio central (modelos, puertos de entrada `api`, puertos de salida `spi` ).
* **`application/`**: Capa de aplicación, incluye DTOs (Data Transfer Objects) y mappers para la comunicación entre la infraestructura y el dominio, así como los manejadores de comandos y queries.
* **`infrastructure/`**: Implementaciones de los puertos, tanto de entrada (`input/rest` para controladores REST) como de salida (`output/jpa` para adaptadores de persistencia JPA, `output/adapter` para adaptadores de servicios externos).
* **`config/`**: Clases de configuración de Spring.

## ⚙️ Configuración del Entorno Local

Para levantar el servicio en tu entorno local, sigue estos pasos:

### **Requisitos Previos**

* **Java Development Kit (JDK) 17 o superior**
* **Gradle** (generalmente incluido con Spring Boot y tu IDE)
* **MySQL Server** (versión 8.0 o superior recomendada)
* **Servicio de Autenticación (AuthService):** Este servicio depende del `auth-service` para la validación de usuarios propietarios. Asegúrate de tenerlo levantado y accesible.


```bash


git clone https://github.com/Leobor91/plazoleta-restaurant-service.git


cd auth-service
```

### **Configuración de la Base de Datos**

1.  Al levantar el servicio este crea ( `db_restaurant_db`) automaticamente 
2.  Actualiza las propiedades de conexión a la base de datos en `src/main/resources/application.yml`:

    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/db_plazoleta_restaurantes?useSSL=false&serverTimezone=UTC
        username: your_mysql_username
        password: your_mysql_password
        driver-class-name: com.mysql.cj.jdbc.Driver
      jpa:
        hibernate:
          ddl-auto: update # o create si quieres que las tablas se creen desde cero
        show-sql: true
        properties:
          hibernate:
            format_sql: true
    ```
    Reemplaza `your_mysql_username` y `your_mysql_password` con tus credenciales de MySQL.

### **Configuración del Servicio de Autenticación**

1.  Asegúrate de que el `auth-service` esté corriendo y accesible.
2.  Configura la URL de tu `auth-service` en `src/main/resources/application.yml`:

    ```yaml
    auth:
      service:
        host: http://localhost:8081 # URL base del Auth Service. Ajustar al puerto de tu Auth Service
        url: /api/v1/users/isOwner # Endpoint para buscar usuario por ID. Ajustar según el Auth Service
       
    ```
    Asegúrate de que `auth.service.host` apunte a la ubicación correcta de tu servicio de autenticación.

### **Ejecución del Servicio**

1.  Clona este repositorio.
2.  Navega hasta la raíz del proyecto `plazoleta-restaurant-service`.
3.  Ejecuta la aplicación Spring Boot usando Gradle:

    ```bash
    ./gradlew bootRun
    ```

    La aplicación se iniciará por defecto en `http://localhost:8082` (o el puerto configurado en `application.yml`).

#  ⚙️ Configuración y Rutas Clave

### puertos
* **Puerto de la Aplicación:** 8082 (configulable en `application.yml`)

---

## Endpoints de API REST

Todos los endpoints están prefijados con `/api/v1/restaurants`.

---

### Registro de Restaurante

`POST /api/v1/restaurants/create-restaurant`

Crea un nuevo restaurante con usuario de **PROPIETARIO** asignado.

**Cuerpo de la Solicitud (JSON):**

```json
{
  "nombre": "Restaurante 123",
  "nit": "90081234",
  "direccion": "Calle 123 #4-56",
  "Celular": "+5730012345",
  "url_del_logo": "http://example.com/logo.png",
  "id_propietario": "3"
}
```
---

### Respuestas Comunes para Registro:

* **`201 Created`**: El restaurante fue creado exitosamente.
* **`400 Bad Request`**: Los datos enviados no son válidos (ej. formato incorrecto, campos obligatorios faltantes).
* **`404 Not Found`**: no se encontro algun recurso.
* **`409 Conflict`**: Se presentó un conflicto de datos; esto puede ocurrir si el nombre, Nit ya están registrados, o el usuario asignado no tiene el rol Propietario.
* **`403 Forbidden`**: No tienes los permisos necesarios para realizar esta acción, posiblemente debido a restricciones de seguridad que impiden la creación del rol solicitado.
* **`500 Internal Server Error`**: Ocurrió un error inesperado en el servidor durante el procesamiento de la solicitud.

---

### Gestión de Excepciones

Las excepciones personalizadas (```PersonalizedException```, ```PersonalizedBadRequestException```, ```PersonalizedNotFoundException```) lanzadas por la lógica de negocio son capturadas por un
```@ControllerAdvice``` y mapeadas a respuestas HTTP ```409 Conflict```, ```400 Bad Request```, ```404 Not Foundt```
, con un cuerpo JSON que contiene el mensaje de error.

* Ubicación del ControllerAdvice:
  ```com.pragma.plazadecomidas.restaurantservice.domain.exception.GlobalExceptionHandler.java```


---

## 📚 Documentación API (Swagger UI)

Una vez que el servicio esté corriendo, puedes acceder a la documentación interactiva de la API a través de Swagger UI en:

* [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

Aquí encontrarás todos los endpoints disponibles, sus parámetros de solicitud, ejemplos de respuesta y códigos de estado.


---

## 🧪 Pruebas y Cobertura de Código

### Ejecutar Tests y Generar Reporte de Cobertura

Para ejecutar todas las pruebas unitarias y generar un reporte de cobertura de código con JaCoCo, utiliza el siguiente comando:

```bash

# ./gradlew clean test jacocoTestReport
 
```



---

### Acceder al Reporte de Cobertura

Una vez generadas las pruebas, puedes ver el **reporte HTML de JaCoCo** en tu navegador:

**Ruta Local:** `build/reports/jacoco/html/index.html`

Este reporte te mostrará el porcentaje de líneas, ramas e instrucciones de tu código que están cubiertas por los tests.

---

```gradle

jacoco {
	toolVersion = "0.8.11"
}

def coverageExcludes = [
		'**/dto/.*',
		'**/spi/.*',
		'**/api/.*',
		'**/mappers/.*',
		'**/repository/.*',
		'**/*.test',
		'**/generated/.*',
]

jacocoTestReport {
	reports {
		xml.required = true
		html.required = true
	}
	afterEvaluate {
		classDirectories.setFrom(files(classDirectories.files.collect {
			fileTree(dir: it, exclude: coverageExcludes)
		}))
	}
}
```

---
