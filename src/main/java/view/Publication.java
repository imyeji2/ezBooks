/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import model.BookDAO;
import model.CategoryDAO;
import model.CategoryDTO;
import model.ContractDAO;
import model.InOrderTableDTO;
import model.StockTableViewDTO;

/**
 *
 * @author yj
 */
public class Publication extends javax.swing.JFrame implements ActionListener, ItemListener{
    private String contract_colNames[] = {"선택", "계약번호", "거래처명", "사업자 등록 번호", "담당자명", "부서", "계약일"};
    private String book_colNames[] = {"선택", "책번호", "카테고리", "책 이름", "저자명", "출판사", "금액"};
    private Object category_colNames[] = {"선택", "번호", "카테고리명"};
    private DefaultTableModel model = new DefaultTableModel();
    private CategoryDAO categoryDao;
    private ContractDAO contractDao;
    private int categoryState = 0;
    private BookDAO bookDao;
    private BookAddEdit bae;

    /** Creates new form publication */
    public Publication() {
        initComponents();
        init();
        addEvent();
        setLocationRelativeTo(null); //가운데 배치
    }
    
    private void init() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        categoryDao = new CategoryDAO();
        contractDao = new ContractDAO();
        bookDao = new BookDAO();

        tf_caNo.enable(false);
        tf_caName.enable(false);

        try {
            bookShowAll("b.bookNo");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String bookOrderByList[] = {"번호순", "가나다순"};
        DefaultComboBoxModel<String> bookOrderBy_CModel = new DefaultComboBoxModel<>(bookOrderByList);
        cb_bookOrderBy.setModel(bookOrderBy_CModel);

        String bookSelectList[] = {"책이름", "출판사명"};
        DefaultComboBoxModel<String> bookSelect_CModel = new DefaultComboBoxModel<>(bookSelectList);
        cb_bookSel.setModel(bookSelect_CModel);

        String contractOrderByList[] = {"계약일(오름차순)", "계약일(내림차순)"};
        DefaultComboBoxModel<String> contractOrderBy_CModel = new DefaultComboBoxModel<>(contractOrderByList);
        cb_conOrderBy.setModel(contractOrderBy_CModel);

        String contractSelectList[] = {"거래처명", "담당자명"};
        DefaultComboBoxModel<String> contractSelect_CModel = new DefaultComboBoxModel<>(contractSelectList);
        cb_conSel.setModel(contractSelect_CModel);
    }

