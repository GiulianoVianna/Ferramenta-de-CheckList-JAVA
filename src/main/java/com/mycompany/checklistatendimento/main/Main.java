package com.mycompany.checklistatendimento.main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.checklistatendimento.dao.CheckListAtendimentoDAO;
import com.mycompany.checklistatendimento.view.CheckListAtendimentoView;

/**
 * Classe principal para iniciar a aplicação de Checklist de Atendimento.
 * Configura o tema Dark Flat e inicializa a interface gráfica.
 */
public class Main {

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

