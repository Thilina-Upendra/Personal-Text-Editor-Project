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
    public JFXTextField txtReplaceWord;
    public JFXButton btnReplaceAll;
    public JFXButton btnReplace;
    public Label lblSearchCount;

    private Matcher matcher;
    private boolean textChanged;
    private ArrayList<Integer> indexes ;


    public void initialize() {
        lblFileSaveName.setText("untitled document*");
        lblFilePath.setText("");

        txtTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                setWordCount();
            }else{
                lblWordCount.setText("");
                lblSearchCount.setText("");
            }
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

        /*Create a new window method*/
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

        /*Open a selected window method*/
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

        /*Save work method*/
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

        /*Exit from the window method*/
        Stage stage = (Stage) mainContext.getScene().getWindow();
        if (txtTextArea.getText().isEmpty()) {
            stage.close();
        } else {
            mtmSaveOnAction(new ActionEvent());
        }
    }

    public void mtmCutOnAction(ActionEvent actionEvent) {

        /*Cut selected text method*/
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

        /*Copy selected text to the clipboard method*/
        setSelectedText();
    }

    public void mtmPasteOnAction(ActionEvent actionEvent) {

        /*Paste copied or cut text from the text*/
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        String string = systemClipboard.getString();
        int position = txtTextArea.getCaretPosition();
        txtTextArea.insertText(position, string);
    }

    public void mtmSelectAllOnAction(ActionEvent actionEvent) {

        /*Select all the text method*/
        txtTextArea.selectAll();
    }

    public void mtmAboutUsOnAction(ActionEvent actionEvent) throws IOException {

        /*Load about us window*/
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

        /*Navigate to the next word method*/
        txtTextArea.deselect();
        if(textChanged){
            int flags = 0;
            if (!btnRegExp.isSelected()) flags = flags|Pattern.LITERAL;
            if (!btnCaseSensitive.isSelected()) flags = flags | Pattern.CASE_INSENSITIVE;
            matcher = Pattern.compile(txtFind.getText(),flags).matcher(txtTextArea.getText());
            textChanged = false;

            /*Get all the indexes*/
            indexes = new ArrayList<>();
            while(matcher.find()){
                indexes.add(matcher.start());
                indexes.add(matcher.end());
            }
            matcher.reset();
        }

        if(matcher.find()){
            setRangeAndSetForwardWordCount();
        }else{
            matcher.find(indexes.get(0));
            setRangeAndSetForwardWordCount();
        }
    }

    private void setRangeAndSetForwardWordCount() {

        /*Show the forward navigation word count*/
        txtTextArea.selectRange(matcher.start(), matcher.end());
        lblSearchCount.setText(" "+(indexes.indexOf(matcher.start())/2+1)+"/"+(indexes.size()/2));
    }

    public void btnCaseSensitiveOnAction(ActionEvent actionEvent) {

        /*Case-sensitive option*/
        textChanged = true;
        btnFindNextWord.fire();
    }

    public void btnRegExpOnAction(ActionEvent actionEvent) {

        /*Regular expression option*/
        textChanged = true;
        btnFindNextWord.fire();
    }

    public void btnFindPreviousWordOnAction(ActionEvent actionEvent) {

        /*Navigate backwards word*/

        /*Finalize the index*/
        for (int i = 0; i < indexes.size(); i++) {
            if(matcher.start()==indexes.get(0)){
                txtTextArea.selectRange(indexes.get(indexes.size()-2),indexes.get(indexes.size()-1));
                lblSearchCount.setText(indexes.size()/2+"/"+ indexes.size()/2);
                matcher.find(indexes.get(indexes.size()-2));
                return;
            }else if (matcher.start()==indexes.get(i)){
                txtTextArea.selectRange(indexes.get(i-2),indexes.get(i-1));
                lblSearchCount.setText(" "+(((i-2)/2)+1)+"/"+(indexes.size()/2));
                matcher.find(indexes.get(i-2));
                return;
            }
        }
    }

    public void btnReplaceAllOnAction(ActionEvent actionEvent) {

        /*Replace all method*/
        if(matcher==null){
            btnFindNextWordOnAction(new ActionEvent());
        }
        txtTextArea.deselect();
        String replacedText = matcher.replaceAll(txtReplaceWord.getText());
        txtTextArea.setText(replacedText);
        textChanged = true;
        btnFindNextWordOnAction(new ActionEvent());
    }

    public void btnReplaceOnAction(ActionEvent actionEvent) {

        /*Replace selected word methdo*/
        if(matcher==null){
            btnFindNextWordOnAction(new ActionEvent());
            return;
        }
        StringBuffer buffer = new StringBuffer(txtTextArea.getText());
        StringBuffer replacedText = buffer.replace(matcher.start(), matcher.end(), txtReplaceWord.getText());
        txtTextArea.setText(String.valueOf(replacedText));
        textChanged = true;
        btnFindNextWordOnAction(new ActionEvent());
    }
}
