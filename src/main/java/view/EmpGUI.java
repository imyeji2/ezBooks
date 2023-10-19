/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import model.DeptDAO;
import model.DeptDTO;
import model.EmpDAO;
import model.EmpDTO;
import model.EmpTableDTO;
import model.DeptTableDTO;


/**
 *
 * @author YJ
 */
public class EmpGUI extends javax.swing.JFrame implements ActionListener, ItemListener{
    private DeptDAO deptDao;
    private EmpDAO empDao;
    
    private DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<>();
    private DefaultTableModel model = new DefaultTableModel();
    private String[] colNames1 = {"사원번호", "이름", "연락처", "직급", "부서"};
    private String[] colNames2 = {"부서번호", "부서명", "파트장", "인원수"};
    
    private boolean dupChecked;    //매니저 체크
    
    //등록, 수정, 삭제 버튼을 누른 후 숫자를 통해 완료 버튼으로 처리하기 위한 것
    //ex) 등록 = 1, 수정 = 2, 삭제 = 3
    private int EMPSTATE = 0;   
    private int DEPTSTATE = 0;
    
    //파트장 존재여부를 확인하기 위한 getter, setter
    public boolean isDupChecked() {
        return dupChecked;
    }

    public void setDupChecked(boolean dupChecked) {
        this.dupChecked = dupChecked;
    }


    
    /**
     * Creates new form InOrder
     */
    public EmpGUI() {
        initComponents();
        init();
        addEvent();
        setLocationRelativeTo(null); //가운데 배치
    }
    
    
    private void init() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        empDao = new EmpDAO();
        deptDao = new DeptDAO();
        //plInput.setVisible(false);
        try {
            showAllEmp();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       
        //메인화면 진입 시 입력창 막아두기.
        setEditingEmpMain();
        setEditingDeptMain();
        
        try {
            //콤보박스 값 자동으로 넣기
            getDeptNameList();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//
    
    //이벤트 처리
    private void addEvent() {
        tabP1.addMouseListener(new EventHandler());
        
        //사원
        btEmpAdd.addActionListener(this);
        btEmpModify.addActionListener(this);
        btEmpDelete.addActionListener(this);
        btSearch.addActionListener(this);
        btFinish.addActionListener(this);
        tfSearch.addActionListener(this);

        cbName.addActionListener(this);
        cbEmpDept.addActionListener(this);
        tbEmp.addMouseListener(new EventHandler());
        bt_dell2.addActionListener(this);
        
        //부서
        btDepRegister.addActionListener(this);
        btDepModify.addActionListener(this);
        btDepDelete.addActionListener(this);
        btDepNameSh.addActionListener(this);
        btDeptFinish.addActionListener(this);
        btDeptSearch.addActionListener(this);
        tf_serch.addActionListener(this);
        
        tbDept.addMouseListener(new EventHandler());
        cbDept.addActionListener(this);
        bt_dell4.addActionListener(this);
    }
    
    
  //======================================이벤트 핸들러=====================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btEmpAdd) {
            //사원등록 준비
            editingEmpAdd();    //막아뒀던 입력창 풀기
            editingEmp();       //만약 테이블을 클릭해 데이터가 있을 시 없애주는 용도
            
            try {
                //사원번호 자동으로 입력해주기
                addEmpNo();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            EMPSTATE = 1;   //사원등록번호 매기기

        } else if (e.getSource() == btEmpModify) {
            //사원정보 수정 준비
            editingEmpModi();   //막아뒀던 입력창 풀기
            EMPSTATE = 2;       //사원수정번호 매기기
            

        } else if (e.getSource() == btEmpDelete) {
            //사원정보 삭제 준비
            editingEmpDel();    //막아뒀던 입력창 풀기
            EMPSTATE = 3;       //사원삭제번호 매기기

        } else if (e.getSource() == btFinish && EMPSTATE == 1) {
            //사원 등록
            try {
                addEmp();   //사원 등록 처리
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setEditingEmpMain();    //다시 입력창 막아두기
            
        } else if (e.getSource() == btFinish && EMPSTATE == 2) {
            //사원 수정
            try {
                modifyEmp();    //사원 수정 처리
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setEditingEmpMain();    //다시 입력창 막아두기
            
        } else if (e.getSource() == btFinish && EMPSTATE == 3) {
            //사원 삭제
            try {
                deleteEmp();   //사원 삭제 처리
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setEditingEmpMain();    //다시 입력창 막아두기
            
        } else if (e.getSource() == btSearch || e.getSource() == tfSearch) {    //텍스트필드도 이벤트 처리해서 엔터키로도 검색할 수 있게
            //사원 콤보박스로 원하는 정보 선택해서 검색
            int index = cbName.getSelectedIndex();  //콤보박스의 선택된 값을 숫자로 받아오기
            System.out.println("선택된 index = " + index);
            if (index == 0) {   //선택된 값이 정렬일 때
                try {
                    showAllEmp();   //모든 사원데이터 보여주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else if (index == 1) {    //선택된 값이 이름일 때
                try {
                    showEmpName(); //이름으로 검색한 데이터 보여주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else if (index == 2) {    //선택된 값이 사원번호일 때
                try {
                    showEmpNo();   //사원번호로 검색한 데이터 보여주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }//

        } else if (e.getSource() == bt_dell2) {
            //사원 새로고침 버튼
            EmpRefresh();
            
        }else if (e.getSource() == btDepRegister) {
            //부서 등록 준비
            editingDeptAdd();   //막아뒀던 입력창 풀기
            DEPTSTATE = 1;      //부서 등록번호 매기기
            
        } else if (e.getSource() == btDepModify) {
            //부서 수정 준비
            editingDeptModi();  //막아줬던 입력창 풀기
            DEPTSTATE = 2;      //부서 수정번호 매기기

        } else if (e.getSource() == btDepDelete) {
            //부서 삭제 준비
            editingDeptDel();   //막아뒀던 입력창 풀기
            DEPTSTATE = 3;      //부서 삭제번호 매기기

        } else if (e.getSource() == btDeptFinish && DEPTSTATE == 1) {
            //부서 정보 등록
            try {
                addDept();  //부서 등록 처리
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setEditingDeptMain();   //다시 입력창 막아두기

        } else if (e.getSource() == btDeptFinish && DEPTSTATE == 2) {
            //부서 정보 수정
            try {
                modifyDept();   //부서 수정 처리
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setEditingDeptMain();   //다시 입력창 막아두기
            setDupChecked(false);   //매니저 이름 존재여부 확인을 다시 false로 바꿔놓기

        } else if (e.getSource() == btDeptFinish && DEPTSTATE == 3) {
            //부서 정보 삭제
            try {
                deleteDept();   //부서 삭제 처리
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setEditingDeptMain();   //다시 입력창 막아두기

        } else if (e.getSource() == btDeptSearch || e.getSource() == tf_serch) {    //텍스트필드도 이벤트 처리해서 엔터키로도 검색할 수 있게
            //부서 콤보박스로 원하는 정보 선택해서 검색
            int index = cbDept.getSelectedIndex();  //콤보박스에서 선택된 값 숫자로 받아오기
            System.out.println("선택된 index = " + index);

            if (index == 0) {   //선택된 값이 정렬일 때
                try {
                    showAllDept();  //모든 부서데이터 보여주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else if (index == 1) {    //선택된 값이 부서번호일 때
                try {
                    showDeptNo();   //부서번호로 조회한 데이터 보여주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else if (index == 2) {    //선택된 값이 부서명일 때
                try {
                    showDeptName(); //부서명으로 조회한 데이터 보여주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } else if (e.getSource() == bt_dell4) {
            //부서 새로고침
            DeptRefresh();
            
        } else if (e.getSource() == btDepNameSh) {
        
            try {
                duplicated();   //사원이름이 존재한지 여부확인
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }

    }//

    @Override
    public void itemStateChanged(ItemEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    class EventHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {    //마우스 클릭에 대한 이벤트 처리
            if (e.getSource() == tbEmp) {
                //사원 테이블을 클릭했을 때 선택된 데이터 입력창에 보여주기
                System.out.println("emp 테이블 클릭");
                
                int row = tbEmp.getSelectedRow(); //테이블에서 선택된 행 변수에 저장하기 (클릭된 값이 없으면 '-1')

                Object objNo = tbEmp.getValueAt(row, 0);    //원하는 위치의 데이터 가져오기 (선택된 행의 0번째 값(사원번호))
                String empNo = (String) objNo;  //데이터를 문자열에 담기

                try {
                    showByTable(empNo); //가져온 사원번호로 조회해서 값을 넣어주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else if (e.getSource() == tbDept) {
                //부서테이블을 클릭했을 때 선택된 데이터 입력창에 보여주기
                System.out.println("dept 테이블 클릭");
                
                int row = tbDept.getSelectedRow();  //테이블에서 선택된 행 변수에 저장하기 (클릭된 값이 없으면 '-1')
                
                Object objNo = tbDept.getValueAt(row, 0);   //원하는 위치의 데이터 가져오기 (선택된 행의 0번째 값(부서번호))
                int deptno = Integer.parseInt((String) objNo);  //데이터를 문자열에 담기
                
                try {
                    showTableDept(deptno);  //가져온 부서번호로 조회해서 값을 넣어주기
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else if (e.getSource() == tabP1) {
                //탭 전환시 초기화
                JTabbedPane tp=(JTabbedPane) e.getSource(); 
                int index = tp.getSelectedIndex();  //선택된 탭을 숫자로 받기
                
                //사원탭 초기화
                if (index == 0) {   //선택된 탭이 사원일 때
                    try {
                        showAllEmp();   //모든 사원데이터 보여주기  
                        clearTfEmp();   //모든 입력창, 콤보박스 초기화하기
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                } else if (index == 1) {    //선택된 탭이 부서일 때
                    try {
                        showAllDept();  //모든 부서데이터 보여주기 
                        clearTfDept();  //모든 입력창, 콤보박스 초기화하기
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } 
            }//
            
            
        }//
        


    }//
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabP1 = new javax.swing.JTabbedPane();
        plDept = new javax.swing.JPanel();
        plInput = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tfNo = new javax.swing.JTextField();
        tfName = new javax.swing.JTextField();
        tfPw = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfTel = new javax.swing.JTextField();
        btFinish = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cbPosition = new javax.swing.JComboBox<>();
        cbEmpDept = new javax.swing.JComboBox<>();
        tfSearch = new javax.swing.JTextField();
        btSearch = new javax.swing.JButton();
        btEmpAdd = new javax.swing.JButton();
        btEmpModify = new javax.swing.JButton();
        btEmpDelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbEmp = new javax.swing.JTable();
        cbName = new javax.swing.JComboBox<>();
        bt_dell2 = new javax.swing.JButton();
        plMem = new javax.swing.JPanel();
        plnput2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        tfDeptNo = new javax.swing.JTextField();
        tfDeptName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tfDeptManager = new javax.swing.JTextField();
        btDeptFinish = new javax.swing.JButton();
        btDepNameSh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDept = new javax.swing.JTable();
        cbDept = new javax.swing.JComboBox<>();
        tf_serch = new javax.swing.JTextField();
        btDepRegister = new javax.swing.JButton();
        btDepModify = new javax.swing.JButton();
        btDepDelete = new javax.swing.JButton();
        bt_dell4 = new javax.swing.JButton();
        btDeptSearch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("인사관리");

        plInput.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(153, 153, 153)));

        jLabel4.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("사원번호");

        tfNo.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        tfName.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        tfPw.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("이름");

        jLabel6.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("연락처");

        tfTel.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        btFinish.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btFinish.setText("확인");

        jLabel7.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("비밀번호");

        jLabel8.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("부서");

        jLabel9.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("직급");

        cbPosition.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cbPosition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "인턴", "사원", "대리", "부장", "사장" }));

        cbEmpDept.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cbEmpDept.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "인사팀", "물류팀", "고객지원팀", "영업팀" }));

        javax.swing.GroupLayout plInputLayout = new javax.swing.GroupLayout(plInput);
        plInput.setLayout(plInputLayout);
        plInputLayout.setHorizontalGroup(
            plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plInputLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(plInputLayout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfTel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(plInputLayout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(plInputLayout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(plInputLayout.createSequentialGroup()
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfPw, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(plInputLayout.createSequentialGroup()
                            .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cbPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbEmpDept, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        plInputLayout.setVerticalGroup(
            plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plInputLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(plInputLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                    .addComponent(tfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbEmpDept, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(plInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPw, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        btSearch.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btSearch.setText("검색");

        btEmpAdd.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btEmpAdd.setText("등록");

        btEmpModify.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btEmpModify.setText("수정");
        btEmpModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEmpModifyActionPerformed(evt);
            }
        });

        btEmpDelete.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btEmpDelete.setText("삭제");

        tbEmp.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        tbEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "사원번호", "이름", "연락처", "직급", "부서"
            }
        ));
        tbEmp.setCellSelectionEnabled(true);
        tbEmp.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbEmp.setDragEnabled(true);
        tbEmp.setRowHeight(25);
        tbEmp.setShowGrid(true);
        jScrollPane2.setViewportView(tbEmp);

        cbName.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cbName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "전체조회", "이름", "사원번호" }));

        bt_dell2.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_dell2.setText("새로고침");

        javax.swing.GroupLayout plDeptLayout = new javax.swing.GroupLayout(plDept);
        plDept.setLayout(plDeptLayout);
        plDeptLayout.setHorizontalGroup(
            plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plDeptLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(plDeptLayout.createSequentialGroup()
                        .addComponent(cbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(btSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(plDeptLayout.createSequentialGroup()
                        .addComponent(btEmpAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btEmpModify, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btEmpDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_dell2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(plInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
        plDeptLayout.setVerticalGroup(
            plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plDeptLayout.createSequentialGroup()
                .addGroup(plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plDeptLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, plDeptLayout.createSequentialGroup()
                                .addComponent(tfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addGroup(plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btEmpAdd)
                                .addComponent(btEmpModify)
                                .addComponent(btEmpDelete)
                                .addComponent(bt_dell2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, plDeptLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btSearch)
                        .addGap(9, 9, 9)))
                .addGroup(plDeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(plInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        tabP1.addTab("사원관리", plDept);

        plnput2.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(153, 153, 153)));

        jLabel10.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("부서번호");

        jLabel11.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("부서명");

        jLabel12.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("파트장");

        btDeptFinish.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btDeptFinish.setText("확인");

        btDepNameSh.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btDepNameSh.setText("검색");

        javax.swing.GroupLayout plnput2Layout = new javax.swing.GroupLayout(plnput2);
        plnput2.setLayout(plnput2Layout);
        plnput2Layout.setHorizontalGroup(
            plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plnput2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(plnput2Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfDeptManager, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btDepNameSh, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(plnput2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfDeptName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(plnput2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfDeptNo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, plnput2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btDeptFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        plnput2Layout.setVerticalGroup(
            plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plnput2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(plnput2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                    .addComponent(tfDeptNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDeptName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btDepNameSh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(plnput2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfDeptManager, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btDeptFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        tbDept.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        tbDept.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "부서번호", "부서명", "파트장", "인원수"
            }
        ));
        tbDept.setCellSelectionEnabled(true);
        tbDept.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbDept.setDragEnabled(true);
        tbDept.setRowHeight(25);
        tbDept.setShowGrid(true);
        jScrollPane1.setViewportView(tbDept);

        cbDept.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        cbDept.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "전체조회", "부서번호", "부서명" }));

        tf_serch.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N

        btDepRegister.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btDepRegister.setText("등록");

        btDepModify.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btDepModify.setText("수정");
        btDepModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDepModifyActionPerformed(evt);
            }
        });

        btDepDelete.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btDepDelete.setText("삭제");

        bt_dell4.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        bt_dell4.setText("새로고침");

        btDeptSearch.setFont(new java.awt.Font("맑은 고딕", 0, 12)); // NOI18N
        btDeptSearch.setText("검색");

        javax.swing.GroupLayout plMemLayout = new javax.swing.GroupLayout(plMem);
        plMem.setLayout(plMemLayout);
        plMemLayout.setHorizontalGroup(
            plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plMemLayout.createSequentialGroup()
                .addGroup(plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(plMemLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(cbDept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_serch, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btDeptSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(plMemLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(plMemLayout.createSequentialGroup()
                        .addComponent(btDepRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btDepModify, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btDepDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_dell4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(plnput2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36))
        );
        plMemLayout.setVerticalGroup(
            plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plMemLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btDepRegister)
                        .addComponent(btDepModify)
                        .addComponent(btDepDelete)
                        .addComponent(bt_dell4))
                    .addGroup(plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbDept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tf_serch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btDeptSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(plMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plMemLayout.createSequentialGroup()
                        .addComponent(plnput2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(150, Short.MAX_VALUE))
                    .addGroup(plMemLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                        .addGap(9, 9, 9))))
        );

        tabP1.addTab("부서관리", plMem);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabP1, javax.swing.GroupLayout.PREFERRED_SIZE, 820, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(tabP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btEmpModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEmpModifyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btEmpModifyActionPerformed

    private void btDepModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDepModifyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btDepModifyActionPerformed

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
            java.util.logging.Logger.getLogger(EmpGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpGUI().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDepDelete;
    private javax.swing.JButton btDepModify;
    private javax.swing.JButton btDepNameSh;
    private javax.swing.JButton btDepRegister;
    private javax.swing.JButton btDeptFinish;
    private javax.swing.JButton btDeptSearch;
    private javax.swing.JButton btEmpAdd;
    private javax.swing.JButton btEmpDelete;
    private javax.swing.JButton btEmpModify;
    private javax.swing.JButton btFinish;
    private javax.swing.JButton btSearch;
    private javax.swing.JButton bt_dell2;
    private javax.swing.JButton bt_dell4;
    private javax.swing.JComboBox<String> cbDept;
    private javax.swing.JComboBox<String> cbEmpDept;
    private javax.swing.JComboBox<String> cbName;
    private javax.swing.JComboBox<String> cbPosition;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel plDept;
    private javax.swing.JPanel plInput;
    private javax.swing.JPanel plMem;
    private javax.swing.JPanel plnput2;
    private javax.swing.JTabbedPane tabP1;
    private javax.swing.JTable tbDept;
    private javax.swing.JTable tbEmp;
    private javax.swing.JTextField tfDeptManager;
    private javax.swing.JTextField tfDeptName;
    private javax.swing.JTextField tfDeptNo;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfNo;
    private javax.swing.JTextField tfPw;
    private javax.swing.JTextField tfSearch;
    private javax.swing.JTextField tfTel;
    private javax.swing.JTextField tf_serch;
    // End of variables declaration//GEN-END:variables
 private void showByTable(String empNo) throws SQLException {
        //테이블 선택하면 뿌려주는 용도
        EmpDTO dto = empDao.selectTableEmpNo(empNo);
        
        String po = dto.getPosition();
        int dept = dto.getDeptNo();
        String name = empDao.findDeptName(dept);
        System.out.println(dept);
        System.out.println("name = " +name + "dept = " + dept);
        
        tfNo.setText(dto.getEmpNo());
        tfName.setText(dto.getEname());
        tfTel.setText(dto.getHp());
        cbPosition.setSelectedItem(dto.getPosition());
        cbEmpDept.setSelectedItem(name);
        tfPw.setText(dto.getPw());
    }

    private void showAllEmp() throws SQLException {
        //사원 전체조회
        List<EmpTableDTO> list = empDao.selectAll();

        //유효성
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사원 정보가 없습니다");
            return;
        }

        String[][] data = new String[list.size()][colNames1.length];
        for (int i = 0; i < list.size(); i++) {
            EmpTableDTO dto = list.get(i);

            data[i][0] = dto.getEmpNo();
            data[i][1] = dto.getEname();
            data[i][2] = dto.getHp();
            data[i][3] = dto.getPosition();
            data[i][4] = dto.getDeptName();
        }
        model.setDataVector(data, colNames1);
        tbEmp.setModel(model);
        
        tbEmp.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbEmp.getColumnModel().getColumn(1).setPreferredWidth(30);
        tbEmp.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbEmp.getColumnModel().getColumn(3).setPreferredWidth(30);
        tbEmp.getColumnModel().getColumn(4).setPreferredWidth(40);
        tableCellCenter(tbEmp);
    }//

    private void showEmpName() throws SQLException {
        //사원 이름으로 조회
        String ename = tfSearch.getText();
        List<EmpTableDTO> list = empDao.selectName(ename);

        //유효성
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사원 정보가 없습니다.");
            tfSearch.requestFocus();
            return;
        } else if (ename == null || ename.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름을 입력하세요.");
            tfSearch.requestFocus();
            return;
        }

        String[][] data = new String[list.size()][colNames1.length];
        for (int i = 0; i < list.size(); i++) {
            EmpTableDTO dto = list.get(i);

            data[i][0] = dto.getEmpNo();
            data[i][1] = dto.getEname();
            data[i][2] = dto.getHp();
            data[i][3] = dto.getPosition();
            data[i][4] = dto.getDeptName();
        }
        model.setDataVector(data, colNames1);
        tbEmp.setModel(model);
        
        tbEmp.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbEmp.getColumnModel().getColumn(1).setPreferredWidth(30);
        tbEmp.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbEmp.getColumnModel().getColumn(3).setPreferredWidth(30);
        tbEmp.getColumnModel().getColumn(4).setPreferredWidth(40);
        
        tableCellCenter(tbEmp);
    }//

    private void showEmpNo() throws SQLException {
        //사원번호로 사원 조회
        String empNo = tfSearch.getText();
        List<EmpTableDTO> list = empDao.selectNo(empNo);

        //유효성검사
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사원 정보가 없습니다.");
            tfSearch.requestFocus();
            return;
        } else if (empNo == null || empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사원번호를 입력하세요.");
            tfSearch.requestFocus();
            return;
        }

        String[][] data = new String[list.size()][colNames1.length];
        for (int i = 0; i < list.size(); i++) {
            EmpTableDTO dto = list.get(i);

            data[i][0] = dto.getEmpNo();
            data[i][1] = dto.getEname();
            data[i][2] = dto.getHp();
            data[i][3] = dto.getPosition();
            data[i][4] = dto.getDeptName();
        }
        model.setDataVector(data, colNames1);
        tbEmp.setModel(model);
        
        tbEmp.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbEmp.getColumnModel().getColumn(1).setPreferredWidth(30);
        tbEmp.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbEmp.getColumnModel().getColumn(3).setPreferredWidth(30);
        tbEmp.getColumnModel().getColumn(4).setPreferredWidth(40);
        
        tableCellCenter(tbEmp);
    }//

    private void addEmpNo() throws SQLException {
        int deptno = findDeptno();
        EmpDTO dto = empDao.AddInNo(deptno);
        
        tfNo.setText(dto.getEmpNo());
    }
    
    private void addEmp() throws SQLException {
        System.out.println("test");
        //사원 정보 추가
        String empno = tfNo.getText();
        String eName = tfName.getText();
        String hp = tfTel.getText();
        String pw = tfPw.getText();

        //유효성
        if (eName == null || eName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름를 입력하세요 !");
            tfName.requestFocus();
            return;
        } else if (hp == null || hp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "연락처를 입력하세요 !");
            tfTel.requestFocus();
            return;
        } else if (pw == null || pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요 !");
            tfPw.requestFocus();
            return;
        }
        
        String position = findPosition();
        System.out.println("position = " + position);

        int deptno = findDeptno();
        
        EmpDTO dto = new EmpDTO();
        dto.setEmpNo(empno);
        dto.setEname(eName);
        dto.setHp(hp);
        dto.setPosition(position);
        dto.setDeptNo(deptno);
        dto.setPw(pw);

        
        int update = JOptionPane.showConfirmDialog(this, "사원을 등록하시겠습니까 ?",
                "등록", JOptionPane.YES_NO_OPTION);

        if (update == JOptionPane.YES_OPTION) {
            int cnt = empDao.insertEmp(dto);

            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "사원 등록을 완료했습니다.");
                showAllEmp();
                clearTfEmp();
            } else {
                JOptionPane.showMessageDialog(this, "사원 등록 실패 !!");
            }
            
        }
        
    }//

    private void modifyEmp() throws SQLException {
        //사원정보 수정
        String empNo = tfNo.getText();
        String eName = tfName.getText();
        String hp = tfTel.getText();
        String pw = tfPw.getText();

        if (empNo == null || empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사원번호를 입력하세요 !");
            tfNo.requestFocus();
            return;
        } else if (eName == null || eName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름를 입력하세요 !");
            tfName.requestFocus();
            return;
        } else if (hp == null || hp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "연락처를 입력하세요 !");
            tfTel.requestFocus();
            return;
        } else if (pw == null || pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요 !");
            tfPw.requestFocus();
            return;
        }

        int deptno = findDeptno();

        String position = findPosition();

        EmpDTO dto = new EmpDTO();
        dto.setEmpNo(empNo);
        dto.setEname(eName);
        dto.setHp(hp);
        dto.setPosition(position);
        dto.setDeptNo(deptno);
        dto.setPw(pw);

        int update = JOptionPane.showConfirmDialog(this, empNo + "의 정보를 수정하시겠습니까 ?",
                "수정", JOptionPane.YES_NO_OPTION);

        if (update == JOptionPane.YES_OPTION) {
            int cnt = empDao.modifyEmp(dto);

            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "사원정보가 수정되었습니다.");
                showAllEmp();
                clearTfEmp();
            } else {
                JOptionPane.showMessageDialog(this, "사원정보 수정이 실패했습니다!");
                return;
            }
        }
    }//

    private void deleteEmp() throws SQLException {
        //사원정보 삭제
        String empNo = tfNo.getText();

        //유효성 검사
        if (empNo == null || empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사원번호를 입력하세요 !");
            tfNo.requestFocus();
            return;
        }

        EmpDTO dto = new EmpDTO();
        dto.setEmpNo(empNo);

        int delete = JOptionPane.showConfirmDialog(this, empNo + "를 삭제하시겠습니까 ?",
                "삭제", JOptionPane.YES_NO_OPTION);
        if (delete == JOptionPane.YES_OPTION) {
            int cnt = empDao.deleteEmp(empNo);

            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "사원정보가 삭제되었습니다.");
                clearTfEmp();
                showAllEmp();
            } else {
                JOptionPane.showMessageDialog(this, "사원정보 삭제를 실패했습니다!");
                tfNo.requestFocus();
                return;
            }
        }
    }//

    private int findDeptno() throws SQLException {
        //부서번호 찾기
        /*
        int deptno = 0;
        int index = cbEmpDept.getSelectedIndex();
        deptno = 100 + (index * 10);
        System.out.println("부서번호 찾기 deptno = " + deptno);
        return deptno;
        */
        
        String empName = cbEmpDept.getSelectedItem().toString();
        System.out.println(empName);
        
        DeptDAO deptDao = new DeptDAO();
        DeptDTO dto =  deptDao.serchDeptNO(empName);
        return dto.getDeptNo();
        
    }
    
    private String findPosition() {
        String position = "";
        if (cbPosition.getSelectedIndex() == 0) {
            position = "인턴";
        } else if (cbPosition.getSelectedIndex() == 1) {
            position = "사원";
        } else if (cbPosition.getSelectedIndex() == 2) {
            position = "대리";
        } else if (cbPosition.getSelectedIndex() == 3) {
            position = "과장";
        } else if (cbPosition.getSelectedIndex() == 4) {
            position = "부장";
        }
        return position;
    }
    
    private void getDeptNameList() throws SQLException {
        //사원탭 부서이름 콤보박스에 db 넣기
        Vector<String> vec = empDao.getDeptName();
        
        cbModel = new DefaultComboBoxModel<>(vec);
        cbEmpDept.setModel(cbModel);
    }//

    private void showAllDept() throws SQLException {
        //부서 전체조회
        List<DeptTableDTO> list = deptDao.selectAll();

        //유효성
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사원 정보가 없습니다.");
            return;
        }

        String[][] data = new String[list.size()][colNames2.length];
        for (int i = 0; i < list.size(); i++) {
            DeptTableDTO dto = list.get(i);

            data[i][0] = dto.getDeptNo() + "";
            data[i][1] = dto.getDeptName();
            data[i][2] = dto.getManager();
            data[i][3] = dto.getTotal() + "";
        }
        model.setDataVector(data, colNames2);
        tbDept.setModel(model);
        tableCellCenter(tbDept);
    }//
    
    private void showDeptNo() throws SQLException {
        //부서번호로 조회
        String deptno = tf_serch.getText();
        List<DeptTableDTO> list = deptDao.selectDeptNO(Integer.parseInt(deptno));
        
        //유효성 검사
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서 정보가 없습니다.");
            tf_serch.requestFocus();
            return;
        } else if (deptno == null || deptno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서번호를 입력하세요.");
            tf_serch.requestFocus();
            return;
        }
        
        String[][] data = new String[list.size()][colNames2.length];
        for (int i = 0; i < list.size(); i++) {
            DeptTableDTO dto = list.get(i);
            
            data[i][0] = dto.getDeptNo() + "";
            data[i][1] = dto.getDeptName();
            data[i][2] = dto.getManager();
            data[i][3] = dto.getTotal() + "";
        }
        model.setDataVector(data, colNames2);
        tbDept.setModel(model);
        tableCellCenter(tbDept);
    }//
    
    private void showDeptName() throws SQLException {
        //부서이름으로 검색
        String deptname = tf_serch.getText();
        List<DeptTableDTO> list = deptDao.selectDeptName(deptname);
        
        //유효성 검사
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서 정보가 없습니다.");
            tf_serch.requestFocus();
            return;
        } else if (deptname == null || deptname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서명을 입력하세요.");
            tf_serch.requestFocus();
            return;
        }
        
        String[][] data = new String[list.size()][colNames2.length];
        for (int i = 0; i < list.size(); i++) {
            DeptTableDTO dto = list.get(i);
            
            data[i][0] = dto.getDeptNo() + "";
            data[i][1] = dto.getDeptName();
            data[i][2] = dto.getManager();
            data[i][3] = dto.getTotal() + "";
        }
        model.setDataVector(data, colNames2);
        tbDept.setModel(model);
        tableCellCenter(tbDept);
    }//
    
    private void addDept() throws SQLException {
        //부서 등록
        String deptname = tfDeptName.getText();
        
        //유효성 검사
        if (deptname == null || deptname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서명를 입력하세요.");
            tfDeptName.requestFocus();
            return;
        } 
        
        DeptDTO dto = new DeptDTO();
        dto.setDeptName(deptname);

        int update = JOptionPane.showConfirmDialog(this, "부서를 등록하시겠습니까 ?",
                "등록", JOptionPane.YES_NO_OPTION);

        if (update == JOptionPane.YES_OPTION) {
            int cnt = deptDao.insertDept(dto);

            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "부서 등록이 완료되었습니다.");
                showAllDept();
                clearTfDept();
            } else {
                JOptionPane.showMessageDialog(this, "부서 등록을 실패했습니다.");
            }

        }

    }//
    
    private void modifyDept() throws SQLException {
        //부서 정보 수정
        String deptno = tfDeptNo.getText();
        String deptname = tfDeptName.getText();
        String manager = tfDeptManager.getText();
        
        //유효성 검사
        if (deptno == null || deptno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서번호를 입력하세요.");
            tfDeptNo.requestFocus();
            return;
        } else if (deptname == null || deptname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서명를 입력하세요.");
            tfDeptName.requestFocus();
            return;
        } else if (manager == null || manager.isEmpty()) {
            JOptionPane.showMessageDialog(this, "파트장을 입력하세요.");
            tfDeptManager.requestFocus();
            return;
        } else if (!isDupChecked()) {
            JOptionPane.showMessageDialog(this, "파트장 이름을 확인하세요.");
            btDepNameSh.requestFocus();
            return;
        }
        
        DeptDTO dto = new DeptDTO();
        dto.setDeptNo(Integer.parseInt(deptno));
        dto.setDeptName(deptname);
        dto.setManager(manager);
        
        
        int update = JOptionPane.showConfirmDialog(this, "부서를 수정하시겠습니까 ?",
                "수정", JOptionPane.YES_NO_OPTION);

        if (update == JOptionPane.YES_OPTION) {
            int cnt = deptDao.modifyDept(dto);

            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "부서 수정이 완료되었습니다.");
                showAllDept();
                clearTfDept();
            } else {
                JOptionPane.showMessageDialog(this, "부서 수정을 실패했습니다.");
            }

        }

    }//
     
    private void deleteDept() throws SQLException {
        //부서 삭제
        String deptno = tfDeptNo.getText();
        
        //유효성 검사
        if(deptno == null || deptno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "부서번호를 입력하세요.");
            tfDeptNo.requestFocus();
            return;
        }
        
        DeptDTO dto = new DeptDTO();
        dto.setDeptNo(Integer.parseInt(deptno));
        
        
        //삭제 전 확인 
        int delete = JOptionPane.showConfirmDialog(this, deptno + "를 삭제하시겠습니까 ?",
                "삭제", JOptionPane.YES_NO_OPTION);
        if (delete == JOptionPane.YES_OPTION) {
            
        int cnt = deptDao.deleteDept(Integer.parseInt(deptno));

            if (cnt > 0) {
                JOptionPane.showMessageDialog(this, "부서가 삭제되었습니다.");
                showAllDept();
                clearTfDept();
            } else {
                JOptionPane.showMessageDialog(this, "부서 삭제를 실패했습니다.");
            }
        }
    }//
    
    private void showTableDept(int deptno) throws SQLException {
        //부서 테이블 선택 시 값 뿌리기
        DeptDTO dto = deptDao.showTableDept(deptno);
        
        deptno = dto.getDeptNo();
        String deptname = dto.getDeptName();
        String manager = dto.getManager();
        
        tfDeptNo.setText(Integer.toString(deptno));
        tfDeptName.setText(deptname);
        tfDeptManager.setText(manager);
        
        System.out.println("테이블 값 넣기");
    }//
    
    private void duplicated() throws SQLException {
        //파트장 이름 존재하는지 확인
        String empname = tfDeptManager.getText();
        if (empname == null || empname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "파트장 이름를 입력하세요.");
            tfDeptManager.requestFocus();
            return;
        }
        
        int result = deptDao.findManager(empname);
        
        if (result == deptDao.USABLE_MANAGER) {
            int type = JOptionPane.showConfirmDialog(this, "파트장으로 등록 가능합니다." 
                            + "등록하시겠습니까?", "이름 확인", JOptionPane.YES_NO_OPTION);
            if (type == JOptionPane.YES_OPTION) {
                setDupChecked(true);
                System.out.println("");
            }
        } else if (result == deptDao.UNUSABLE_MANAGER) {
            JOptionPane.showMessageDialog(this, "사원이 존재하지 않습니다.");

        } else {
            JOptionPane.showMessageDialog(this, "사원 확인을 실패했습니다.");
        }
    }//
    
    private void EmpRefresh() {
        int re = JOptionPane.showConfirmDialog(this, "새로고침 하시겠습니까 ?", "확인", JOptionPane.YES_NO_OPTION);
        if (re == JOptionPane.YES_OPTION) {
            try {
                showAllEmp();
                clearTfEmp();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }//if
    }//
    
    private void DeptRefresh() {
        int re = JOptionPane.showConfirmDialog(this, "새로고침 하시겠습니까 ?", "확인", JOptionPane.YES_NO_OPTION);
        if (re == JOptionPane.YES_OPTION) {
            try {
                showAllDept();
                clearTfDept();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }//if
    }//
    
    private void clearTfEmp() {
        tfNo.setText("");
        tfName.setText("");
        tfTel.setText("");
        tfPw.setText("");
        cbEmpDept.setSelectedIndex(0);
        cbPosition.setSelectedIndex(0);
        cbName.setSelectedIndex(0);
        tfSearch.setText("");
    }

    private void clearTfDept() {
        tfDeptNo.setText("");
        tfDeptName.setText("");
        tfDeptManager.setText("");
        tf_serch.setText("");
        cbDept.setSelectedIndex(0);
    }
    
    private void setEditingEmpMain() { 
        //기본으로 입력 못하게 막기
        tfNo.setEditable(false);
        tfName.setEditable(false);
        tfTel.setEditable(false);
        tfPw.setEditable(false);
        cbEmpDept.setEditable(false);
        cbPosition.setEditable(false);
    }//
    
    private void setEditingDeptMain() {
        //기본으로 입력 못하게 막기
        tfDeptNo.setEditable(false);
        tfDeptName.setEditable(false);
        tfDeptManager.setEditable(false);
    }//
    
    private void editingEmpAdd() {
        //등록버튼 누르면 입력할 수 있게
        tfName.setEditable(true);
        tfTel.setEditable(true);
        tfPw.setEditable(true);
        cbEmpDept.setEditable(true);
        cbPosition.setEditable(true);
    }
    
    private void editingEmpModi() {
        //수정버튼 누르면 입력할 수 있게
        tfName.setEditable(true);
        tfTel.setEditable(true);
        tfPw.setEditable(true);
        cbEmpDept.setEditable(true);
        cbPosition.setEditable(true);
        tfNo.setEditable(true);
    }//
    
    private void editingEmpDel() {
        //삭제버튼 누르면 입력할 수 있게
        tfNo.setEditable(true);
    }
    
    private void editingDeptAdd() {
        //부서 생성
        tfDeptName.setEditable(true);
    }
    
    private void editingDeptModi() {
        //부서 수정
        tfDeptName.setEditable(true);
        tfDeptManager.setEditable(true);
        tfDeptNo.setEditable(true); 
    }
    
    private void editingDeptDel() {
        //부서 삭제
        tfDeptNo.setEditable(true);
    }
    
    private void editingEmp() {
        //등록 누르면 사원번호, 부서 제외한 나머지 초기화
        tfName.setText("");
        tfTel.setText("");
        tfPw.setText("");
        cbPosition.setSelectedIndex(0);
    }
    
    
    

   /*---------------------------------테이블 디자인 관련(출력포함)--------------------------------------*/
  


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
    }//
    
 
}



