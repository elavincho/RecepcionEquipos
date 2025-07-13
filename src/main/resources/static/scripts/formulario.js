const formulario = document.getElementById("formulario");
const inputs = document.querySelectorAll("#formulario input");

const expresiones = {
  nombreUsuario: /^[a-zA-Z0-9\_\-]{4,16}$/, // Letras, numeros, guion y guion_bajo
  nombre: /^[a-zA-ZÀ-ÿ\s]{1,40}$/, // Letras y espacios, pueden llevar acentos.
  apellido: /^[a-zA-ZÀ-ÿ\s]{1,40}$/, // Letras y espacios, pueden llevar acentos.
  contrasena: /^.{4,12}$/, // 4 a 12 digitos.
  email: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
  telefono: /^\d{7,14}$/, // 7 a 14 numeros.
  documento: /^\d{7,8}$/, // 7 a 8 numeros.
  direccion: /^[a-zA-ZÀ-ÿ\s\_\-\.]{1,20}$/, // Letras, numeros, guion, guion_bajo y puntos.
  altura: /^\d{1,5}$/, // 1 a 4 numeros.
  piso: /^[a-zA-Z0-9]{1,2}$/, // Letras y espacios, pueden llevar acentos.
  depto: /^[a-zA-Z0-9]{1,2}$/, // Letras y espacios, pueden llevar acentos.
  localidad: /^[a-zA-ZÀ-ÿ\s]{4,16}$/, // Letras y espacios, pueden llevar acentos.
  provincia: /^[a-zA-ZÀ-ÿ\s]{5,16}$/, // Letras y espacios, pueden llevar acentos.
};

const campos = {
  nombreUsuario: false,
  nombre: false,
  apellido: false,
  contrasena: false,
  email: false,
  contrasena: false,
  password2: false,
  email: false,
  telefono: false,
  direccion: false,
  altura: false,
  piso: false,
  depto: false,
  localidad: false,
  provincia: false
};

const validarFormulario = (e) => {
  switch (e.target.name) {
    case "nombreUsuario":
      validarCampo(expresiones.nombreUsuario, e.target, "nombreUsuario");
      break;
    case "nombre":
      validarCampo(expresiones.nombre, e.target, "nombre");
      break;
    case "apellido":
      validarCampo(expresiones.apellido, e.target, "apellido");
      break;
    case "contrasena":
      validarCampo(expresiones.contrasena, e.target, "contrasena");
      validarPassword2();
      break;
    case "password2":
      validarPassword2();
      break;
    case "email":
      validarCampo(expresiones.email, e.target, "email");
      break;
    case "documento":
      validarCampo(expresiones.documento, e.target, "documento");
      break;
    case "telefono":
      validarCampo(expresiones.telefono, e.target, "telefono");
      break;
    case "direccion":
      validarCampo(expresiones.direccion, e.target, "direccion");
      break;
    case "altura":
      validarCampo(expresiones.altura, e.target, "altura");
      break;
    case "piso":
      validarCampo(expresiones.piso, e.target, "piso");
      break;
    case "depto":
      validarCampo(expresiones.depto, e.target, "depto");
      break;
    case "localidad":
      validarCampo(expresiones.localidad, e.target, "localidad");
      break;
    case "provincia":
      validarCampo(expresiones.provincia, e.target, "provincia");
      break;
  }
};

const validarCampo = (expresion, input, campo) => {
  if (expresion.test(input.value)) {
    document
      .getElementById(`grupo-${campo}`)
      .classList.remove("formulario-grupo-incorrecto");
    document
      .getElementById(`grupo-${campo}`)
      .classList.add("formulario-grupo-correcto");
    document
      .querySelector(`#grupo-${campo} i`)
      .classList.add("fa-check-circle");
    document
      .querySelector(`#grupo-${campo} i`)
      .classList.remove("fa-times-circle");
    document
      .querySelector(`#grupo-${campo} .formulario-input-error`)
      .classList.remove("formulario-input-error-activo");
    campos[campo] = true;
  } else {
    document
      .getElementById(`grupo-${campo}`)
      .classList.add("formulario-grupo-incorrecto");
    document
      .getElementById(`grupo-${campo}`)
      .classList.remove("formulario-grupo-correcto");
    document
      .querySelector(`#grupo-${campo} i`)
      .classList.add("fa-times-circle");
    document
      .querySelector(`#grupo-${campo} i`)
      .classList.remove("fa-check-circle");
    document
      .querySelector(`#grupo-${campo} .formulario-input-error`)
      .classList.add("formulario-input-error-activo");
    campos[campo] = false;
  }
};

