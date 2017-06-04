/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

/**
 *
 * @author f98877a
 */
public interface CashFlowInfo {

    public static final String CATEGORY_TYPE_OUT = "EXPENSE";
    public static final String CATEGORY_TYPE_IN = "INCOME";
    public static final String POSITIVE = "POSITIVE";
    public static final String NEGATIVE = "NEGATIVE";

    public enum addScreenOp {
        ACCOUNT,
        EXPENSE,
        INCOME,
        MAXNUM
    }

    public static final String[] ExpColumns = new String[]{
        "Data", "Descrição", "Valor", "Categoria", "Conta"
    };

    public static final String[][] Categories = new String[][]{
        {"Remuneração", "INCOME", ""},
        {"Moradia", "EXPENSE", ""},
        {"Mercado", "EXPENSE"},
        {"Bebida", "EXPENSE"},
        {"Refeição", "EXPENSE"},
        {"Lazer", "EXPENSE"},
        {"Tv / Internet / Phone", "EXPENSE"},
        {"Compras", "EXPENSE"},
        {"Saúde", "EXPENSE"},
        {"Pagamento de cartão", "EXPENSE"},
        {"Investimentos", "EXPENSE"},
        {"Invest Imobiliario", "EXPENSE"},
        {"Viagem", "EXPENSE"},
        {"Saques", "EXPENSE"},
        {"Transporte", "EXPENSE"},
        {"Rendimento", "INCOME"}
    };
}
