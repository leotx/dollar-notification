package ltx.dollarnotification.utils;

import ltx.dollarnotification.model.Quotation;

public interface TaskDelegate {
    public void taskCompletionResult(Quotation quotation);
}
