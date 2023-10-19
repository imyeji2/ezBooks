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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.RefundDAO;
import model.RefundDTO;
import model.SaleDAO;
import model.SaleDTO;

/**
 *
 * @author yj
 */
public class SaleGUI extends javax.swing.JFrame implements ActionListener {

    private SaleDAO saleDao;
    private RefundDAO refundDao = new RefundDAO();
    private JCheckBox checkBox;
    private DefaultTableModel model = new DefaultTableModel();
    private String[] colName = {"선택", "판매번호", "아이디", "책번호", "책이름", "판매금액", "처리 상태", "판매일자"};
    private String[] colName2 = {"선택", "환불번호", "판매번호", "판매금액", "처리상태", "환불일자"};

    /**
     * Creates new form SaleGUI
     */
    public SaleGUI() {
        initComponents();
        init();
        addEvent();
        setLocationRelativeTo(null); //가운데 배치
    }

    private void init() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        saleDao = new SaleDAO();
        
        try {
            showAllSell();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tb_sell.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    //이벤트 처리
    private void addEvent() {
        bt_search.addActionListener(this);
        bt_search2.addActionListener(this);
        bt_refund.addActionListener(this);
        bt_reEdit.addActionListener(this);
        bt_sellAdd.addActionListener(this);
        bt_sellComplete.addActionListener(this);
        bt_showAll1.addActionListener(this);

        cb_state1.addItemListener(new EventHandler());
        cb_state2.addItemListener(new EventHandler());
        cb_sort.addItemListener(new EventHandler());
        cb_sort3.addItemListener(new EventHandler());
        jTabbedPane1.addMouseListener(new EventHandler());
        tf_search.addKeyListener(new MyKeyListener());
    }

    //======================================이벤트 핸들러=====================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bt_search) {           //판매관리 검색
            try {
                SellSearch();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } else if (e.getSource() == bt_refund) { //판매관리 수정
            try { 
                refund();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == bt_reEdit) {   //환불관리 수정  
            try {
            refundEdit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == bt_search2) {    //환불관리 검색
            try {
                refundSearch();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == bt_sellAdd) { //판매 추가등록
            SellAdd();
        } else if (e.getSource() == bt_sellComplete) { //판매완료 
            try {
                sellComplete();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource()==bt_showAll1){
            try {
                showAllSell();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    class EventHandler extends MouseAdapter implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == cb_sort) { //컬럼별 정렬 
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = cb_sort.getSelectedIndex(); //index값 받아와서 
                    System.out.println("선택한 index 값" + index);
                    try {
                        switch (index) {
                            case 0:
                                sortDate(); // 0이면 일자별 정렬
                                break;
                            case 1:
                                sortPrice(); // 1이면 가격별 정렬
                                break;
                            case 2:
                                sortSubject(); //2 제목별 정렬
                                break;
                        }//switch
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }// 중첩if
            } else if (e.getSource() == cb_state1) { // 상태별 정리 인덱스값 받아와서 처리
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = cb_state1.getSelectedIndex();
                    System.out.println("선택한 index 값" + index);
                    try {
                        String selldate = (String) cb_state1.getSelectedItem(); //선택한 아이템 텍스트 받아와서 검색
                        switch (index) {
                            case 0:
                                sortState(); //상태별로 내림차순 정렬
                                break;
                            case 1:
                            case 2:
                                sortState2(selldate); //상태별로(판매처리중,판매완료) 정렬
                                break;
                        }//switch
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            } else if (e.getSource() == cb_state2) { //환불 처리 '상태' 정렬

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = cb_state2.getSelectedIndex(); //선택한 콤보박스 인덱스값 받아와서
                    System.out.println("선택한 index 값=" + index);
                    try {
                        String sortState = (String) cb_state2.getSelectedItem();
                        switch (index) {
                            case 0:
                                sortState3(); //인덱스값이 0이면 처리상태 정렬
                                break;
                            case 1:
                            case 2:
                                sortState4(sortState); // 인덱스값이 1,2면 선택한 상태 텍스트받아서 정렬
                                break;
                        }//switch
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }//try catch

                }

            } else if (e.getSource() == cb_sort3) {    // 환불 아이템별(판매일,금액)순 정리
                int index = cb_sort3.getSelectedIndex(); //선택한 인덱스값 받아와서
                try {

                    switch (index) { 
                        case 0:
                            refundSortDate(); //인덱스값이 0이면 판매일 정렬
                            break;
                        case 1:
                            refundSortPrice(); // 인덱스값이 1이면 금액순 정렬
                            break;
                    }//switch

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }// try-catch

            }//if
        }//메서드

        @Override
        public void mouseClicked(MouseEvent e) {
            JTabbedPane tp = (JTabbedPane) e.getSource();
            int index = tp.getSelectedIndex();
            System.out.println("index=" + index);
            //판매 페이지 초기화 
            if (index == 0) { //선택한값이 (0)판매관리 면
                try {
                    showAllSell(); //테이블 초기화
                    tf_search.setText(""); // 텍스트 비우기
                    cb_sort.setSelectedIndex(0); // 콤보박스 기본값
                    cb_state1.setSelectedIndex(0); // 콤보박스 기본값
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            } else if (index == 1) { //선택한 인덱스값이 (1)환불관리테이블이면
               
                try {
                    System.out.println("환불페이지 초기화");
                    showAllRefund();  //환불 페이지 초기화
                    tf_search2.setText(""); //텍스트 정리
                    cb_sort3.setSelectedIndex(0); //콤보박스 정리
                    cb_state2.setSelectedIndex(0); //콤보박스 정리
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        }
        
        

    }//class

    class MyKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            try {
                switch (keyCode) {
                    case KeyEvent.VK_ENTER: // 키코드가 엔터면
                        SellSearch(); // 판매관리테이블 검색
                        break;

                }//switch
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }//class

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jp_inOrder = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_sell = new javax.swing.JTable();
        cb_state1 = new javax.swing.JComboBox<>();
        cb_sort = new javax.swing.JComboBox<>();
        tf_search = new javax.swing.JTextField();
        bt_search = new javax.swing.JButton();
        bt_sellAdd = new javax.swing.JButton();
        bt_refund = new javax.swing.JButton();
        bt_showAll1 = new javax.swing.JButton();
        bt_sellComplete = new javax.swing.JButton();
        jp_stock = new javax.swing.JPanel();
        cb_state2 = new javax.swing.JComboBox<>();
        cb_sort3 = new javax.swing.JComboBox<>();
        tf_search2 = new javax.swing.JTextField();
        bt_search2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_refund = new javax.swing.JTable();
        bt_reEdit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("판매관리");

        jTabbedPane1.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        tb_sell.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        tb_sell.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "선택", "판매번호", "아이디", "책번호", "제목", "판매금액", "처리상태", "판매일자"
            }
        ));
        tb_sell.setCellSelectionEnabled(true);
        tb_sell.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tb_sell.setDragEnabled(true);
        tb_sell.setRowHeight(25);
        tb_sell.setShowGrid(true);
        jScrollPane1.setViewportView(tb_sell);

        cb_state1.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_state1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "처리상태", "판매완료", "판매처리중" }));
        cb_state1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_state1ActionPerformed(evt);
            }
        });

        cb_sort.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_sort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "판매일순", "판매금액", "책이름" }));

        tf_search.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        bt_search.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_search.setText("검색");

        bt_sellAdd.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_sellAdd.setText("추가");

        bt_refund.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_refund.setText("환불신청");

        bt_showAll1.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_showAll1.setText("전체조회");

        bt_sellComplete.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_sellComplete.setText("판매완료");
        bt_sellComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_sellCompleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp_inOrderLayout = new javax.swing.GroupLayout(jp_inOrder);
        jp_inOrder.setLayout(jp_inOrderLayout);
        jp_inOrderLayout.setHorizontalGroup(
            jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_inOrderLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_inOrderLayout.createSequentialGroup()
                        .addComponent(cb_state1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_sort, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_search, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jp_inOrderLayout.createSequentialGroup()
                                .addComponent(bt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_sellAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bt_showAll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bt_sellComplete)
                            .addComponent(bt_refund))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jp_inOrderLayout.setVerticalGroup(
            jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_inOrderLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_refund)
                    .addComponent(bt_showAll1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_state1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_sort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_search)
                    .addComponent(bt_sellAdd)
                    .addComponent(bt_sellComplete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("판매관리", jp_inOrder);

        cb_state2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_state2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "처리상태", "환불처리중", "환불완료" }));

        cb_sort3.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_sort3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "환불일순", "판매금액순" }));

        tf_search2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        bt_search2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_search2.setText("검색");

        tb_refund.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        tb_refund.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "재고번호", "제목", "출판사", "수량", "입고일자", "재고상태"
            }
        ));
        tb_refund.setCellSelectionEnabled(true);
        tb_refund.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tb_refund.setDragEnabled(true);
        tb_refund.setRowHeight(25);
        tb_refund.setShowGrid(true);
        jScrollPane3.setViewportView(tb_refund);

        bt_reEdit.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_reEdit.setText("환불완료");

        javax.swing.GroupLayout jp_stockLayout = new javax.swing.GroupLayout(jp_stock);
        jp_stock.setLayout(jp_stockLayout);
        jp_stockLayout.setHorizontalGroup(
            jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_stockLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_stockLayout.createSequentialGroup()
                        .addComponent(cb_state2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_sort3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_search2, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_search2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_reEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jp_stockLayout.setVerticalGroup(
            jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_stockLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_state2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_sort3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_search2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_search2)
                    .addComponent(bt_reEdit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("환불관리", jp_stock);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cb_state1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_state1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_state1ActionPerformed

    private void bt_sellCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_sellCompleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_sellCompleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SaleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SaleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SaleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SaleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SaleGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_reEdit;
    private javax.swing.JButton bt_refund;
    private javax.swing.JButton bt_search;
    private javax.swing.JButton bt_search2;
    private javax.swing.JButton bt_sellAdd;
    private javax.swing.JButton bt_sellComplete;
    private javax.swing.JButton bt_showAll1;
    private javax.swing.JComboBox<String> cb_sort;
    private javax.swing.JComboBox<String> cb_sort3;
    private javax.swing.JComboBox<String> cb_state1;
    private javax.swing.JComboBox<String> cb_state2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jp_inOrder;
    private javax.swing.JPanel jp_stock;
    private javax.swing.JTable tb_refund;
    private javax.swing.JTable tb_sell;
    private javax.swing.JTextField tf_search;
    private javax.swing.JTextField tf_search2;
    // End of variables declaration//GEN-END:variables
    //판매관리 전체조회
    private void showAllSell() throws SQLException { 
        List<SaleDTO> list = saleDao.SellshowAll();

        Object[][] data = new Object[list.size()][colName.length];
        for (int i = 0; i < list.size(); i++) {
            SaleDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getSellNo();
            data[i][2] = dto.getId();
            data[i][3] = dto.getBookNo() + "";
            data[i][4] = dto.getSubject();
            data[i][5] = dto.getPrice() + "";
            data[i][6] = dto.getSellState();
            data[i][7] = dto.getSellDate() + "";
        }
        model.setDataVector(data, colName);
        tb_sell.setModel(model);
        CallCheckBox();
        sellTableSize();
    }
    //판매완료상품 환불처리 하기
    private void refund() throws SQLException {
        int count = 0;
        //테이블 전체 행을 돌면서 체크된 거 확인
        for (int i = 0; i < model.getRowCount(); i++) { //체크박스 전부 돌면서 체크 돼있는지 확인
            boolean bool = (boolean) model.getValueAt(i, 0); 
            //체크되어 있으면
            if (bool == true) {
                String sellState = (String) tb_sell.getValueAt(i, 6); // 6번째 컬럼 상태받아서
                if (sellState.equals("판매완료")) { // 판매완료일 경우만

                    String sellNo = (String) tb_sell.getValueAt(i, 1); //해당 판매번호 받아서 
                    int cnt = saleDao.refundComplete(sellNo); // 환불처리

                    RefundDTO dto = refundDao.getReNo(); //환불번호 받기
                    dto.setSellno(sellNo); //판매번호 넣기
                    System.out.println("판매,환불번호 확인" + refundDao.getReNo() + sellNo);
                    refundDao.insertRefund(dto); //환불번호 판매번호 받은 DTO처리
                    count += cnt; //건수+ 
                }

            }
        }//for    
        if (count != 0) { //업데이트횟수 받아서 처리
            JOptionPane.showMessageDialog(this, count + "건의 환불처리가 진행되었습니다.");
            showAllSell(); //테이블 갱신
        } else { //조건이 맞지않을때 팝업
            JOptionPane.showMessageDialog(this, "판매완료인 상품을 선택해주세요.");
            
        }

    }

    private void refundSearch() throws SQLException { // 환불검색
        String reno = tf_search2.getText(); //텍스쳐필드 환불번호받아서
        List<RefundDTO> list = new ArrayList<>();
        list = refundDao.refundSearch(reno); // 해당 환불번호 있는지 조회후 list화
        
        if(list==null || list.isEmpty()){ //없으면 팝업창
            JOptionPane.showMessageDialog(this, "해당 판매번호에 대한 결과가 없습니다.");
            showAllRefund();
            tf_search2.requestFocus();
        }

        Object[][] data = new Object[list.size()][colName2.length];

        for (int i = 0; i < list.size(); i++) {
            RefundDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getReno();
            data[i][2] = dto.getSellno();
            data[i][3] = dto.getPrice() + "";
            data[i][4] = dto.getRestate();
            data[i][5] = dto.getRedate() + "";
        }
        
        model.setDataVector(data, colName2);
        tb_refund.setModel(model);
        CallCheckBox2();
        refundTableSize();
    }

    private void SellAdd() { //추가GUI 불러오기
        SaleAdd f = new SaleAdd();
        f.setVisible(true);
    }

    private void showAllRefund() throws SQLException { // 환불전체조회
        List<RefundDTO> list = new ArrayList<>();
        list = refundDao.refundShowAll();
        Object[][] data = new Object[list.size()][colName2.length];

        for (int i = 0; i < list.size(); i++) {
            RefundDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getReno();
            data[i][2] = dto.getSellno();
            data[i][3] = dto.getPrice() + "";
            data[i][4] = dto.getRestate();
            data[i][5] = dto.getRedate() + "";
        }

        model.setDataVector(data, colName2);
        tb_refund.setModel(model);
        CallCheckBox2();
        refundTableSize();
    }

    private void sellComplete() throws SQLException {
        int count = 0;
        //테이블 전체 행을 돌면서 체크된 거 확인
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            //체크되어 있으면
            if (bool == true) {
                String sellState = (String) tb_sell.getValueAt(i, 6); // 7번째 컬럼 상태받아서
                if (sellState.equals("판매처리중")) { // 판매처리중일 경우만

                    String sellNo = (String) tb_sell.getValueAt(i, 1); //해당 판매번호 받아서 
                    int cnt = saleDao.updateState(sellNo); // 판매완료처리
                    count += cnt;
                }

            }
        }//for    
        if (count != 0) {
            JOptionPane.showMessageDialog(this, count + "건의 판매가 완료되었습니다.");
            showAllSell();
        } else {
            JOptionPane.showMessageDialog(this, "판매처리중인 상품을 선택해주세요.");

        }

    }
    
     //판매관리 아이디 검색   
    private void SellSearch() throws SQLException {
        String id = tf_search.getText();
        List<SaleDTO> list = new ArrayList<>();
        list = saleDao.searchId(id);

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "해당 아이디에 대한 결과가 없습니다.");
            tf_search.requestFocus();
        }

        Object[][] data = new Object[list.size()][colName.length];

        for (int i = 0; i < list.size(); i++) {
            SaleDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getSellNo();
            data[i][2] = dto.getId();
            data[i][3] = dto.getBookNo() + "";
            data[i][4] = dto.getSubject();
            data[i][5] = dto.getPrice() + "";
            data[i][6] = dto.getSellState();
            data[i][7] = dto.getSellDate() + "";

        }

        model.setDataVector(data, colName);
        tb_sell.setModel(model);
        CallCheckBox();
        sellTableSize();

    }
        
    //선택 시 정렬 (판매일순)
    private void sortDate() throws SQLException {
        List<SaleDTO> list = new ArrayList<>();
        list = saleDao.showSortSellDate();
        Object[][] data = new Object[list.size()][colName.length];
        for (int i = 0; i < list.size(); i++) {
            SaleDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getSellNo();
            data[i][2] = dto.getId();
            data[i][3] = dto.getBookNo() + "";
            data[i][4] = dto.getSubject();
            data[i][5] = dto.getPrice() + "";
            data[i][6] = dto.getSellState();
            data[i][7] = dto.getSellDate() + "";

        }

        if (list == null || list.isEmpty()) {
            System.out.println("조회할 자료가 없습니다.");
        }
        model.setDataVector(data, colName);
        tb_sell.setModel(model);
        CallCheckBox();
        sellTableSize();
    }

    //선택 시 정렬 (판매금액순)
    private void sortPrice() throws SQLException {
        ArrayList<SaleDTO> list = new ArrayList<>();
        list = saleDao.showSortPrice();
        Object[][] data = new Object[list.size()][colName.length];

        for (int i = 0; i < list.size(); i++) {
            SaleDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getSellNo();
            data[i][2] = dto.getId();
            data[i][3] = dto.getBookNo() + "";
            data[i][4] = dto.getSubject();
            data[i][5] = dto.getPrice() + "";
            data[i][6] = dto.getSellState();
            data[i][7] = dto.getSellDate() + "";

        }
        if (list == null || list.isEmpty()) {
            System.out.println("조회할 자료가 없습니다.");
        }
        model.setDataVector(data, colName);
        tb_sell.setModel(model);
        CallCheckBox();
        sellTableSize();
    }

        //선택 시 정렬 (책이름)
        private void sortSubject() throws SQLException {
            ArrayList<SaleDTO> list = new ArrayList<>();
            list = saleDao.showSortSubject();

            Object[][] data = new Object[list.size()][colName.length];

            for (int i = 0; i < list.size(); i++) {
                SaleDTO dto = list.get(i);
                data[i][0] = false;
                data[i][1] = dto.getSellNo();
                data[i][2] = dto.getId();
                data[i][3] = dto.getBookNo() + "";
                data[i][4] = dto.getSubject();
                data[i][5] = dto.getPrice() + "";
                data[i][6] = dto.getSellState();
                data[i][7] = dto.getSellDate() + "";

            }
            if (list == null || list.isEmpty()) {
                System.out.println("조회할 자료가 없습니다.");
            }
            model.setDataVector(data, colName);
            tb_sell.setModel(model);
            CallCheckBox();
            sellTableSize();
        }        
    