    private void addEvent() {
        bt_bookAdd.addActionListener(this);
        bt_bookEdit.addActionListener(this);
        bt_bookSel.addActionListener(this);
        bt_bookDel.addActionListener(this);
        bt_bookRefresh.addActionListener(this);

        bt_caAdd.addActionListener(this);
        bt_caEdit.addActionListener(this);
        bt_caDel.addActionListener(this);
        bt_caSel.addActionListener(this);
        bt_ok.addActionListener(this);
        bt_cancel.addActionListener(this);

        bt_conAdd.addActionListener(this);
        bt_conEdit.addActionListener(this);
        bt_conDel.addActionListener(this);
        bt_conSel.addActionListener(this);
        bt_conRefresh.addActionListener(this);

        pane_manager.addMouseListener(new EventHandler());

        cb_bookOrderBy.addItemListener(this);
        cb_conOrderBy.addItemListener(this);
    }
    
 
  //======================================이벤트 핸들러=====================================================
       class EventHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == pane_manager) { //탭바꾸면 기존에 있던 탭에 있던 텍스트필드를 비우고 바꾼탭의 전체조회를 실행
                JTabbedPane tp = (JTabbedPane) e.getSource();
                int index = tp.getSelectedIndex();
                try {
                    if (index == 0) {
                        categoryClear();
                        contractClear();
                        bookShowAll("b.bookNo");
                    } else if (index == 1) {
                        bookClear();
                        contractClear();
                        categoryShowAll();
                    } else if (index == 2) {
                        bookClear();
                        categoryClear();
                        contractShowAll("asc");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public void itemStateChanged(ItemEvent e) {
        try {
            if (e.getSource() == cb_bookOrderBy) {
                if (cb_bookOrderBy.getSelectedIndex() == 0) {
                    bookShowAll("b.bookNo");
                } else {
                    bookShowAll("b.subject");
                }
            } else if (e.getSource() == cb_conOrderBy) {
                if (cb_conOrderBy.getSelectedIndex() == 0) {
                    contractShowAll("asc");
                } else {
                    contractShowAll("desc");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == bt_bookAdd) { //책등록
                bae = new BookAddEdit(BookAddEdit.BOOK_ADD, this);
                bae.setVisible(true);
            } else if (e.getSource() == bt_bookEdit) { //책수정
                int list[] = bookEdit();
                int caNo = list[1], index = list[0];
                if (index == 1) {
                    bae = new BookAddEdit(BookAddEdit.BOOK_EDIT, caNo, this);
                    bae.setVisible(true);
                }
            } else if (e.getSource() == bt_bookSel) { //책검색
                bookSelect();
            } else if (e.getSource() == bt_bookDel) { //책삭제
                bookDelete();
            } else if (e.getSource() == bt_bookRefresh) { //책 새로고침
                bookShowAll("b.bookNo");
            } else if (e.getSource() == bt_caDel) { //카테고리 삭제
                categoryDelete();
            } else if (e.getSource() == bt_caAdd) { //카테고리 추가에 필요한 준비
                tf_caName.enable(true);
                categoryState = 1;
            } else if (e.getSource() == bt_caEdit) { //카테고리 수정에 필요한 준비
                categoryEditCheck();
            } else if (e.getSource() == bt_ok && categoryState == 1) { //카테고리 추가
                categoryAdd();
            } else if (e.getSource() == bt_ok && categoryState == 2) { //카테고리 수정
                categoryEdit();
            } else if (e.getSource() == bt_cancel) { //취소
                categoryState = 0;
                categoryClear();
                tf_caName.enable(false);
            } else if (e.getSource() == bt_caSel) { //카테고리별 책검색
                categorySelectBook();
            } else if (e.getSource() == bt_conAdd) {
                ContractAdd ca = new ContractAdd(this);
                ca.setVisible(true);
            } else if (e.getSource() == bt_conEdit) {
                contractEdit();
            } else if (e.getSource() == bt_conDel) {
                contractDelete();
            } else if (e.getSource() == bt_conSel) {
                contractSelect();
            } else if (e.getSource() == bt_conRefresh) {
                contractShowAll("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


     

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pane_manager = new javax.swing.JTabbedPane();
        tab_book = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_book = new javax.swing.JTable();
        cb_bookOrderBy = new javax.swing.JComboBox<>();
        cb_bookSel = new javax.swing.JComboBox<>();
        tf_bookSel = new javax.swing.JTextField();
        bt_bookSel = new javax.swing.JButton();
        bt_bookAdd = new javax.swing.JButton();
        bt_bookEdit = new javax.swing.JButton();
        bt_bookDel = new javax.swing.JButton();
        bt_bookRefresh = new javax.swing.JButton();
        tab_category = new javax.swing.JPanel();
        plnput2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        tf_caNo = new javax.swing.JTextField();
        tf_caName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        bt_ok = new javax.swing.JButton();
        bt_cancel = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_category = new javax.swing.JTable();
        bt_caAdd = new javax.swing.JButton();
        bt_caEdit = new javax.swing.JButton();
        bt_caDel = new javax.swing.JButton();
        bt_caSel = new javax.swing.JButton();
        tab_contract = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_contract = new javax.swing.JTable();
        cb_conOrderBy = new javax.swing.JComboBox<>();
        cb_conSel = new javax.swing.JComboBox<>();
        tf_conSel = new javax.swing.JTextField();
        bt_conSel = new javax.swing.JButton();
        bt_conAdd = new javax.swing.JButton();
        bt_conEdit = new javax.swing.JButton();
        bt_conDel = new javax.swing.JButton();
        bt_conRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("거래처관리");

        table_book.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "선택", "책번호", "카테고리", "제목", "저자", "출판수", "금액"
            }
        ));
        table_book.setCellSelectionEnabled(true);
        table_book.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table_book.setDragEnabled(true);
        table_book.setRowHeight(25);
        table_book.setShowGrid(true);
        jScrollPane1.setViewportView(table_book);

        cb_bookOrderBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "정렬", "번호순", "가나다순" }));

        cb_bookSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "책이름", "출판사명" }));

        bt_bookSel.setText("검색");

        bt_bookAdd.setText("추가");

        bt_bookEdit.setText("수정");

        bt_bookDel.setText("삭제");

        bt_bookRefresh.setText("전체조회");
        bt_bookRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_bookRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab_bookLayout = new javax.swing.GroupLayout(tab_book);
        tab_book.setLayout(tab_bookLayout);
        tab_bookLayout.setHorizontalGroup(
            tab_bookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_bookLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(tab_bookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_bookLayout.createSequentialGroup()
                        .addComponent(cb_bookOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_bookSel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_bookSel, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_bookSel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_bookAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_bookEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(bt_bookDel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bt_bookRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        tab_bookLayout.setVerticalGroup(
            tab_bookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_bookLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bt_bookRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab_bookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_bookOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_bookSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_bookSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_bookSel)
                    .addComponent(bt_bookAdd)
                    .addComponent(bt_bookEdit)
                    .addComponent(bt_bookDel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        pane_manager.addTab("도서관리", tab_book);

        plnput2.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(153, 153, 153)));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("카테고리번호");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("카테고리명");

        bt_ok.setText("확인");

        bt_cancel.setText("취소");

        javax.swing.GroupLayout plnput2Layout = new javax.swing.GroupLayout(plnput2);
        plnput2.setLayout(plnput2Layout);
        plnput2Layout.setHorizontalGroup(
            plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plnput2Layout.createSequentialGroup()
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plnput2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(bt_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(plnput2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(plnput2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_caName, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(plnput2Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_caNo, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        plnput2Layout.setVerticalGroup(
            plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plnput2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_caNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_caName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plnput2Layout.createSequentialGroup()
                        .addComponent(bt_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(bt_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        table_category.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "선택", "번호", "카테고리명"
            }
        ));
        table_category.setCellSelectionEnabled(true);
        table_category.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table_category.setDragEnabled(true);
        table_category.setRowHeight(25);
        table_category.setShowGrid(true);
        jScrollPane4.setViewportView(table_category);

        bt_caAdd.setText("등록");

        bt_caEdit.setText("수정");

        bt_caDel.setText("삭제");

        bt_caSel.setText("책 조회");

        javax.swing.GroupLayout tab_categoryLayout = new javax.swing.GroupLayout(tab_category);
        tab_category.setLayout(tab_categoryLayout);
        tab_categoryLayout.setHorizontalGroup(
            tab_categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_categoryLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(plnput2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_categoryLayout.createSequentialGroup()
                        .addComponent(bt_caAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_caEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_caDel, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_caSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(36, 36, 36))
        );
        tab_categoryLayout.setVerticalGroup(
            tab_categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_categoryLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(tab_categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_categoryLayout.createSequentialGroup()
                        .addGroup(tab_categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bt_caAdd)
                            .addComponent(bt_caEdit)
                            .addComponent(bt_caDel)
                            .addComponent(bt_caSel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(plnput2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        pane_manager.addTab("카테고리 관리", tab_category);

        table_contract.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "선택", "계약번호", "거래처명", "사업자등록번호", "담당자명", "부서", "계약일"
            }
        ));
        table_contract.setCellSelectionEnabled(true);
        table_contract.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table_contract.setDragEnabled(true);
        table_contract.setRowHeight(25);
        table_contract.setShowGrid(true);
        jScrollPane3.setViewportView(table_contract);

        cb_conOrderBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "계약일 정렬", "오름차순", "내림차순" }));

        cb_conSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "거래처명", "담당자명" }));

        bt_conSel.setText("검색");

        bt_conAdd.setText("추가");

        bt_conEdit.setText("수정");

        bt_conDel.setText("삭제");

        bt_conRefresh.setText("전체조회");

        javax.swing.GroupLayout tab_contractLayout = new javax.swing.GroupLayout(tab_contract);
        tab_contract.setLayout(tab_contractLayout);
        tab_contractLayout.setHorizontalGroup(
            tab_contractLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_contractLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(tab_contractLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_contractLayout.createSequentialGroup()
                        .addComponent(cb_conOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_conSel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_conSel, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_conSel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_conAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_conEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(bt_conDel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bt_conRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        tab_contractLayout.setVerticalGroup(
            tab_contractLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_contractLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bt_conRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab_contractLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_conOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_conSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_conSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_conSel)
                    .addComponent(bt_conAdd)
                    .addComponent(bt_conEdit)
                    .addComponent(bt_conDel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        pane_manager.addTab("계약관리", tab_contract);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pane_manager)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(pane_manager, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_bookRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_bookRefreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_bookRefreshActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
             Publication f= new Publication();
             f.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_bookAdd;
    private javax.swing.JButton bt_bookDel;
    private javax.swing.JButton bt_bookEdit;
    private javax.swing.JButton bt_bookRefresh;
    private javax.swing.JButton bt_bookSel;
    private javax.swing.JButton bt_caAdd;
    private javax.swing.JButton bt_caDel;
    private javax.swing.JButton bt_caEdit;
    private javax.swing.JButton bt_caSel;
    private javax.swing.JButton bt_cancel;
    private javax.swing.JButton bt_conAdd;
    private javax.swing.JButton bt_conDel;
    private javax.swing.JButton bt_conEdit;
    private javax.swing.JButton bt_conRefresh;
    private javax.swing.JButton bt_conSel;
    private javax.swing.JButton bt_ok;
    private javax.swing.JComboBox<String> cb_bookOrderBy;
    private javax.swing.JComboBox<String> cb_bookSel;
    private javax.swing.JComboBox<String> cb_conOrderBy;
    private javax.swing.JComboBox<String> cb_conSel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane pane_manager;
    private javax.swing.JPanel plnput2;
    private javax.swing.JPanel tab_book;
    private javax.swing.JPanel tab_category;
    private javax.swing.JPanel tab_contract;
    private javax.swing.JTable table_book;
    private javax.swing.JTable table_category;
    private javax.swing.JTable table_contract;
    private javax.swing.JTextField tf_bookSel;
    private javax.swing.JTextField tf_caName;
    private javax.swing.JTextField tf_caNo;
    private javax.swing.JTextField tf_conSel;
    // End of variables declaration//GEN-END:variables

//--------------------------기능 메서드----------------------------------------------
 

    //책 삭제
    private void bookDelete() throws SQLException {
        List<Integer> checkArr = new ArrayList<>();
        List<Integer> checkRowNum = new ArrayList<>();
        int cnt = 0;

        //선택된 체크박스열 인덱스 구하기
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            if (bool) {
                checkRowNum.add(i);
            }
        }

        if (checkRowNum.size() == 0) {
            JOptionPane.showMessageDialog(this, "데이터를 선택해주세요");
            return;
        }

        int type = JOptionPane.showConfirmDialog(this, "정말로 삭제하시겠습니까?", "삭제확인", JOptionPane.YES_NO_OPTION);

        if (type == JOptionPane.YES_OPTION) {
            for (int i = 0; i < checkRowNum.size(); i++) {
                checkArr.add((Integer) model.getValueAt(checkRowNum.get(i), 1));
                cnt = bookDao.bookDelete(checkArr.get(i));
            }
        }

        if (cnt > 0) {
            JOptionPane.showMessageDialog(this, "삭제성공");
            bookShowAll("b.bookNo");
        } else {
            JOptionPane.showMessageDialog(this, "삭제실패");
        }
    }

    //책 수정선택
    private int[] bookEdit() {
        int caNo = 0;
        int index = 0;

        int list[] = new int[2];

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            if (bool) {
                index++;
            }
        }
        list[0] = index;
        if (index == 0) {
            JOptionPane.showMessageDialog(this, "데이터를 선택해 주세요");
        } else if (index > 1) {
            JOptionPane.showMessageDialog(this, "하나의 데이터만 선택하세요");
        } else {
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean bool = (boolean) model.getValueAt(i, 0);
                if (bool) {
                    caNo = (int) model.getValueAt(i, 1);
                    list[1] = caNo;
                }
                System.out.println("책번호구하기: " + caNo);
            }
        }
        return list;
    }

   

    // 카테고리 추가
    private void categoryAdd() throws SQLException {
        String caName = tf_caName.getText();

        CategoryDTO dto = new CategoryDTO();
        dto.setCaName(caName);

        List<CategoryDTO> list = categoryDao.categoryShowAll();

        //카테고리명 중복체크
        for (int i = 0; i < list.size(); i++) {
            String categoryName = list.get(i).getCaName();
            if (caName.equals(categoryName)) {
                JOptionPane.showMessageDialog(this, "이미 있는 카테고리명 입니다.");
                tf_caName.requestFocus();
                return;
            }
        }

        int type = JOptionPane.showConfirmDialog(this, "등록가능한 카테고리명 입니다\n"
                + "등록하시겟습니까?", "카테고리명 확인", JOptionPane.YES_NO_OPTION);

        if (type == JOptionPane.YES_OPTION) {
            int cnt = categoryDao.categoryAdd(dto);
            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "카테고리를 추가하였습니다.");
                tf_caName.enable(false);
                categoryClear();
                categoryShowAll();
            } else {
                JOptionPane.showMessageDialog(this, "카테고리추가가 실패하였습니다");
                tf_caName.requestFocus();
            }
        }
    }

    //카테고리 수정전 체크
    private void categoryEditCheck() {
        int index = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            if (bool) {
                index++;
            }
        }

        if (index == 0) {
            JOptionPane.showMessageDialog(this, "데이터를 선택해 주세요");
            return;
        } else if (index > 1) {
            JOptionPane.showMessageDialog(this, "하나의 데이터만 선택해 주세요");
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            int caNo = (int) model.getValueAt(i, 1);
            String caName = (String) model.getValueAt(i, 2);
            if (bool) {
                tf_caNo.setText(caNo + "");
                tf_caName.setText(caName);
                tf_caName.enable(true);
                categoryState = 2;
            }
        }
    }

    //카테고리 수정
    private void categoryEdit() throws SQLException {
        int caNo = Integer.parseInt(tf_caNo.getText());
        String caName = tf_caName.getText();

        CategoryDTO dto = new CategoryDTO(caNo, caName);

        //카테고리명 중복확인
        List<CategoryDTO> list = categoryDao.categoryShowAll();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCaName().equals(caName)) {
                JOptionPane.showMessageDialog(this, "이미 존재하는 카테고리명 입니다.");
                tf_caName.requestFocus();
                return;
            }
        }

        int type = JOptionPane.showConfirmDialog(this, "변경가능한 카테고리명 입니다"
                + "변경하시겠습니까?", "카테고리변경 확인", JOptionPane.YES_NO_OPTION);

        if (type == JOptionPane.YES_OPTION) {
            int cnt = categoryDao.categoryEdit(dto);
            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "카테고리명 변경 성공");
                tf_caName.enable(false);
                categoryShowAll();
                categoryClear();
            } else {
                JOptionPane.showMessageDialog(this, "카테고리명 변경 실패");
            }
        }
    }

    //카테고리 삭제
    private void categoryDelete() throws SQLException {
        List<Integer> checkArr = new ArrayList<>();
        List<Integer> checkRowNum = new ArrayList<>();
        int cnt = 0;

        //선택된 체크박스열 인덱스 구하기
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            if (bool) {
                checkRowNum.add(i);
            }
        }

        if (checkRowNum.size() == 0) {
            JOptionPane.showMessageDialog(this, "데이터를 선택해주세요");
            return;
        }

        int type = JOptionPane.showConfirmDialog(this, "정말로 삭제하시겠습니까?", "삭제확인", JOptionPane.YES_NO_OPTION);

        if (type == JOptionPane.YES_OPTION) {
            for (int i = 0; i < checkRowNum.size(); i++) {
                checkArr.add((Integer) model.getValueAt(checkRowNum.get(i), 1));
                cnt = categoryDao.categoryDelete(checkArr.get(i));
            }
        }

        if (cnt > 0) {
            JOptionPane.showMessageDialog(this, "삭제성공");
            categoryShowAll();
        } else {
            JOptionPane.showMessageDialog(this, "삭제실패");
        }
    }

    //카테고리별 책종류 팝업
    private void categorySelectBook() {
        int index = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            if (bool) {
                index++;
            }
        }

        if (index == 0) {
            JOptionPane.showMessageDialog(this, "데이터를 선택해주세요");
            return;
        } else if (index > 1) {
            JOptionPane.showMessageDialog(this, "한개의 데이터만 선택해주세요");
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            int caNo = (int) model.getValueAt(i, 1);
            String caName = (String) model.getValueAt(i, 2);

            if (bool) {
                CategorySelect cs = new CategorySelect(caNo, caName);
                cs.setVisible(true);
            }
        }
    }

   

    //계약수정
    public void contractEdit() {
        String conNo = "";
        int index = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            if (bool) {
                index++;
            }
        }

        if (index == 0) {
            JOptionPane.showMessageDialog(this, "데이터를 선택해 주세요");
            return;
        } else if (index > 1) {
            JOptionPane.showMessageDialog(this, "하나의 데이터만 선택하세요");
            return;
        } else {
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean bool = (boolean) model.getValueAt(i, 0);
                if (bool) {
                    conNo = (String) model.getValueAt(i, 1);
                    ContractEdit ce = new ContractEdit(conNo, this);
                    ce.setVisible(true);
                }
                System.out.println("책번호구하기: " + conNo);
            }
        }
    }

    //계약삭제
    private void contractDelete() throws SQLException {
        List<String> checkArr = new ArrayList<>();
        List<Integer> checkRowNum = new ArrayList<>();
        int cnt = 0;

        //선택된 체크박스열 인덱스 구하기
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            if (bool) {
                checkRowNum.add(i);
            }
        }

        if (checkRowNum.size() == 0) {
            JOptionPane.showMessageDialog(this, "데이터를 선택해주세요");
            return;
        }

        int type = JOptionPane.showConfirmDialog(this, "정말로 삭제하시겠습니까?", "삭제확인", JOptionPane.YES_NO_OPTION);

        if (type == JOptionPane.YES_OPTION) {
            for (int i = 0; i < checkRowNum.size(); i++) {
                checkArr.add((String) model.getValueAt(checkRowNum.get(i), 1));
                cnt = contractDao.contractDelete(checkArr.get(i));
            }
        }

        if (cnt > 0) {
            JOptionPane.showMessageDialog(this, "삭제성공");
            contractShowAll("asc");
        } else {
            JOptionPane.showMessageDialog(this, "삭제실패");
        }
    }

    
    
