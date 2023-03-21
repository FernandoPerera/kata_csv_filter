import exeptions.ListWithoutHeaderExeption;
import org.junit.jupiter.api.Test;
import utils.CsvFilter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFilterTest {

    /*
    *   Use cases:
    *
    *       - An empty or null list will produce an empty list of output.
    *
    *       - A single line file is incorrect because it has no header.
    */

    private final String HEADER_LINE = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente";

    private final CsvFilter FILTER = new CsvFilter();

    @Test
    void given_empty_list_return_empty_list() throws ListWithoutHeaderExeption {
        List<String> lines = List.of();
        List<String> result = FILTER.apply(lines);

        assertEquals(lines, result);
    }

    @Test
    void given_list_without_header_line_return_throw_error() throws ListWithoutHeaderExeption {

        List<String> linesWithoutHeader = List.of(String.join(",", "1", "21/03/2023", "1000", "810", "", "7","B76430134", ""));

        ListWithoutHeaderExeption thrown = assertThrows(ListWithoutHeaderExeption.class, () ->
            FILTER.apply(linesWithoutHeader));

        String errorResponse = "ERROR: don't exist header in the list";

        assertTrue(thrown.getMessage().contentEquals(errorResponse));

    }

}