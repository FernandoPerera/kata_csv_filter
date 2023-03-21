package utils;

import exeptions.ListWithoutHeaderExeption;

import java.util.List;

public class CsvFilter {

    private final String HEADER_LINE = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente";

    public List<String> apply(List<String> lines) throws ListWithoutHeaderExeption {

        if (lines.isEmpty()){
            return lines;
        }

        boolean listContainHeader = !lines.get(0).equals(HEADER_LINE);

        if ( listContainHeader ){
            throw new ListWithoutHeaderExeption("ERROR: don't exist header in the list");
        }

        return lines;
    }

}
