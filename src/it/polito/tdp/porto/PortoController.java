package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void handleCoautori(ActionEvent event) {
    	Author partenza=this.boxPrimo.getValue();
    	this.txtResult.clear();
    	this.txtResult.appendText("Lista dei coautori di "+partenza+"\n");
    	for(Author a: model.cercaCoautori(partenza)) {
    		this.txtResult.appendText(a+"\n");
    	}
    	this.boxSecondo.getItems().clear();
    	ArrayList temp=model.autoriNonCollegati();
    	Collections.sort(temp, new ComparatorAutore());
    	this.boxSecondo.getItems().addAll(temp);
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	Author partenza=this.boxPrimo.getValue();
    	Author arrivo=this.boxSecondo.getValue();
    	ArrayList<Paper> cammino=this.model.getCammino(partenza,arrivo);
    	if(cammino==null) {
    		this.txtResult.appendText("\nNon esiste nessun cammino tra "+partenza+" e "+arrivo+"\n");
    		return;
    	}
    	this.txtResult.appendText("\nlista degli articoli tra "+partenza+" e "+arrivo+"\n");
    	for(Paper a: cammino) {
    		this.txtResult.appendText(a+"\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model=model;
		model.creaGrafo();
		ArrayList<Author> temp= new ArrayList<Author>(model.getAutori());
		Collections.sort(temp, new ComparatorAutore());
		this.boxPrimo.getItems().addAll(temp);
	}
}
