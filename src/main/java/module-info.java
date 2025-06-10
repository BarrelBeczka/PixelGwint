// Nazwa modułu może być np. taka jak nazwa projektu lub główny pakiet
// Jeśli IntelliJ nazwał moduł np. "PixelGwint", zostaw to.
// Dla spójności, jeśli możesz, nazwij moduł "pixelgwint"
module pixelgwint { // LUB np. module PixelGwint - sprawdź jak nazwał IntelliJ
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;

    // Otwieramy nasz pakiet widoków dla JavaFX
    opens pixelgwint.widok to javafx.fxml;
    // Otwieramy pakiet modeli, jeśli modele będą używane w kontrolkach FXML
    opens pixelgwint.model to javafx.fxml;


    // Eksportujemy główny pakiet, aby aplikacja mogła być uruchomiona
    exports pixelgwint;
    // Możemy też chcieć eksportować inne pakiety, jeśli będą potrzebne gdzieś indziej
    // exports pixelgwint.model;
    // exports pixelgwint.widok;
}