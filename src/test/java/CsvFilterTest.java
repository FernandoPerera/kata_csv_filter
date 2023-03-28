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
    *
    *       - A file with a single invoice where everything is correct should produce as output
    *         the same line.
    *
    *       - A file with a single invoice where VAT and IGIC are filled in, should eliminate the
    *         line.
    *
    *       - A file with a single invoice where the net is wrongly calculated should be deleted.
    *         nothing.
    *
    *       - A file with a single invoice where CIF and NIF are filled in, should eliminate the
    *         line.
    *
    *       - If the invoice number is repeated on several lines, all of them are deleted (without leaving
    *         none).
    */

    private final String HEADER_LINE = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente";

    private final CsvFilter FILTER = new CsvFilter();

    @Test
    void if_invoice_is_not_entered_nothing_is_returned() throws ListWithoutHeaderExeption {

        List<String> lines = List.of();

        List<String> result = FILTER.apply(lines);

        assertEquals(lines, result);
    }

    @Test
    void if_header_is_not_entered_return_throw_error() {

        List<String> linesWithoutHeader = List.of(String.join(",", "1", "21/03/2023", "1000", "810", "", "7","B76430134", ""));

        ListWithoutHeaderExeption thrown = assertThrows(ListWithoutHeaderExeption.class, () ->
            FILTER.apply(linesWithoutHeader));

        assertEquals(thrown.getClass(), ListWithoutHeaderExeption.class);
    }

    @Test
    void if_an_correct_invoice_is_entered_return_it() throws ListWithoutHeaderExeption {

        List<String> lines = List.of(HEADER_LINE, String.join(",", "1", "21/03/2023", "1000", "930", "", "7","B76430134", ""));

        List<String> result = FILTER.apply(lines);

        assertEquals(lines, result);
    }
    @Test
    void if_there_is_iva_and_igic_on_an_invoice_delete_that_line() throws ListWithoutHeaderExeption {

        List<String> lines = List.of(HEADER_LINE, String.join(",", "1", "21/03/2023", "1000", "810", "21", "7","B76430134", ""));
        List<String> expectedResponse = List.of(HEADER_LINE);

        List<String> result = FILTER.apply(lines);

        assertEquals(expectedResponse, result);
    }
    @Test
    void if_the_net_value_is_wrongly_calculated_delete_that_invoice() throws ListWithoutHeaderExeption {

        List<String> lines = List.of(HEADER_LINE, String.join(",", "1", "21/03/2023", "1200", "968", "21", "","B76430134", ""));
        List<String> expectedResponse = List.of(HEADER_LINE);

        List<String> result = FILTER.apply(lines);

        assertEquals(expectedResponse, result);
    }

    @Test
    void if_cif_and_nif_exist_on_an_invoice_delete_that_line() throws ListWithoutHeaderExeption {

        List<String> lines = List.of(HEADER_LINE, String.join(",", "1", "21/03/2023", "1000", "930", "", "7","B76430134", "98102782L"));
        List<String> expectedResponse = List.of(HEADER_LINE);

        List<String> result = FILTER.apply(lines);

        assertEquals(expectedResponse, result);
    }

    @Test
    void if_the_invoice_number_is_duplicated_delete_those_lines() throws ListWithoutHeaderExeption {

        List<String> lines = List.of(HEADER_LINE,
                String.join(",", "1", "21/03/2023", "1000", "930", "", "7","", "98102782L"),
                String.join("," , "1", "20/03/2023", "1200", "948", "21", "","B76430134", ""),
                String.join(",", "2", "21/03/2023", "1000", "930", "", "7","", "98102782L"));
        List<String> expectedResponse = List.of(HEADER_LINE, String.join(",", "2", "21/03/2023", "1000", "930", "", "7","", "98102782L"));

        List<String> result = FILTER.apply(lines);

        assertEquals(expectedResponse, result);
    }

}