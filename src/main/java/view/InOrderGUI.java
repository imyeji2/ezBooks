/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import model.EmpDAO;
import model.InOrderDAO;
import model.StockDAO;
import model.StockTableViewDTO;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
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
import model.InOrderTableDTO;

/**
 *
 * @author YJ
 */
public class InOrderGUI extends javax.swing.JFrame implements ActionListener, ItemListener{
    private InOrderDAO Dao;
    private StockDAO StockDao;
    private String[] colNames ={"선택","입고번호","계약번호","수량","수량단가","총금액","담당자","입고일자"};
    private String[] colNames2 ={"재고번호","제목","출판사","수량","입고일자","재고상태"};
    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel model2 = new DefaultTableModel();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



    
    /**
     * Creates new form InOrder
     */
    public InOrderGUI() {
        initComponents();
        init();
        addEvent();
        setLocationRelativeTo(null); //가운데 배치
        
    }
    
    
    private void init() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dao = new InOrderDAO();
        StockDao = new StockDAO();
       
        
        try {
            showAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        

    }
    
    //이벤트 처리
    private void addEvent() {
        tf_serch.addActionListener(this);
        bt_serch.addActionListener(this);
        cb_select1.addItemListener(this);
        cb_sort.addItemListener(this);
        bt_add.addActionListener(this);
        bt_edit.addActionListener(this);
        bt_dell.addActionListener(this);
        jTabbedPane1.addMouseListener(new EventHandler());
        bt_serch2.addActionListener(this);
        tf_serch2.addActionListener(this);
        bt_showAll1.addActionListener(this);
        bt_showAll2.addActionListener(this);
    }


    
  //======================================이벤트 핸들러=====================================================
 
