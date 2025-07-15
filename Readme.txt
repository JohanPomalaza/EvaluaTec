============================================================
               Evaluatec - Aplicación de Tareas
============================================================

Descripción:
------------
Evaluatec es una aplicación móvil que permite gestionar tareas y visualizar las notas que el profesor vaya subiendo.
Los usuarios pueden crear, editar, eliminar y marcar tareas como completadas.

Funcionalidades principales:
----------------------------
Estudiante:
- Visualiza tus notas por curso.
- Recibe comentarios de parte de tu profesor.
- Sigue le evolución de tus notas.

Profesor:
- Editar tareas existentes.
- Registra notas según curso o tema.
- Visualiza los alumnos de tu salón.
- Brinda comentarios según el tema a tus alumnos.

Administrador:
- Gestiona tus docentes.
- Gestiona los alumnos.
- Gestiona los cursos de la institución.

Requisitos del sistema:
-----------------------
- Android 7.0 (Nougat) o superior.
- Android Studio Bumblebee o superior (para compilar).
- Conexión a internet (opcional, para sincronización con API).

Instalación:
------------
1. Clonar o descargar el proyecto.
2. Abrir el proyecto con Android Studio.
3. Conectar un dispositivo o iniciar un emulador.
4. Ejecutar el proyecto seleccionando la opción "Run > Run 'app'".

Estructura del proyecto:
------------------------
/app
   /java            -> Modelos de datos y base de datos local (Room)
   /res             -> Actividades y fragmentos de la interfaz de usuario
   /api             -> Clases para consumo de API (Retrofit)


Configuración adicional:
------------------------
Si deseas habilitar sincronización con servidor externo:
1. Crear un archivo llamado 'local.properties' en la raíz del proyecto.
2. Añadir lo siguiente:

   API_BASE_URL=https://api.tuservicio.com
   API_KEY=tu_clave_api

Créditos:
---------
Desarrollado por: Equipo Chapatu20
Versión: 1.0.0
Fecha: Julio 2025

Licencia:
---------
Este proyecto está licenciado bajo la licencia MIT. Puedes usarlo, modificarlo y distribuirlo libremente.

Contacto:
---------
Email: soporte@evaluatec.com
Web: https://soluciones.com