package ltx.dollarnotification.model;

public class Quotation {
    private ValueQuotation bovespa;
    private ValueQuotation euro;
    private ValueQuotation dolar;

    public ValueQuotation getBovespa() {
        return bovespa;
    }

    public ValueQuotation getEuro() {
        return euro;
    }

    public ValueQuotation getDolar() {
        return dolar;
    }
}