//----------------------체크박스 관련 설정 ---------------------------------------------------------    
    //체크박스 설정   
    class MyDefaultTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            /*JTable table : 현재 작업 중인 JTable
                Object value : 현재 작업 중인 JTable의 셀객체
                int row : 현재 작업 중인 row번호
                int column : 현재 작업 중인 column번호*/
            
            if (column == 0) {
                //JCheckBox checkBox = checkBox = new JCheckBox();
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(((Boolean) value).booleanValue());
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);//가운데 정렬
                return checkBox;
            } else {
                return null;
            }
        }
    }//class
    
    //처리상태 정렬(상태순)
    private void sortState() throws SQLException {
        List<SaleDTO> list = saleDao.showSortState();

        Object[][] data = new Object[list.size()][colName.length];
        for (int i = 0; i < list.size(); i++) {
            SaleDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getSellNo();
            data[i][2] = dto.getId();
            data[i][3] = dto.getBookNo() + "";
            data[i][4] = dto.getSubject();
            data[i][5] = dto.getPrice() + "";
            data[i][6] = dto.getSellState();
            data[i][7] = dto.getSellDate() + "";
        }

        if (list == null || list.isEmpty()) {
            System.out.println("조회할 자료가 없습니다.");
        }
        model.setDataVector(data, colName);
        tb_sell.setModel(model);
        CallCheckBox();
        sellTableSize();

    }

    //처리상태정렬 (상태별)
    private void sortState2(String state) throws SQLException {
        List<SaleDTO> list = saleDao.showSortState2(state);

        System.out.println("판매상태별 정렬 ");
        Object[][] data = new Object[list.size()][colName.length];
        for (int i = 0; i < list.size(); i++) {
            SaleDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getSellNo();
            data[i][2] = dto.getId();
            data[i][3] = dto.getBookNo() + "";
            data[i][4] = dto.getSubject();
            data[i][5] = dto.getPrice() + "";
            data[i][6] = dto.getSellState();
            data[i][7] = dto.getSellDate() + "";
        }

        model.setDataVector(data, colName);
        tb_sell.setModel(model);
        CallCheckBox();
        sellTableSize();
    }
    //환불상태별 정렬
    private void sortState3() throws SQLException { 
        List<RefundDTO> list = new ArrayList<>();
        list = refundDao.refundSortState();

        Object[][] data = new Object[list.size()][colName2.length];

        for (int i = 0; i < list.size(); i++) {
            RefundDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getReno();
            data[i][2] = dto.getSellno();
            data[i][3] = dto.getPrice() + "";
            data[i][4] = dto.getRestate();
            data[i][5] = dto.getRedate() + "";
        }

        model.setDataVector(data, colName2);
        tb_refund.setModel(model);
        CallCheckBox2();
        refundTableSize();
    }
    //환불 상태별 정렬(환불처리중, 환불완료)
    private void sortState4(String state) throws SQLException {
        List<RefundDTO> list = new ArrayList<>();
        list = refundDao.refundSortState(state); //해당 인덱스값 아이템 받아서 DAO에서 처리

        Object[][] data = new Object[list.size()][colName2.length];

        for (int i = 0; i < list.size(); i++) {
            RefundDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getReno();
            data[i][2] = dto.getSellno();
            data[i][3] = dto.getPrice() + "";
            data[i][4] = dto.getRestate();
            data[i][5] = dto.getRedate() + "";
        }

        model.setDataVector(data, colName2);
        tb_refund.setModel(model);
        CallCheckBox2();
        refundTableSize();
    }
    //환불테이블 상태로 정렬
    private void refundSortDate() throws SQLException {

        List<RefundDTO> list = new ArrayList<>();
        list = refundDao.refundSortDate();

        Object[][] data = new Object[list.size()][colName2.length];

        for (int i = 0; i < list.size(); i++) {
            RefundDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getReno();
            data[i][2] = dto.getSellno();
            data[i][3] = dto.getPrice() + "";
            data[i][4] = dto.getRestate();
            data[i][5] = dto.getRedate() + "";
        }

        model.setDataVector(data, colName2);
        tb_refund.setModel(model);
        CallCheckBox2();
        refundTableSize();
        
    }
    //환불관리 가격별 정렬
    private void refundSortPrice() throws SQLException {
        List<RefundDTO> list = new ArrayList<>();
        list = refundDao.refundSortPrice();

        Object[][] data = new Object[list.size()][colName2.length];

        for (int i = 0; i < list.size(); i++) {
            RefundDTO dto = list.get(i);
            data[i][0] = false;
            data[i][1] = dto.getReno();
            data[i][2] = dto.getSellno();
            data[i][3] = dto.getPrice() + "";
            data[i][4] = dto.getRestate();
            data[i][5] = dto.getRedate() + "";
        }

        model.setDataVector(data, colName2);
        tb_refund.setModel(model);
        CallCheckBox2();
        refundTableSize();

    }
    //환불완료 처리
    private void refundEdit() throws SQLException {
        int count = 0;
        //테이블 전체 행을 돌면서 체크된 거 확인
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean bool = (boolean) model.getValueAt(i, 0);
            //체크되어 있으면
            if (bool == true) {
                String refundState = (String) tb_refund.getValueAt(i, 4); // 4번째 컬럼 상태받아서
                if (refundState.equals("환불처리중")) { // 환불처리중일 경우만

                    String reNo = (String) tb_refund.getValueAt(i, 1); //해당 환불번호 받아서 
                    int cnt = refundDao.refundEdit(reNo); // 환불처리
                    String sellNo = (String) tb_refund.getValueAt(i, 2);
                    int cnt2 = refundDao.refundComplete(sellNo);
                    System.out.println("환불번호=" + reNo+", 판매번호" + sellNo);
                    count += cnt; //건수+
                    if(cnt==1){
                        refundDao.plusStock(sellNo); //재고테이블 + 처리
                    }
                }

            }
        }//for    
        if (count != 0) { //완료되면 건합쳐서 팝업처리
            JOptionPane.showMessageDialog(this, count + "건의 환불처리가 완료되었습니다.");
            showAllRefund();
        } else { //실패시 팝업처리
            JOptionPane.showMessageDialog(this, "환불처리중인 상품을 선택해주세요.");
        }
    }
    
    // -명령어 메서드 정리-
    public void CallCheckBox() {
        DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
        tb_sell.getColumn("선택").setCellRenderer(renderer);
        tb_sell.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
    }
    
    public void CallCheckBox2() {
        DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
        tb_refund.getColumn("선택").setCellRenderer(renderer);
        tb_refund.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
    }
    //판매관리 테이블 사이즈 조절
    public void sellTableSize() {

        TableColumn col0 = tb_sell.getColumnModel().getColumn(0); //선택
        TableColumn col1 = tb_sell.getColumnModel().getColumn(1); //판매번호
        TableColumn col2 = tb_sell.getColumnModel().getColumn(2); //아이디
        TableColumn col3 = tb_sell.getColumnModel().getColumn(3); //책번호
        TableColumn col4 = tb_sell.getColumnModel().getColumn(4); //책이름
        TableColumn col5 = tb_sell.getColumnModel().getColumn(5); //판매금액
        TableColumn col6 = tb_sell.getColumnModel().getColumn(6); //처리 상태
        TableColumn col7 = tb_sell.getColumnModel().getColumn(7); // 판매일자
        
        
        col0.setPreferredWidth(32);
        col1.setPreferredWidth(128);
        col2.setPreferredWidth(70);
        col3.setPreferredWidth(43);
        col4.setPreferredWidth(125);
        col5.setPreferredWidth(80);
        col6.setPreferredWidth(70);
        col7.setPreferredWidth(130);

    }
    //환불관리 테이블 사이즈 조절
    public void refundTableSize() {
        
        TableColumn col0 = tb_refund.getColumnModel().getColumn(0); //선택
        TableColumn col1 = tb_refund.getColumnModel().getColumn(1); //환불번호
        TableColumn col2 = tb_refund.getColumnModel().getColumn(2); //판매번호
        TableColumn col3 = tb_refund.getColumnModel().getColumn(3); //판매금액
        TableColumn col4 = tb_refund.getColumnModel().getColumn(4); //처리상태
        TableColumn col5 = tb_refund.getColumnModel().getColumn(5); //환불일자
        
        
        col0.setPreferredWidth(32);
        col1.setPreferredWidth(115);
        col2.setPreferredWidth(118);
        col3.setPreferredWidth(80);
        col4.setPreferredWidth(65);
        col5.setPreferredWidth(130);

    }
    
       
}