package com.mycompany.checklistatendimento.dao;

import com.mycompany.checklistatendimento.dto.CheckListAtendimentoDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe responsável pela gestão do banco de dados para o checklist de
 * atendimento.
 * <p>
 * Esta classe inclui métodos para verificar a existência e criar um banco de
 * dados SQLite, bem como criar as tabelas necessárias.
 * </p>
 *
 * @author Giuliano Vianna
 */
public class CheckListAtendimentoDAO {

    private static final String URL = "jdbc:sqlite:checklist.db";

    /**
     * Verifica a existência do banco de dados e cria as tabelas se necessário.
     * <p>
     * Este método estabelece uma conexão com o banco de dados SQLite. Se o
     * banco de dados não existir, ele será criado automaticamente. As tabelas
     * necessárias também são criadas se ainda não existirem.
     * </p>
     */
    public static void verificarECriarBancoDeDados() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                // O banco de dados não existe e será criado
                criarTabelas(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cria as tabelas no banco de dados.
     * <p>
     * Este método cria uma tabela 'checklist' com colunas para idChecklist,
     * cliente, empresa, status, descrição e data.
     * </p>
     *
     * @param conn A conexão com o banco de dados.
     * @throws SQLException Se ocorrer um erro SQL durante a criação das
     * tabelas.
     */
    private static void criarTabelas(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS checklist ("
                + "idChecklist INTEGER PRIMARY KEY,"
                + "cliente TEXT NOT NULL,"
                + "empresa TEXT NOT NULL,"
                + "status TEXT NOT NULL,"
                + "descricao TEXT,"
                + "data DATE NOT NULL"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Cadastra um novo checklist no banco de dados.
     * <p>
     * Este método insere um novo registro na tabela 'checklist' usando os dados
     * fornecidos através de um objeto {@code CheckListAtendimentoDTO}. Utiliza
     * um {@code PreparedStatement} para definir os valores de cada coluna na
     * tabela e executar a inserção. Em caso de erro na operação de banco de
     * dados, uma {@code SQLException} é capturada e uma mensagem de erro é
     * exibida ao usuário através de um {@code JOptionPane}.
     * </p>
     *
     * @param objDTO Objeto contendo os dados do checklist a ser cadastrado.
     * Espera-se que este objeto não seja {@code null} e que contenha todos os
     * dados necessários para o cadastro.
     */
    public void cadastrarChecklist(CheckListAtendimentoDTO objDTO) {

        String sql = "INSERT INTO checklist (cliente, empresa, status, "
                + "descricao, data) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Definindo os valores para cada parâmetro
            pstmt.setString(1, objDTO.getCliente());
            pstmt.setString(2, objDTO.getEmpresa());
            pstmt.setString(3, objDTO.getStatus());
            pstmt.setString(4, objDTO.getDescricao());
            pstmt.setDate(5, objDTO.getData());

            // Executando a inserção
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Checklist cadastrado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Checklist: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Recupera todos os registros de checklist do banco de dados.
     * <p>
     * Este método estabelece uma conexão com o banco de dados e executa uma
     * consulta SQL para recuperar todos os registros da tabela de checklist.
     * Cada registro é convertido em um objeto {@link CheckListAtendimentoDTO} e
     * adicionado a uma lista.
     * </p>
     * <p>
     * A lista de objetos {@code CheckListAtendimentoDTO} é então retornada. Em
     * caso de falha na execução da consulta ou na conexão com o banco de dados,
     * uma mensagem de erro é exibida e uma lista vazia é retornada.
     * </p>
     *
     * @return Uma lista de {@code CheckListAtendimentoDTO} contendo todos os
     * checklists registrados no banco de dados. A lista pode estar vazia se não
     * houver registros ou se ocorrer um erro.
     * @see CheckListAtendimentoDTO
     */
    public List<CheckListAtendimentoDTO> listarChecklists() {

        List<CheckListAtendimentoDTO> lista = new ArrayList<>();

        String sql = "SELECT * FROM checklist WHERE status <> 'Cancelado' AND status <> "
                + "'Finalizado' ORDER BY data";


        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CheckListAtendimentoDTO objDTO = new CheckListAtendimentoDTO();
                objDTO.setId(rs.getInt("idChecklist"));
                objDTO.setCliente(rs.getString("cliente"));
                objDTO.setEmpresa(rs.getString("empresa"));
                objDTO.setStatus(rs.getString("status"));
                objDTO.setDescricao(rs.getString("descricao"));
                objDTO.setData(rs.getDate("data"));
                lista.add(objDTO);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Listar Checklists: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    /**
     * Atualiza um registro de checklist existente no banco de dados.
     * <p>
     * Este método utiliza uma conexão SQL para atualizar um checklist
     * específico, identificado pelo seu ID, com os novos valores fornecidos.
     * </p>
     *
     * @param objDTO Objeto {@link CheckListAtendimentoDTO} contendo as
     * informações atualizadas do checklist. Deve incluir o ID do checklist a
     * ser atualizado, além dos novos valores para cliente, empresa, status,
     * descrição e data.
     * <p>
     * <b>Exemplo de Uso:</b>
     * <pre>
     * CheckListAtendimentoDTO checklist = new CheckListAtendimentoDTO();
     * checklist.setId(1); // ID do checklist a ser atualizado
     * checklist.setCliente("Novo Cliente");
     * checklist.setEmpresa("Nova Empresa");
     * checklist.setStatus("Atualizado");
     * checklist.setDescricao("Descrição atualizada");
     * checklist.setData(new Date());
     *
     * editarChecklist(checklist);
     * </pre>
     * </p>
     */
    public void editarChecklist(CheckListAtendimentoDTO objDTO) {

        // A SQL agora é uma operação de UPDATE
        String sql = "UPDATE checklist SET cliente = ?, empresa = ?, status = ?, "
                + "descricao = ?, data = ? WHERE idChecklist = ?";

        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Definindo os valores para cada parâmetro (os mesmos que no cadastro)
            pstmt.setString(1, objDTO.getCliente());
            pstmt.setString(2, objDTO.getEmpresa());
            pstmt.setString(3, objDTO.getStatus());
            pstmt.setString(4, objDTO.getDescricao());
            pstmt.setDate(5, objDTO.getData());

            // Definindo o ID do checklist a ser atualizado
            pstmt.setInt(6, objDTO.getId());

            // Executando a atualização
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Checklist atualizado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Editar Checklist: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exclui um checklist específico da base de dados.
     * <p>
     * Este método executa uma operação SQL de DELETE para remover um checklist
     * baseado em seu ID. É importante que o ID fornecido corresponda a um
     * checklist existente na base de dados.
     * <p>
     * Passos executados:
     * <ul>
     * <li>Estabelece uma conexão com o banco de dados.</li>
     * <li>Prepara uma instrução SQL de DELETE com o ID fornecido.</li>
     * <li>Executa a instrução e verifica se algum registro foi afetado.</li>
     * <li>Exibe uma mensagem ao usuário informando sobre o sucesso ou falha da
     * operação.</li>
     * </ul>
     * <p>
     * Em caso de exceção (como problemas de conexão ou SQL inválido), uma
     * mensagem de erro é exibida.
     *
     * @param objDTO O ID do checklist a ser excluído.
     * @see PreparedStatement
     * @see Connection
     */
    public void excluirChecklist(CheckListAtendimentoDTO objDTO) {

        // A SQL agora é uma operação de DELETE
        String sql = "DELETE FROM checklist WHERE idChecklist = ?";

        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Definindo o ID do checklist a ser excluído
            pstmt.setInt(1, objDTO.getId());

            // Executando a exclusão
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Checklist excluído com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum checklist encontrado com o ID fornecido.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Excluir Checklist: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lista todos os checklists com um determinado status.
     * <p>
     * Este método consulta a base de dados para obter todos os registros de
     * checklists que correspondem ao status fornecido. A lista é ordenada pela
     * data do checklist.
     * <p>
     * A consulta SQL utilizada filtra os registros pelo status e utiliza a
     * cláusula ORDER BY para organizar os resultados pela data.
     * <p>
     * Cada registro obtido é convertido em um objeto
     * {@link CheckListAtendimentoDTO} e adicionado à lista de retorno.
     *
     * @param status O status dos checklists a serem listados.
     * @return Uma lista de {@link CheckListAtendimentoDTO} correspondente ao
     * status fornecido.
     * @throws SQLException Se ocorrer um problema na consulta SQL ou na conexão
     * com o banco de dados.
     */
    public List<CheckListAtendimentoDTO> listarChecklistsPorStatus(String status) {

        List<CheckListAtendimentoDTO> lista = new ArrayList<>();

        // Modifica a SQL para filtrar por status
        String sql = "SELECT * FROM checklist WHERE status = ? ORDER BY data";

        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define o valor do parâmetro status na consulta
            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CheckListAtendimentoDTO objDTO = new CheckListAtendimentoDTO();
                    objDTO.setId(rs.getInt("idChecklist"));
                    objDTO.setCliente(rs.getString("cliente"));
                    objDTO.setEmpresa(rs.getString("empresa"));
                    objDTO.setStatus(rs.getString("status"));
                    objDTO.setDescricao(rs.getString("descricao"));
                    objDTO.setData(rs.getDate("data"));
                    lista.add(objDTO);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Listar Checklists: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

}
