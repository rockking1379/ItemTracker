package org.itemtracker.testgui;

import com.panemu.tiwulfx.table.TextColumn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.itemtracker.common.objects.Loan;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * controls MainPage fxml file
 * Created by james on 5/30/15.
 */
public class MainPageController implements Initializable
{
    @FXML
    private Button btnLoanOut;
    @FXML
    private Button btnReturnLoan;
    @FXML
    private Button btnLoanableHistory;
    @FXML
    private Button btnNewLoanee;
    @FXML
    private Button btnNewLoanable;
    @FXML
    private Button btnLoaneeHistory;
    @FXML
    private TableView<Loan> tblViewActiveLoans;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        TextColumn<Loan> clmnLoanee = new TextColumn<>("loanee");
        TextColumn<Loan> clmnLoanable = new TextColumn<>("loanedItem");
        TextColumn<Loan> clmnCheckOut = new TextColumn<>("checkOut");

        clmnLoanable.setResizable(false);
        clmnLoanee.setResizable(false);
        clmnCheckOut.setResizable(false);

        clmnLoanable.setText("Loaned Item");
        clmnLoanee.setText("Loeaned To");
        clmnCheckOut.setText("Checked Out");

        clmnLoanable.setPrefWidth(150);
        clmnLoanee.setPrefWidth(150);
        clmnCheckOut.setPrefWidth(150);

        tblViewActiveLoans.getColumns().clear(); //just to be safe
        tblViewActiveLoans.getColumns().add(clmnLoanee);
        tblViewActiveLoans.getColumns().add(clmnLoanable);
        tblViewActiveLoans.getColumns().add(clmnCheckOut);

        List<Loan> loans = MainFrame.dbManager.getLoans();

        tblViewActiveLoans.getItems().addAll(loans);
    }
}
