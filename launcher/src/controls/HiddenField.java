package controls;

import javafx.event.EventHandler;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

public class HiddenField extends PasswordField {
    private String informationRequested;
    private CredentialValidator myField = new CredentialValidator() {
        @Override
        public String currentFieldValue() {
            return getTextEntered();
        }
    };
    public HiddenField(String information){

        informationRequested = information;
        this.setText(informationRequested);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleDisplay();
            }
        });
    }
    public CredentialValidator accessValue(){
        return myField;
    }
    private void handleDisplay(){
        if (this.getText().equals(informationRequested)){
            this.setText("");
        }
    }
    private String getTextEntered(){
        return new String(this.getText());
    }

}
