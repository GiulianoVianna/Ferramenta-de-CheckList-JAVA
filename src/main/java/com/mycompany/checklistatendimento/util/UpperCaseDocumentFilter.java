package com.mycompany.checklistatendimento.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Filtro de documento para transformar automaticamente o texto inserido em
 * maiúsculas.
 * <p>
 * Este filtro pode ser aplicado a qualquer
 * {@link javax.swing.text.JTextComponent} (como {@link javax.swing.JTextField})
 * para garantir que todo texto digitado seja convertido para maiúsculas. Ele é
 * particularmente útil para campos onde a consistência da formatação do texto é
 * importante.
 * <p>
 * Este filtro sobrescreve os métodos {@code insertString} e {@code replace} da
 * classe {@link DocumentFilter}, modificando o texto recebido antes de
 * inseri-lo no documento do componente.
 *
 * @author Giuliano Vianna
 * @see DocumentFilter
 */
public class UpperCaseDocumentFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {
        text = text.toUpperCase();
        super.insertString(fb, offset, text, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        text = text.toUpperCase();
        super.replace(fb, offset, length, text, attrs);
    }
}