const validarPassword2 = () => {
  const inputPassword1 = document.getElementById("contrasena");
  const inputPassword2 = document.getElementById("password2");

  if (inputPassword1.value !== inputPassword2.value) {
    document
      .getElementById(`grupo-password2`)
      .classList.add("formulario-grupo-incorrecto");
    document
      .getElementById(`grupo-password2`)
      .classList.remove("formulario-grupo-correcto");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.add("fa-times-circle");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.remove("fa-check-circle");
    document
      .querySelector(`#grupo-password2 .formulario-input-error`)
      .classList.add("formulario-input-error-activo");
    campos["contrasena"] = false;
  } else {
    document
      .getElementById(`grupo-password2`)
      .classList.remove("formulario-grupo-incorrecto");
    document
      .getElementById(`grupo-password2`)
      .classList.add("formulario-grupo-correcto");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.remove("fa-times-circle");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.add("fa-check-circle");
    document
      .querySelector(`#grupo-password2 .formulario-input-error`)
      .classList.remove("formulario-input-error-activo");
    campos["contrasena"] = true;
  }
};

const validarRepetirContrasena = () => {
  const inputPassword1 = document.getElementById("nuevaContrasena");
  const inputPassword2 = document.getElementById("repetirContrasena");

  if (inputPassword1.value !== inputPassword2.value) {
    document
      .getElementById(`grupo-password2`)
      .classList.add("formulario-grupo-incorrecto");
    document
      .getElementById(`grupo-password2`)
      .classList.remove("formulario-grupo-correcto");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.add("fa-times-circle");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.remove("fa-check-circle");
    document
      .querySelector(`#grupo-password2 .formulario-input-error`)
      .classList.add("formulario-input-error-activo");
    campos["contrasena"] = false;
  } else {
    document
      .getElementById(`grupo-password2`)
      .classList.remove("formulario-grupo-incorrecto");
    document
      .getElementById(`grupo-password2`)
      .classList.add("formulario-grupo-correcto");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.remove("fa-times-circle");
    document
      .querySelector(`#grupo-password2 i`)
      .classList.add("fa-check-circle");
    document
      .querySelector(`#grupo-password2 .formulario-input-error`)
      .classList.remove("formulario-input-error-activo");
    campos["contrasena"] = true;
  }
};

inputs.forEach((input) => {
  input.addEventListener("keyup", validarFormulario);
  input.addEventListener("blur", validarFormulario);
});

//  <<<---------- * ---------- * ---------- * ---------- * ---------- * ---------->>>

// Fecha con Calendario

document.addEventListener("DOMContentLoaded", function () {
  flatpickr("#fecha", {
    dateFormat: "d/m/Y", // Formato de la fecha
    locale: "es", // Idioma español
    defaultDate: "", // Fecha por defecto "today"(hoy)
    allowInput: true, // Permitir edición manual
    clickOpens: true, // Abrir calendario al hacer clic
    monthSelectorType: "dropdown", // Seleccionar mes con un dropdown
    yearSelectorType: "dropdown", // Seleccionar año con un dropdown
  });
});

// Fin Fecha con Calendario

//  <<<---------- * ---------- * ---------- * ---------- * ---------- * ---------->>>

// Verificación de Números y Fechas
function validacionFormulario() {
  // Llama a ambas funciones y verifica que ambas devuelvan true
  if (validarNumero() && validarFecha()) {
    return true; // Permite el envío del formulario
  } else {
    return false; // Evita el envío del formulario
  }
}

