package gui;

import logic.GradeSheet;
import logic.LogicProvider;
import logic.MarkInfo;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class MainForm extends JFrame implements View {
    private JPanel mainWindow;
    private JTable dataTable;
    private JButton createNewSheetButton;
    private JLabel letiImage;
    private JButton openExistingSheetButton;
    private JButton aboutAuthorButton;
    private JLabel groupNumberLabel;
    private JLabel teacherLabel;
    private JButton saveButton;
    private JLabel subjectLabel;
    private JButton addStudentButton;
    private JButton removeStudentButton;

    private LogicProvider logicProvider = new LogicProvider(this);

    private Boolean isTableModelListenerEnabled = true;
    private DefaultTableModel tableModel = new DefaultTableModel(null, new String[]{"№", "ФИО", "Оценка"}) {
        @Override
        public boolean isCellEditable(int row, int column) {
            //0 колонка - колонка с номерами, её не надо редактировать
            if (column == 0) return false;
            return true;
        }
    };


    public MainForm() {
        setListeners();
        createDataTable();

        setTitle("Редактор ведомости успеваемости");
        setContentPane(mainWindow);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //Настройка таблицы ведомости в GUI
    private void createDataTable() {
        //устанавливает кол во способов выбора строк
        dataTable.setSelectionMode(SINGLE_SELECTION);
        //высота строки
        dataTable.setRowHeight(40);

        dataTable.setModel(tableModel);

        dataTable.setFont(new Font("Consolas", Font.BOLD, 16));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        dataTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        dataTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        dataTable.getColumnModel().getColumn(0).setMaxWidth(50);
        dataTable.getColumnModel().getColumn(0).setMinWidth(50);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(2).setMinWidth(100);;

    }

    private void setDataToTable(ArrayList<MarkInfo> marksInfo) {

        //Чтобы при выполнении следующей инструкции не вызвался слушатель изменения таблицы, заглушаю его сначала
        isTableModelListenerEnabled = false;

        //Сброс всего содержимого таблицы перед установкой нового содержимого
        tableModel.setRowCount(0);

        for (int i = 0; i < marksInfo.size(); i++) {
            tableModel.addRow(new String[]{Integer.toString(i+1), marksInfo.get(i).getStudent(), marksInfo.get(i).getMark()});
        }

        dataTable.setModel(tableModel);

        //Включаю слушателя обратно
        isTableModelListenerEnabled = true;
    }

    //Установка слушателей событий
    private void setListeners() {
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                if (isTableModelListenerEnabled) {
                    System.out.println("Table changed");
                    //Получение номера измененной строки
                    int rowOfChanging = tableModelEvent.getFirstRow();

                    logicProvider.updateGradeSheetData(
                            rowOfChanging,
                            (String) tableModel.getValueAt(rowOfChanging, 1),
                            (String) tableModel.getValueAt(rowOfChanging, 2)
                    );
                }
            }
        });

        //кнопка создания новой ведомости
        createNewSheetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Create new sheet button clicked");

                NewSheetInfo newSheetInfo = new NewSheetInfo();

                if (newSheetInfo.isInputCorrect()) {
                    logicProvider.createNewSheet(
                            newSheetInfo.getGroup(),
                            newSheetInfo.getSubject(),
                            newSheetInfo.getTeacher());
                }
            }
        });

        //кнопка открытия ведомости
        openExistingSheetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open existing sheet button clicked");

                String selectedFile = selectFile();

                if (selectedFile != null) logicProvider.loadSheet(selectedFile);
            }
        });

        //кнопка сохранения ведомости
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Save button clicked");

                String selectedFile = selectFile();

                if (selectedFile != null) logicProvider.exportToSvg(selectedFile);
            }
        });

        //информация
        aboutAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("About button clicked");

                JOptionPane.showMessageDialog(null, "Создатель программы Чередилина Е.А. группа 8301",
                        "Информация об авторе", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //кнопка добавления студента
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Add student button clicked");

                int selectedRow = dataTable.getSelectedRow();

                if (selectedRow == -1) logicProvider.addStudent();
                else logicProvider.addStudent(selectedRow);
            }
        });

        //кнопка удаления студента
        removeStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Remove student button clicked");

                int selectedRow = dataTable.getSelectedRow();

                //-1 будет, когда ничего не выделено. поэтому проверка на != -1
                if (selectedRow != -1) {

                    CellEditor cellEditor = dataTable.getCellEditor();
                    if (cellEditor != null) {
                        if (cellEditor.getCellEditorValue() != null) {
                            cellEditor.stopCellEditing();
                        } else {
                            cellEditor.cancelCellEditing();
                        }
                    }
                    logicProvider.removeStudent(selectedRow);
                }
            }
        });
    }

    private String selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файл ведомостей (.csv)","csv"));
        fileChooser.setDialogTitle("Выберите файл");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getPath();
        } else return null;
    }

    @Override
    public void update() {
        //Получение текущей ведомости
        GradeSheet currentGradeSheet = logicProvider.getCurrentSheet();

        if (currentGradeSheet != null) {
            saveButton.setEnabled(true);
            addStudentButton.setEnabled(true);
            removeStudentButton.setEnabled(true);

            groupNumberLabel.setText("Группа №" + currentGradeSheet.getGroup());
            subjectLabel.setText("Дисциплина: " + currentGradeSheet.getSubject());
            teacherLabel.setText("Преподаватель: " + currentGradeSheet.getTeacher());

            setDataToTable(currentGradeSheet.getMarks());
        }
        else saveButton.setEnabled(false);
    }

    @Override
    public void showError(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
