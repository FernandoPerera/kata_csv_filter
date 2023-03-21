import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFilterTest {

    /*
    *   Use cases:
    *
    *       - An empty or null list will produce an empty list of output.
    *
    */

    private final String HEADER_LINE = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente";

    private final CsvFilter FILTER = new CsvFilter();

    @Test
    void given_empty_list_return_empty_list(){
        List<String> lines = List.of();
        List<String> result = FILTER.apply(lines);

        assertEquals(lines, result);
    }

}