// Validar Numeros
function validarNumero() {
  const numeroInput = document.getElementById("numero");
  const numeroInput2 = document.getElementById("numero2");
  const numeroError = document.getElementById("numeroError");
  const numeroError2 = document.getElementById("numeroError2");
  const numero = numeroInput.value.trim();
  const numero2 = numeroInput2.value.trim();

  // Expresión regular para validar números con punto decimal
  const regex = /^\d+(\.\d{1,2})?$/;

  if (!regex.test(numero)) {
    numeroError.style.display = "block"; // Mostrar mensaje de error
    return false; // Evitar que el formulario se envíe
  } else if (!regex.test(numero2)) {
    numeroError2.style.display = "block"; // Mostrar mensaje de error
    return false; // Evitar que el formulario se envíe
  } else {
    numeroError.style.display = "none"; // Ocultar mensaje de error
    return true; // Permitir el envío del formulario
  }
}
//Fin Validar Numeros

//  <<<---------- * ---------- * ---------- * ---------- * ---------- * ---------->>>

// Validar Fechas
function validarFecha() {
  const fechaInput = document.getElementById("fecha");
  const fecha = fechaInput.value.trim();

  // Si el campo está vacío, permitir el envío del formulario
  if (fecha === "") {
    return true;
  }

  // Expresión regular para validar el formato dd/MM/yyyy
  const regex = /^\d{2}\/\d{2}\/\d{4}$/;

  if (!regex.test(fecha)) {
    alert("La fecha debe estar en formato dd/MM/yyyy o estar vacía.");
    return false; // Evitar que el formulario se envíe
  }

  return true; // Permitir el envío del formulario
}

// Fin Validar Fecha

//  <<<---------- * ---------- * ---------- * ---------- * ---------- * ---------->>>

/* Buscador*/
document.addEventListener("keyup", (e) => {
  if (e.target.matches("#buscador")) {
    /*Para vaciar el campo al presionar esc*/
    if (e.key === "Escape") {
      e.target.value = "";
    }
    document.querySelectorAll("#buscar").forEach((articulo) => {
      articulo.textContent.toUpperCase().includes(e.target.value) ||
      articulo.textContent.toLowerCase().includes(e.target.value) ||
      articulo.textContent.includes(e.target.value)
        ? articulo.classList.remove("filtro")
        : articulo.classList.add("filtro");
    });
  }
});
/* Fin Buscador*/

//  <<<---------- * ---------- * ---------- * ---------- * ---------- * ---------->>>

// Mostrar Fecha

function mostrarFechaCompleta() {
  const fecha = new Date();

  // Arrays con los nombres de los días y meses
  const diasSemana = [
    "Domingo",
    "Lunes",
    "Martes",
    "Miércoles",
    "Jueves",
    "Viernes",
    "Sábado",
  ];
  const meses = [
    "Enero",
    "Febrero",
    "Marzo",
    "Abril",
    "Mayo",
    "Junio",
    "Julio",
    "Agosto",
    "Septiembre",
    "Octubre",
    "Noviembre",
    "Diciembre",
  ];

  // Obtener el día de la semana, el día del mes, el mes y el año
  const diaSemana = diasSemana[fecha.getDay()]; // getDay() devuelve 0 (domingo) a 6 (sábado)
  const diaMes = fecha.getDate(); // Día del mes (1-31)
  const mes = meses[fecha.getMonth()]; // getMonth() devuelve 0 (enero) a 11 (diciembre)
  const anio = fecha.getFullYear(); // Año (4 dígitos)

  // Mostrar la fecha en el elemento HTML
  document.getElementById("diaSemana").textContent = `${diaSemana}`;
  document.getElementById("diaMes").textContent = `${diaMes}`;
  document.getElementById("mes").textContent = `${mes}`;
  document.getElementById("anio").textContent = `${anio}`;
}

// Llamar a la función cuando la página se cargue
window.onload = mostrarFechaCompleta;

// Fin Mostrar Fecha

//  <<<---------- * ---------- * ---------- * ---------- * ---------- * ---------->>>

// Mostrar el nombre del archivo seleccionado en los input

document.getElementById("img").onchange = function () {
  document.getElementById("fichero").innerHTML = this.files[0].name;
};

