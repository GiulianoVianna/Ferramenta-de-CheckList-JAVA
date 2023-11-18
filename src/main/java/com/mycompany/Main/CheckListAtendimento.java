package com.mycompany.Main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.DAO.CheckListAtendimentoDAO;
import com.mycompany.View.CheckListAtendimentoView;

/**
 * Classe principal para iniciar a aplicação de Checklist de Atendimento.
 * Configura o tema Dark Flat e inicializa a interface gráfica.
 */
public class CheckListAtendimento {

    public static void main(String[] args) {

        // Configura o tema Dark Flat antes de criar a interface gráfica
        FlatDarkLaf.setup();

        // Verifica e cria o banco de dados, se necessário
        CheckListAtendimentoDAO.verificarECriarBancoDeDados();

        // Cria e exibe a janela principal da aplicação
        CheckListAtendimentoView objCheckListAtendimento = new CheckListAtendimentoView();
        objCheckListAtendimento.setVisible(true);
        objCheckListAtendimento.setLocationRelativeTo(null);
    }
}

