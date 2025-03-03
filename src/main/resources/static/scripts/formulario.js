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



// Fecha

document.addEventListener("DOMContentLoaded", function() {
  flatpickr("#fecha", {
    dateFormat: "d/m/Y",       // Formato de la fecha
    locale: "es",             // Idioma español
    defaultDate: "",     // Fecha por defecto "today"(hoy)
    allowInput: true,         // Permitir edición manual
    clickOpens: true,         // Abrir calendario al hacer clic
    monthSelectorType: "dropdown", // Seleccionar mes con un dropdown
    yearSelectorType: "dropdown",  // Seleccionar año con un dropdown
  });
});

// Fin fecha
