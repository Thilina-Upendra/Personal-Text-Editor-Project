package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditorFormController {

    public Label lblAppName;
    public MenuBar mbrBar;
    public Menu mnFile;
    public MenuItem mtmNew;
    public MenuItem mtmOpen;
    public MenuItem mtmSave;
    public MenuItem mtmPrint;
    public MenuItem mtmExit;
    public Menu mnEdit;
    public MenuItem mtmCut;
    public MenuItem mtmCopy;
    public MenuItem mtmPaste;
    public MenuItem mtmSelectAll;
    public Menu mnHelp;
    public MenuItem mtmAboutUs;
    public JFXTextArea txtTextArea;
    public Label lblFileSaveName;
    public Label lblFilePath;
    public AnchorPane mainContext;
    public Label lblWordCount;
    public JFXTextField txtFind;
    public JFXButton btnFindNextWord;
    public ToggleButton btnCaseSensitive;
    public ToggleButton btnRegExp;
    public JFXButton btnFindPreviousWord;

    private Matcher matcher;
    private boolean textChanged;
    private ArrayList<Integer> indexes = new ArrayList<>();


    public void initialize() {
        lblFileSaveName.setText("untitled document*");
        lblFilePath.setText("");

        txtTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            setWordCount();
        });

        txtFind.textProperty().addListener((observable, oldValue, newValue) -> {
            textChanged = true;
        });


    }



    private void setWordCount() {
        String inputText = txtTextArea.getText();
        Matcher matcher = Pattern.compile("\\b\\w+\\b").matcher(inputText);
        int count = 0;
        while(matcher.find()){
            count++;
        }
        lblWordCount.setText(String.valueOf(count));
    }


    public void mtmNewONAction(ActionEvent actionEvent) throws IOException {

        if (txtTextArea.getText().isEmpty()) {
            loadNewWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save this file??", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                mtmSaveOnAction(new ActionEvent());
            } else {
                loadNewWindow();
            }
        }
    }

    private void loadNewWindow() throws IOException {
        Stage stage = (Stage) mainContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/TextEditorForm.fxml"))));
        stage.show();
    }

    public void mtmOpenOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {

            /*Create a pointer to open(read) the file*/
            Path path = Paths.get(file.getAbsolutePath());

            byte[] bytes = Files.readAllBytes(path);

            txtTextArea.setText(new String(bytes));

            /*Set file name and path*/
            lblFilePath.setText(file.getAbsolutePath());
            lblFileSaveName.setText(file.getName());
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to select path again??", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Open path confirmation");
            Optional<ButtonType> btnType = alert.showAndWait();
            if (btnType.get().equals(ButtonType.YES)) {
                mtmOpenOnAction(new ActionEvent());
            } else {
                return;
            }
        }
    }

    public void mtmSaveOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a file");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {

            /*Create a pointer to save(write) the file*/
            Path path = Paths.get(file.getAbsolutePath());

            /*Get the file content*/
            String fileContent = txtTextArea.getText();
            byte[] bytes = fileContent.getBytes();
            Files.write(path, bytes);

            /*Set file name and path*/
            lblFilePath.setText(file.getAbsolutePath());
            lblFileSaveName.setText(file.getName());
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to select path again??", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Save path confirmation");
            Optional<ButtonType> btnType = alert.showAndWait();
            if (btnType.get().equals(ButtonType.YES)) {
                mtmSaveOnAction(new ActionEvent());
            } else {
                return;
            }
        }
    }

    public void mtmExitOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) mainContext.getScene().getWindow();
        if (txtTextArea.getText().isEmpty()) {
            stage.close();
        } else {
            mtmSaveOnAction(new ActionEvent());
        }
    }

    public void mtmCutOnAction(ActionEvent actionEvent) {
        setSelectedText();
        txtTextArea.setText(txtTextArea.getText().replace(txtTextArea.getSelectedText(), ""));
    }

    private void setSelectedText() {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(txtTextArea.getSelectedText());
        systemClipboard.setContent(content);
    }

    public void mtmCopyOnAction(ActionEvent actionEvent) {
        setSelectedText();
    }

    public void mtmPasteOnAction(ActionEvent actionEvent) {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        String string = systemClipboard.getString();
        int position = txtTextArea.getCaretPosition();
        txtTextArea.insertText(position, string);
    }

    public void mtmSelectAllOnAction(ActionEvent actionEvent) {
        txtTextArea.selectAll();
    }

    public void mtmAboutUsOnAction(ActionEvent actionEvent) throws IOException {
        URL url = getClass().getResource("../view/AboutUsForm.fxml");
        AnchorPane load = FXMLLoader.load(url);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("About UpText");
        stage.show();
    }

    public void btnNewDocOnAction(ActionEvent actionEvent) {
        mtmNew.fire();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        mtmSave.fire();
    }

    public void btnOpenOnAction(ActionEvent actionEvent) {
        mtmOpen.fire();
    }

    public void btnCutOnAction(ActionEvent actionEvent) {
        mtmCut.fire();
    }

    public void btnPasteOnAction(ActionEvent actionEvent) {
        mtmPaste.fire();
    }

    public void btnCopyOnAction(ActionEvent actionEvent) {
        mtmCopy.fire();
    }

    public void btnFindNextWordOnAction(ActionEvent actionEvent) {
        txtTextArea.deselect();
        if(textChanged){
            int flags = 0;
            if (!btnRegExp.isSelected()) flags = flags|Pattern.LITERAL;
            if (!btnCaseSensitive.isSelected()) flags = flags | Pattern.CASE_INSENSITIVE;
            matcher = Pattern.compile(txtFind.getText(),flags).matcher(txtTextArea.getText());
            textChanged = false;

            /*Get all the indexes*/
            while(matcher.find()){
                indexes.add(matcher.start());
                indexes.add(matcher.end());
            }
            matcher.reset();
        }

        if(matcher.find()){
            txtTextArea.selectRange(matcher.start(), matcher.end());
        }else{
            matcher.reset();
        }
    }

    public void btnCaseSensitiveOnAction(ActionEvent actionEvent) {
        textChanged = true;
        btnFindNextWord.fire();
    }

    public void btnRegExpOnAction(ActionEvent actionEvent) {
        textChanged = true;
        btnFindNextWord.fire();
    }

    public void btnFindPreviousWordOnAction(ActionEvent actionEvent) {

        /*Finalize the index*/
        for (int i = 0; i < indexes.size(); i++) {
            if(matcher.start()==indexes.get(0)){
                txtTextArea.selectRange(indexes.get(indexes.size()-2),indexes.get(indexes.size()-1));
                matcher.find(indexes.get(indexes.size()-2));
                return;
            }
            if (matcher.start()==indexes.get(i)){
                txtTextArea.selectRange(indexes.get(i-2),indexes.get(i-1));
                matcher.find(indexes.get(i-2));
                return;
            }
        }
    }
}
