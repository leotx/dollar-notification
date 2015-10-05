package ltx.dollarnotification.model;

public class Quotation {
    private ValueQuotation bovespa;
    private ValueQuotation euro;
    private ValueQuotation dolar;

    public ValueQuotation getBovespa() {
        return bovespa;
    }

    public void setBovespa(ValueQuotation bovespa) {
        this.bovespa = bovespa;
    }

    public ValueQuotation getEuro() {
        return euro;
    }

    public void setEuro(ValueQuotation euro) {
        this.euro = euro;
    }

    public ValueQuotation getDolar() {
        return dolar;
    }

    public void setDolar(ValueQuotation dolar) {
        this.dolar = dolar;
    }
}
