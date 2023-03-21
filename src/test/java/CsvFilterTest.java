import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFilterTest {

    private final String HEADER_LINE = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente";
    private final List<String> emptyDataFile = new ArrayList<>(List.of(HEADER_LINE));
    private String emptyField = "";

    private CsvFilter filter = new CsvFilter();

    @Test
    void correct_lines_are_not_filtered(){

    }



}