    //탭 전환시 새로고침
    class EventHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource()==jTabbedPane1){
                 JTabbedPane tp=(JTabbedPane) e.getSource();
                int index=tp.getSelectedIndex();
                
                //입고 페이지 초기화 
                if(index==0){
                     try {
                         showAll();
                         tf_serch2.setText("");
                         cb_serch.setSelectedIndex(0);
                         cb_sort.setSelectedIndex(0);

                     } catch (SQLException ex) {
                         ex.printStackTrace();
                     }
                 //재고 페이지 초기화
                }else if(index==1){
                     try {
                         showAllStock();
                         tf_serch.setText("");
                         cb_select1.setSelectedIndex(0);
                         cb_select2.setSelectedIndex(0);
                         
                     } catch (SQLException ex) {
                         ex.printStackTrace();
                     }
                }//1번
            }
        }//mouseClicked

    }//EventHandler
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //입고 검색
        if(e.getSource()==tf_serch || e.getSource()==bt_serch){
            //1.사용자로 부터 입력받기
            String serchStr= tf_serch.getText();
            
            if(serchStr==null||serchStr.isEmpty()){
                JOptionPane.showMessageDialog(this, "검색어를 입력해주세요");
                tf_serch.requestFocus();
                return;
            }
            
            //1.입고번호
            if(cb_select2.getSelectedIndex()==0){
                try {
                    serchInDate(serchStr);
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            //2.담당자명    
            }else if(cb_select2.getSelectedIndex()==1){
                try {
                    serchEmpName(serchStr);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                    
            }//else

        //입고 수정
        }else if(e.getSource()==bt_add){
           
            List<String> arr = getCheckId();
            
            if(arr.size()>0){
                JOptionPane.showMessageDialog(this, "체크 박스가 선택되었습니다.");
                return;
            }
            
            InOrderAddGUI f = new InOrderAddGUI();
            f.setVisible(true);           
            try {
                showAll();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            
            
        //입고 수정
        }else if(e.getSource()==bt_edit){
            List<String> arr = getCheckId();
            
            //수정할 항목을 선택하지 않았을 때
            if(arr.size() <1){
                JOptionPane.showMessageDialog(this, "수정할 항목을 선택해주세요");
                return;
            
            //수정할 항목을 여러개 선택했을때
            }else if(arr.size()>1){
                JOptionPane.showMessageDialog(this, "수정할 항목을 하나만 선택해주세요");
                return;               
            }else{
                //수정할 항목을 1개만 선택했을때
                //해당 pk 값 팝업에 전달
                InOrderEditGUI f = new InOrderEditGUI(arr.get(0));
                f.setVisible(true);
            }
        }else if(e.getSource()==bt_dell){
            List<String> arr = getCheckId();
            
            if(arr.size()>0){
                int answer = JOptionPane.showConfirmDialog(this, arr.size()+"건을 삭제하시겠습니까?", "삭제 확인",JOptionPane.YES_NO_OPTION );
                    if(answer==JOptionPane.YES_OPTION){
                    try {
                        delete(arr);
                        //System.out.println("프로그램을 종료합니다.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    } else{  //사용자가 Yes 이외의 값을 눌렀을 경우
                            System.out.println("삭제가 취소되었습니다.");
                    }
            }else{
                JOptionPane.showMessageDialog(this, "삭제할 항목을 선택하세요");
                return;
            } //재고 검색
        }else if(e.getSource()==tf_serch2 || e.getSource()==bt_serch2){
            //1.사용자로 부터 입력받기
            String serchStr= tf_serch2.getText();
            
            if(serchStr==null||serchStr.isEmpty()){
                JOptionPane.showMessageDialog(this, "검색어를 입력해주세요");
                tf_serch2.requestFocus();
                return;
            }
            
            //1.책 제목
            if(cb_serch.getSelectedIndex()==0){
                try {
                    serchSubject(serchStr);
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            //2.출판사명   
            }else if(cb_serch.getSelectedIndex()==1){
                try {
                    serchPubliserName(serchStr);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                    
            }//else
        //새로고침 버튼
        }else if(e.getSource()==bt_showAll1){//입고
            try {
                showAll();
                tf_serch.setText("");
                cb_select1.setSelectedIndex(0);
                cb_select2.setSelectedIndex(0);              
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else if(e.getSource()==bt_showAll2){//재고
            try {
                showAllStock();
                tf_serch2.setText("");
                cb_serch.setSelectedIndex(0);
                cb_sort.setSelectedIndex(0);                  
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }//actionPerformed
    
    
    //정렬 이벤트
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==cb_select1){
            
            //초기화
            if(cb_select1.getSelectedIndex()==0){
                try {
                    showAll();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            //계약일순 정렬   
            }else if(cb_select1.getSelectedIndex()==1){
                try {
                    sortInDate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
               
            }else if(cb_select1.getSelectedIndex()==2){
                try {
                    sortTotalPrice();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                          
            }//else
            
        //재고관리 정렬
        }else if(e.getSource()==cb_sort){
            //재고번호순
            if(cb_sort.getSelectedIndex()==1){
                try {
                    sortStockNo();
                    //재고수량순
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }else if(cb_sort.getSelectedIndex()==2){
                try {
                    sortQty();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }//itemStateChanged
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
        jTable1 = new javax.swing.JTable();
        cb_select1 = new javax.swing.JComboBox<>();
        cb_select2 = new javax.swing.JComboBox<>();
        tf_serch = new javax.swing.JTextField();
        bt_serch = new javax.swing.JButton();
        bt_add = new javax.swing.JButton();
        bt_edit = new javax.swing.JButton();
        bt_dell = new javax.swing.JButton();
        bt_showAll1 = new javax.swing.JButton();
        jp_stock = new javax.swing.JPanel();
        cb_sort = new javax.swing.JComboBox<>();
        cb_serch = new javax.swing.JComboBox<>();
        tf_serch2 = new javax.swing.JTextField();
        bt_serch2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        bt_showAll2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("물류관리");
        setPreferredSize(new java.awt.Dimension(800, 600));

        jTabbedPane1.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        jp_inOrder.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        jTable1.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "선택", "입고번호", "계약번호", "수량", "수량단가", "총금액", "담당자", "입고일자"
            }
        ));
        jTable1.setCellSelectionEnabled(true);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setDragEnabled(true);
        jTable1.setRowHeight(25);
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        cb_select1.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_select1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "정렬", "입고일자", "총금액순" }));
        cb_select1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_select1ActionPerformed(evt);
            }
        });

        cb_select2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_select2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "입고번호", "담당자명" }));

        tf_serch.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        bt_serch.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_serch.setText("검색");

        bt_add.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_add.setText("추가");

        bt_edit.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_edit.setText("수정");

        bt_dell.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_dell.setText("삭제");

        bt_showAll1.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_showAll1.setText("전체조회");

        javax.swing.GroupLayout jp_inOrderLayout = new javax.swing.GroupLayout(jp_inOrder);
        jp_inOrder.setLayout(jp_inOrderLayout);
        jp_inOrderLayout.setHorizontalGroup(
            jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_inOrderLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_inOrderLayout.createSequentialGroup()
                        .addComponent(cb_select1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_select2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_serch, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_serch, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_add, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bt_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(bt_dell, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bt_showAll1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jp_inOrderLayout.setVerticalGroup(
            jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_inOrderLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bt_showAll1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_inOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_select1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_select2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_serch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_serch)
                    .addComponent(bt_add)
                    .addComponent(bt_edit)
                    .addComponent(bt_dell))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jTabbedPane1.addTab("입고관리", jp_inOrder);

        cb_sort.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_sort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "정렬", "재고번호", "재고수량" }));

        cb_serch.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cb_serch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "책 제목", "출판사명" }));

        tf_serch2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        bt_serch2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_serch2.setText("검색");

        jTable2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.setCellSelectionEnabled(true);
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable2.setDragEnabled(true);
        jTable2.setRowHeight(25);
        jTable2.setShowGrid(true);
        jScrollPane3.setViewportView(jTable2);

        bt_showAll2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_showAll2.setText("전체조회");

        javax.swing.GroupLayout jp_stockLayout = new javax.swing.GroupLayout(jp_stock);
        jp_stock.setLayout(jp_stockLayout);
        jp_stockLayout.setHorizontalGroup(
            jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_stockLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_stockLayout.createSequentialGroup()
                        .addComponent(cb_sort, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_serch, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_serch2, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_serch2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_showAll2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jp_stockLayout.setVerticalGroup(
            jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_stockLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jp_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_sort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_serch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_serch2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_serch2)
                    .addComponent(bt_showAll2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("재고관리", jp_stock);

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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cb_select1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_select1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_select1ActionPerformed

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
            java.util.logging.Logger.getLogger(InOrderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InOrderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InOrderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InOrderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InOrderGUI().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_add;
    private javax.swing.JButton bt_dell;
    private javax.swing.JButton bt_edit;
    private javax.swing.JButton bt_serch;
    private javax.swing.JButton bt_serch2;
    private javax.swing.JButton bt_showAll1;
    private javax.swing.JButton bt_showAll2;
    private javax.swing.JComboBox<String> cb_select1;
    private javax.swing.JComboBox<String> cb_select2;
    private javax.swing.JComboBox<String> cb_serch;
    private javax.swing.JComboBox<String> cb_sort;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel jp_inOrder;
    private javax.swing.JPanel jp_stock;
    private javax.swing.JTextField tf_serch;
    private javax.swing.JTextField tf_serch2;
    // End of variables declaration//GEN-END:variables


//-------------------------입고 테이블 관련 처리-------------------------------------------
    
    //입고 전체 조회
    void showAll() throws SQLException {
        //1. 사용자로부터 입력받기
        
        //2. sql 로직 처리
        List<InOrderTableDTO> list =Dao.selectInOrder();
     
        //3. 결과 출력
        if(list==null || list.isEmpty()){
            JOptionPane.showMessageDialog(this, "데이터가 없습니다.");
            return;
        }
        
        addTable(list);

        
    }//showAll

    

    //입고번호로 조회
    private void serchInDate(String serchStr) throws SQLException {

        //2.sql처리
        List<InOrderTableDTO> list = Dao.serchInNo(serchStr);
        EmpDAO empDao = new EmpDAO(); 

        //유효성 검사
        if(list==null || list.isEmpty()){
            JOptionPane.showMessageDialog(this, "검색된 데이터가 없습니다.");
            tf_serch.setText("");
            showAll();
            return;
        }        

        //3. 출력
         addTable(list);

    }//serchInDate
    
    
    
    //담당자명으로 조회
    private void serchEmpName(String serchStr) throws SQLException {

        //2.sql 처리
        List<InOrderTableDTO> list = Dao.serchEmpName(serchStr);
        EmpDAO empDao = new EmpDAO();

        //유효성 검사
        if(list==null||list.isEmpty()){
            JOptionPane.showMessageDialog(this, "검색된 데이터가 없습니다.");
            tf_serch.setText("");
            showAll();
            return;
        }


        //3.결과 출력
         addTable(list);
                         
    }//serchEmpName
    
    //입고일자별 정렬
    private void sortInDate() throws SQLException {

        //1
        //2
        List<InOrderTableDTO> list = Dao.sortIndate();
        EmpDAO empDao = new EmpDAO();

        if(list==null||list.isEmpty()){
            System.out.println("정렬 실패");
        }

        //3
        addTable(list);

    }//sortInDate
    
    
    //금액으로 정렬
    private void sortTotalPrice() throws SQLException {
        //1
        //2
        List<InOrderTableDTO> list = Dao.sortTotal();
        EmpDAO empDao = new EmpDAO();

        if(list==null||list.isEmpty()){
            System.out.println("정렬 실패");
        }

        //3
         addTable(list);
    }//sortTotalPrice
    
    
    
//-------------------------재고 테이블 관련 처리-------------------------------------------

    //재고 전체 조회
    private void showAllStock() throws SQLException {
        //1
        //2
        List<StockTableViewDTO> list = StockDao.showAll();

        //3
        stockTable(list);
    }//showAllStock

    //재고 번호 순서대로 정렬
    private void sortStockNo() throws SQLException {
        //1
        //2
        List<StockTableViewDTO> list = StockDao.sortStockNo();
        //3
        stockTable(list);
    }//sortStockNo

    //재고 수량으로 적은 순 정렬
    private void sortQty() throws SQLException {
        //1
        //2
        List<StockTableViewDTO> list = StockDao.sortStockQty();
        //3
        stockTable(list);        
    }//sortQty
    
    //책 재목으로 검색
    private void serchSubject(String serchStr) throws SQLException {
        //1
        String subject = tf_serch2.getText();
        
        //유효성 검사
        if(subject==null || subject.isEmpty()){
            JOptionPane.showMessageDialog(this, "검색어를 입력해주세요");
            tf_serch2.requestFocus();
            return;
        }
        
        //2
        List<StockTableViewDTO> list = StockDao.serchSubject(subject);
        
        //유효성 검사
        if(list==null||list.isEmpty()){
            JOptionPane.showMessageDialog(this, "검색된 데이터가 없습니다.");
            showAllStock();
            tf_serch2.setText("");
            return;
        }
        //3
        stockTable(list);
    }//serchSubject
 
    
    
    //출판사 명으로 검색
    private void serchPubliserName(String serchStr) throws SQLException {
        //1
        String publisherName = tf_serch2.getText();
        
        //유효성 검사
        if(publisherName==null || publisherName.isEmpty()){
            JOptionPane.showMessageDialog(this, "검색어를 입력해주세요");
            tf_serch2.requestFocus();
            return;
        }
        
        //2
        List<StockTableViewDTO> list = StockDao.serchPuName(publisherName);
        
        //유효성 검사
        if(list==null||list.isEmpty()){
            JOptionPane.showMessageDialog(this, "검색된 데이터가 없습니다.");
            showAllStock();
            tf_serch2.setText("");
            return;
        }       
        
        //3
        stockTable(list);        
    }//serchPubliserName    
    
    

    
//------------------체크 박스 관련 처리 ------------------------------------------------    
    
    
    //체크박스에 pk값 저장해서 받아오기
    private List<String> getCheckId() {
        List<String> CheckArr = new ArrayList<>();
        List<Integer>checkRowNum =new ArrayList<>();
        
        //테이블 전체 행을 돌면서 체크된 거 확인
        for(int i=0;i<model.getRowCount();i++) {
            boolean bool=(boolean) model.getValueAt(i, 0);
            //체크되어 있으면 해당 행번호 저장
            if(bool==true) checkRowNum.add(i);
        }//for         
        
        //체크된 만큼 반복 하면서
        for(int i=0;i<checkRowNum.size();i++){
            //해당 행의 pk값을 CheckArr에 저장
            CheckArr.add((String) model.getValueAt(checkRowNum.get(i), 1));
            System.out.println(checkRowNum.get(i));
            //System.out.println(i+"inNo="+ model.getValueAt(i, 1));      
        }
        return CheckArr;       
        
    }//getEditData
    
    //선택 삭제
    private void delete(List<String> arr) throws SQLException {
        //1
        //2
        
        int result = Dao.deleteInOrder(arr);
        
        //3
        JOptionPane.showMessageDialog(this, result+"건이 삭제 완료 되었습니다.");
        showAll();
    }

    
   /*---------------------------------테이블 디자인 관련(출력포함)--------------------------------------*/
    

    //테이블 출력
    public void addTable(List arr) throws SQLException{
        List<InOrderTableDTO> list=arr;
        Object[][] data = new Object[list.size()][colNames.length];
        for(int i=0; i<list.size();i++){
            
           
            InOrderTableDTO dto =list.get(i);
            
            data[i][0] = false;
            data[i][1] = dto.getInNo();
            data[i][2] = dto.getConNo();
            data[i][3] = dto.getQty()+"";
            data[i][4] = dto.getCost()+"";
            data[i][5]  = dto.getQty()*dto.getCost()+"";
            data[i][6] = dto.getEmpName();
            data[i][7] = sdf.format(dto.getInDate());
        }
        model.setDataVector(data,colNames);
        jTable1.setModel(model);
        columnSize();
        
        //체크박스
        DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
        jTable1.getColumn("선택").setCellRenderer(renderer);
        jTable1.getColumn("선택").setCellEditor(new DefaultCellEditor(new JCheckBox()));
       

    }
    
    //재고 테이블 출력
    public void stockTable(List arr) throws SQLException{
        List<StockTableViewDTO> list=arr;
        Object[][] data = new Object[list.size()][colNames2.length];
        for(int i=0; i<list.size();i++){
            StockTableViewDTO dto = list.get(i);
            if(dto.getInDate()!=null){
                data[i][0] = dto.getStockNo();
                data[i][1] = dto.getSubject();
                data[i][2] = dto.getPuName();
                data[i][3] = dto.getQty()+"";
                data[i][4]  = sdf.format(dto.getInDate());
                      
            }else{
                data[i][0] = dto.getStockNo();
                data[i][1] = dto.getSubject();
                data[i][2] = dto.getPuName();
                data[i][3] = dto.getQty()+"";
                data[i][4]  = "입고미정";
            }
            
            if(dto.getQty()>=10){
                data[i][5]="";
            }else if(dto.getQty()==0){
                data[i][5]= "재고 없음";
            }else{
                data[i][5] = "주문 필요";    
            }
            
        model2.setDataVector(data,colNames2);
        jTable2.setModel(model2);
        columnSize2();
                    
        }
    }//stockTable    
    
    
    
    
    
    
    //입고 테이블 사이즈 조절
    public void columnSize(){
      
        TableColumn col1 = jTable1.getColumnModel().getColumn(0);
        TableColumn col2 = jTable1.getColumnModel().getColumn(1);
        TableColumn col3 = jTable1.getColumnModel().getColumn(2);
        TableColumn col4 = jTable1.getColumnModel().getColumn(3);
        TableColumn col5 = jTable1.getColumnModel().getColumn(4);
        TableColumn col6 = jTable1.getColumnModel().getColumn(5);
        TableColumn col7 = jTable1.getColumnModel().getColumn(6);
        TableColumn col8 = jTable1.getColumnModel().getColumn(7);
         
        col1.setPreferredWidth(20);
        col2.setPreferredWidth(110);
        col3.setPreferredWidth(110);
        col4.setPreferredWidth(30);
        col5.setPreferredWidth(50);
        col6.setPreferredWidth(50);
        col7.setPreferredWidth(30);
        col8.setPreferredWidth(100);
        tableCellCenter(jTable1);//테이블 내용 가운데 정렬
    }
    
    //재고 테이블 사이즈 조절
    public void columnSize2(){
      
        TableColumn col1 = jTable2.getColumnModel().getColumn(0);
        TableColumn col2 = jTable2.getColumnModel().getColumn(1);
        TableColumn col3 = jTable2.getColumnModel().getColumn(2);
        TableColumn col4 = jTable2.getColumnModel().getColumn(3);
        TableColumn col5 = jTable2.getColumnModel().getColumn(4);
        TableColumn col6 = jTable2.getColumnModel().getColumn(5);
         
        col1.setPreferredWidth(20);
        col2.setPreferredWidth(130);
        col3.setPreferredWidth(100);
        col4.setPreferredWidth(30);
        col5.setPreferredWidth(100);
        col6.setPreferredWidth(50);
        tableCellCenter(jTable2);//테이블 내용 가운데 정렬
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