/*---------------------------------테이블 디자인 관련(출력포함)--------------------------------------*/
    
  //책 전체조회
    public void bookShowAll(String bookOrderBy) throws SQLException {

        List<Map> list = bookDao.bookshowAll(bookOrderBy);

        Object data[][] = new Object[list.size()][book_colNames.length];

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);

            data[i][0] = false;
            data[i][1] = map.get("bookNo");
            data[i][2] = map.get("caName");
            data[i][3] = map.get("subject");
            data[i][4] = map.get("writer");
            data[i][5] = map.get("puName");
            data[i][6] = map.get("price");
        }

        model.setDataVector(data, book_colNames);
        table_book.setModel(model);

        DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
        table_book.getColumn("선택").setCellRenderer(renderer);
        table_book.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table_book.getTableHeader().setReorderingAllowed(false);
        table_book.getTableHeader().setResizingAllowed(false);
        bookColumnSize();
        //tableCellCenter(table_book);
    }

    //책검색
    private void bookSelect() throws SQLException {
        String BookSel = (String) cb_bookSel.getSelectedItem();
        String input = tf_bookSel.getText();
        String userInput = "%" + input + "%";

        if (input == null || input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색내용을 입력해 주세요");
            tf_bookSel.requestFocus();
            return;
        }

        if (BookSel.equals("책이름")) {
            BookSel = "subject";
        } else {
            BookSel = "p.puName";
        }

        List<Map> list = bookDao.bookSelect(BookSel, userInput);

        Object data[][] = new Object[list.size()][book_colNames.length];

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "데이터가 없습니다");
            tf_bookSel.requestFocus();
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);

            data[i][0] = false;
            data[i][1] = map.get("bookNo");
            data[i][2] = map.get("caName");
            data[i][3] = map.get("subject");
            data[i][4] = map.get("writer");
            data[i][5] = map.get("puName");
            data[i][6] = map.get("price");

            System.out.println("출력 테스트" + map);
        }

        model.setDataVector(data, book_colNames);
        table_book.setModel(model);

        DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
        table_book.getColumn("선택").setCellRenderer(renderer);
        table_book.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table_book.getTableHeader().setReorderingAllowed(false);
        table_book.getTableHeader().setResizingAllowed(false);

        bookColumnSize();
        //tableCellCenter(table_book);
    }
    
     //카테고리 전체조회
    private void categoryShowAll() throws SQLException {
        List<CategoryDTO> list = categoryDao.categoryShowAll();

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "데이터가 없습니다");
            return;
        }

        Object data[][] = new Object[list.size()][category_colNames.length];

        for (int i = 0; i < list.size(); i++) {
            CategoryDTO dto = list.get(i);

            data[i][0] = false;
            data[i][1] = dto.getCaNo();
            data[i][2] = dto.getCaName();
        }

        model.setDataVector(data, category_colNames);
        table_category.setModel(model);

        DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
        table_category.getColumn("선택").setCellRenderer(renderer);
        table_category.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table_category.getTableHeader().setReorderingAllowed(false);
        table_category.getTableHeader().setResizingAllowed(false);
        categoryColumnSize();
        //tableCellCenter(table_category);
    }  
     //계약전체조회
    public void contractShowAll(String contractOrderBy) throws SQLException {
        List<Map> list = contractDao.contractShowAll(contractOrderBy);

        Object data[][] = new Object[list.size()][contract_colNames.length];

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);

            data[i][0] = false;
            data[i][1] = map.get("conNo");
            data[i][2] = map.get("puName");
            data[i][3] = map.get("puId");
            data[i][4] = map.get("name");
            data[i][5] = map.get("deptName");
            data[i][6] = map.get("conDate");
        }

        model.setDataVector(data, contract_colNames);
        table_contract.setModel(model);

        DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
        table_contract.getColumn("선택").setCellRenderer(renderer);
        table_contract.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table_contract.getTableHeader().setReorderingAllowed(false);
        table_contract.getTableHeader().setResizingAllowed(false);
        contractColumnsize();
        //tableCellCenter(table_contract);
    }

    //계약검색
    private void contractSelect() throws SQLException {
        String input = "%" + tf_conSel.getText() + "%";
        int index = cb_conSel.getSelectedIndex();
        String conSel = "";

        if (index == 0) {
            conSel = "p.puName";
        } else if (index == 1) {
            conSel = "e.name";
        }

        List<Map> list = contractDao.contractSelect(conSel, input);

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색결과가 없습니다.");
            tf_conSel.requestFocus();
        }

        Object data[][] = new Object[list.size()][contract_colNames.length];

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);

            data[i][0] = false;
            data[i][1] = map.get("conNo");
            data[i][2] = map.get("puName");
            data[i][3] = map.get("puId");
            data[i][4] = map.get("name");
            data[i][5] = map.get("deptName");
            data[i][6] = map.get("conDate");

            model.setDataVector(data, contract_colNames);
            table_contract.setModel(model);

            DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
            table_contract.getColumn("선택").setCellRenderer(renderer);
            table_contract.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
            table_contract.getTableHeader().setReorderingAllowed(false);
            table_contract.getTableHeader().setResizingAllowed(false);
            contractColumnsize();
            tableCellCenter(table_contract);
        }
    }
    
    //책테이블 컬럼크기 조정
    private void bookColumnSize() {
        TableColumn col1 = table_book.getColumnModel().getColumn(0);
        TableColumn col2 = table_book.getColumnModel().getColumn(1);
        TableColumn col3 = table_book.getColumnModel().getColumn(2);
        TableColumn col4 = table_book.getColumnModel().getColumn(3);
        TableColumn col5 = table_book.getColumnModel().getColumn(4);
        TableColumn col6 = table_book.getColumnModel().getColumn(5);
        TableColumn col7 = table_book.getColumnModel().getColumn(6);

        col1.setPreferredWidth(10);
        col2.setPreferredWidth(10);
        col3.setPreferredWidth(50);
        col4.setPreferredWidth(130);
        col5.setPreferredWidth(50);
        col6.setPreferredWidth(70);
        col7.setPreferredWidth(60);
    }

    //카테고리테이블 컬럼크기 조정
    private void categoryColumnSize() {
        TableColumn col1 = table_category.getColumnModel().getColumn(0);
        TableColumn col2 = table_category.getColumnModel().getColumn(1);
        TableColumn col3 = table_category.getColumnModel().getColumn(2);

        col1.setPreferredWidth(10);
        col2.setPreferredWidth(10);
        col3.setPreferredWidth(140);
    }

    //계약테이블 컬럼크기 조정
    private void contractColumnsize() {
        TableColumn col1 = table_contract.getColumnModel().getColumn(0);
        TableColumn col2 = table_contract.getColumnModel().getColumn(1);
        TableColumn col3 = table_contract.getColumnModel().getColumn(2);
        TableColumn col4 = table_contract.getColumnModel().getColumn(3);
        TableColumn col5 = table_contract.getColumnModel().getColumn(4);
        TableColumn col6 = table_contract.getColumnModel().getColumn(5);
        TableColumn col7 = table_contract.getColumnModel().getColumn(6);
        
        col1.setPreferredWidth(10);
        col2.setPreferredWidth(100);
        col3.setPreferredWidth(70);
        col4.setPreferredWidth(70);
        col5.setPreferredWidth(30);
        col6.setPreferredWidth(30);
        col7.setPreferredWidth(60);
    }

    private void bookClear() {
        tf_bookSel.setText(" ");
    }

    private void categoryClear() {
        tf_caNo.setText(" ");
        tf_caName.setText(" ");
    }

    private void contractClear() {
        tf_conSel.setText(" ");
    }

    //체크박스 설정   
    class MyDefaultTableCellRenderer extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected,
                                                                boolean hasFocus,int row, int column) {
            /*JTable table : 현재 작업 중인 JTable
            Object value : 현재 작업 중인 JTable의 셀객체
            int row : 현재 작업 중인 row번호
            int column : 현재 작업 중인 column번호*/

            if(column==0){
                    JCheckBox comp = null;
                    comp = new JCheckBox();
                    comp.setSelected(((Boolean)value).booleanValue());	 
                    comp.setHorizontalAlignment(SwingConstants.CENTER);//가운데 정렬
                    return comp;		
            }else {
                    return null;
            }
        }
    }//class


   // 테이블 가운데 정렬하기
    public void tableCellCenter(JTable t){
        
        //테이블 해더 가운데 정렬
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)t.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        t.getTableHeader().setDefaultRenderer(renderer);

        //셀 가운데 정렬
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumnModel tcm = t.getColumnModel();
         
        //컬럼 갯수만큼 컬럼 가져와 for문으로 각 행에 대한 정렬
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            tcm.getColumn(i).setCellRenderer(dtcr);
        }
    }   


}
