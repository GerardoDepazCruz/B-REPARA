# 📘 Documentación Técnica — RePara Backend

**Plataforma Móvil Inteligente para la Gestión de Servicios Técnicos a Domicilio**

| | |
|---|---|
| **Versión** | 1.0 |
| **Fecha** | Octubre 2026 |
| **Curso** | Integrador II |
| **Repositorio** | repara_backend |
| **Base de Datos** | MySQL 8 (`repara_db`) |

---

## 📑 Índice

1. [Introducción](#1-introducción)
2. [Arquitectura del Sistema](#2-arquitectura-del-sistema)
3. [Stack Tecnológico](#3-stack-tecnológico)
4. [Estructura del Proyecto](#4-estructura-del-proyecto)
5. [Modelo de Datos](#5-modelo-de-datos)
6. [Diagrama de Clases UML](#6-diagrama-de-clases-uml)
7. [API REST](#7-api-rest)
8. [Seguridad](#8-seguridad)
9. [Instalación y Ejecución](#9-instalación-y-ejecución)
10. [Despliegue](#10-despliegue)
11. [Testing](#11-testing)
12. [Glosario](#12-glosario)

---

## 1. Introducción

### 1.1 Descripción del Proyecto

**RePara** es una plataforma móvil inteligente que conecta a clientes con técnicos verificados para la reparación de electrodomésticos a domicilio en Lima, Perú. Utiliza inteligencia artificial (**Gemini API**) para clasificar las solicitudes y asignar al técnico más adecuado, con verificación por PIN y certificado de garantía digital.

### 1.2 Autores

| Nombre | Rol |
|---|---|
| Chavez Diaz, Victor Jesus | Development Team - Integración |
| Chavez Geldres, Kevin | Development Team - BD / QA |
| Depaz Cruz, Gerardo Thomas | Scrum Master |
| Ortiz Loyola, Andres | Development Team - Soporte |
| Perez Medina, Ana Cristina | Development Team - Frontend |
| Sanchez Rosales, Jefferson Alexander | Development Team - Backend / IA |

### 1.3 Docente

Ramos Pariachi, Jherber Fernando

---

## 2. Arquitectura del Sistema

### 2.1 Arquitectura General

RePara utiliza una **arquitectura cliente-servidor de 3 capas**:

```text
┌─────────────────────────────────────────────┐
│   CAPA DE PRESENTACIÓN (Frontend)           │
│   React + Vite + Capacitor (Android)        │
└────────────────┬────────────────────────────┘
                 │ HTTPS / JSON
                 ▼
┌─────────────────────────────────────────────┐
│   CAPA DE NEGOCIO (Backend)                 │
│   Spring Boot 3 + Java 17 + JWT             │
│   - Controladores REST                      │
│   - Servicios de negocio                    │
│   - Seguridad (JWT, BCrypt)                 │
│   - Integración Gemini API + FCM            │
└────────────────┬────────────────────────────┘
                 │ JDBC
                 ▼
┌─────────────────────────────────────────────┐
│   CAPA DE DATOS (Base de Datos)             │
│   MySQL 8 — repara_db                       │
│   - 28 tablas relacionales                  │
└─────────────────────────────────────────────┘
```

### 2.2 Patrón Arquitectónico

El backend sigue el patrón **MVC (Model-View-Controller)** con capas adicionales siguiendo los principios de **Clean Architecture**:

```text
Controller → Service → Repository → Entity → MySQL
    ↓           ↓
   DTO      JwtService / IAService / FCM
```

---

## 3. Stack Tecnológico

| Capa | Tecnología | Versión |
|---|---|---|
| **Backend** | Java | 17 |
| | Spring Boot | 3.2+ |
| | Spring Security | 6+ |
| | Spring Data JPA | 3+ |
| | JJWT | 0.12+ |
| | Maven | 3.9+ |
| **Base de Datos** | MySQL | 8.0+ |
| **IA** | Google Gemini API | v1 |
| **Notificaciones** | Firebase Cloud Messaging | - |
| **Cloud Backend** | Render | - |
| **Cloud BD** | Aiven | - |
| **Cloud Frontend** | Netlify | - |

---

## 4. Estructura del Proyecto

```text
repara_backend/
├── src/
│   ├── main/
│   │   ├── java/com/repara/
│   │   │   ├── configuracion/
│   │   │   ├── controladores/
│   │   │   ├── dtos/
│   │   │   ├── entidades/
│   │   │   ├── excepciones/
│   │   │   ├── repositorios/
│   │   │   ├── seguridad/
│   │   │   ├── servicios/
│   │   │   ├── utilidades/
│   │   │   └── ReparaApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
├── pom.xml
├── DOCUMENTACION_TECNICA.md
└── README.md
```

---

## 5. Modelo de Datos

### 5.1 Tablas de la Base de Datos

La base de datos `repara_db` contiene **28 tablas** organizadas en 6 bloques:

| Bloque | Tablas |
|---|---|
| **1. Base** | usuarios, categorias, pagos_metodos, configuracion_sistema |
| **2. Usuarios** | direcciones_cliente, sesiones_usuario, logs_actividad, notificaciones_push_tokens |
| **3. Técnicos** | postulaciones_tecnico, tecnicos, tecnico_especialidad, insignias_tecnico |
| **4. Solicitudes** | solicitudes_servicio, historial_estados_solicitud, mensajes_ia, evidencias, pin_verificacion, chat_trayecto, especialidades_solicitud |
| **5. Pagos** | pagos, certificados_garantia, calificaciones |
| **6. Reportes** | reportes_disputas, reportes_inconvenientes_tecnico, notificaciones, notificacion_destinatario, informes_tecnico, repuestos_utilizados |

### 5.2 Diccionario de Datos

Ver archivo `sql/repara_db.sql` para el DDL completo.

---

## 6. Diagrama de Clases UML

El sistema está organizado en **6 capas**:

1. **Entidades** — 28 clases Java con `@Entity`
2. **Repositorios** — interfaces `JpaRepository`
3. **Servicios** — lógica de negocio
4. **Controladores** — endpoints REST
5. **DTOs** — transferencia de datos
6. **Seguridad** — JWT, filtros, config

### 6.1 Clases Principales

| Clase | Responsabilidad |
|---|---|
| `Usuario` | Clase base para todos los usuarios |
| `Tecnico` | Usuario que atiende servicios |
| `SolicitudServicio` | Entidad central del flujo |
| `Pago` | Gestión de transacciones |
| `PinVerificacion` | Verificación de llegada del técnico |
| `InformeTecnico` | Documento de cierre del servicio |
| `Calificacion` | Retroalimentación del cliente |

---

## 7. API REST

### 7.1 Autenticación

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| POST | `/api/auth/registro` | Registro de usuario | ❌ |
| POST | `/api/auth/login` | Inicio de sesión | ❌ |
| POST | `/api/auth/verificar` | Verificar correo | ❌ |
| POST | `/api/auth/logout` | Cerrar sesión | ✅ |

### 7.2 Solicitudes

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| POST | `/api/solicitudes` | Crear solicitud | ✅ Cliente |
| GET | `/api/solicitudes/{id}` | Obtener solicitud | ✅ |
| PUT | `/api/solicitudes/{id}/aceptar` | Aceptar solicitud | ✅ Técnico |
| DELETE | `/api/solicitudes/{id}` | Cancelar solicitud | ✅ |

### 7.3 Técnicos

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| GET | `/api/tecnicos` | Listar técnicos | ✅ |
| POST | `/api/tecnicos/postular` | Postular como técnico | ✅ |
| PUT | `/api/tecnicos/disponibilidad` | Cambiar disponibilidad | ✅ Técnico |
| PUT | `/api/tecnicos/ubicacion` | Actualizar ubicación | ✅ Técnico |

### 7.4 Pagos

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| POST | `/api/pagos` | Procesar pago | ✅ Cliente |
| GET | `/api/pagos/{id}` | Obtener pago | ✅ |

### 7.5 Notificaciones

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| POST | `/api/notificaciones/enviar` | Enviar notificación | ✅ Admin |
| GET | `/api/notificaciones` | Listar notificaciones | ✅ |

---

## 8. Seguridad

| Aspecto | Implementación |
|---|---|
| **Autenticación** | JWT con HS512 |
| **Expiración token** | 24 horas |
| **Refresh token** | 7 días |
| **Contraseñas** | BCrypt factor 12 |
| **Datos sensibles** | AES-256 |
| **CORS** | Configurado para Netlify y local |
| **Cumplimiento** | Ley N° 29733 (Perú) |

---

## 9. Instalación y Ejecución

### 9.1 Requisitos

- Java 17
- Maven 3.9+
- MySQL 8.0+

### 9.2 Pasos

```bash
# 1. Clonar repositorio
git clone https://github.com/tu-usuario/repara_backend.git
cd repara_backend

# 2. Crear base de datos
mysql -u root -p < sql/repara_db.sql

# 3. Configurar credenciales en application-dev.properties

# 4. Compilar
mvn clean install

# 5. Ejecutar
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080/api`.

---

## 10. Despliegue

### 10.1 Backend — Render

1. Conectar repositorio GitHub
2. Configurar variables: `DB_URL`, `DB_USER`, `DB_PASS`, `JWT_SECRET`, `GEMINI_API_KEY`
3. Deploy automático en push a `main`

### 10.2 Base de Datos — Aiven

1. Crear servicio MySQL
2. Ejecutar `repara_db.sql`
3. Copiar credenciales

### 10.3 Frontend — Netlify

1. Conectar repositorio
2. Build: `npm run build`
3. Publish: `dist`

---

## 11. Testing

### 11.1 Pruebas Unitarias

```bash
mvn test
```

### 11.2 Pruebas de Integración

Uso de `@SpringBootTest` + `MockMvc` para probar endpoints.

### 11.3 Colección Postman

Disponible en `postman/repara.json`.

---

## 12. Glosario

| Término | Definición |
|---|---|
| **JWT** | JSON Web Token |
| **FCM** | Firebase Cloud Messaging |
| **IA** | Inteligencia Artificial |
| **PIN** | Personal Identification Number |
| **SLA** | Service Level Agreement |
| **KPI** | Key Performance Indicator |
| **DTO** | Data Transfer Object |
| **ORM** | Object-Relational Mapping |
| **MVC** | Model-View-Controller |

---

**Última actualización:** Octubre 2026  
**Versión:** 1.0  
**Autor:** Equipo RePara — Curso Integrador II