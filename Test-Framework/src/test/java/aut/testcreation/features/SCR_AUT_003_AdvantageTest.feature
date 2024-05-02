@functional
Feature: Test para la página Advantage

  @fast
  Scenario: SC_003_01_agregarRaton_124
    Given El Usuario agrega el ratón de la posición "pPosicion" al carrito
    Then El Usuario comprueba que el ratón se agregó al carrito

  @slow
  Scenario: TC_003_02_usuarioExistente
    Given El Usuario intenta crear una cuenta con el usuario "pUsername", el email "pEmail" y la contraseña "pPassword" y "pConfirmPassword"
    Then El Sistema muestra que el Usuario ya existe con el mensaje "pMensajeValidacion"
