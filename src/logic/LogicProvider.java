package logic;

import gui.View;

import java.util.ArrayList;
import java.util.List;

public class LogicProvider {

    private final View view;

    private GradeSheet currentSheet = null;

    private final CSVProvider csvProvider = new CSVProvider();

    private final String[] headers = new String[] {"ФИО", "Оценка"};

    public LogicProvider(View view) {
        this.view = view;
    }

    public void createNewSheet(String group, String subject, String teacher) {
        currentSheet = new GradeSheet(group, subject, teacher);
        view.update();
    }
    //изменения в полях ведомости
    public void updateGradeSheetData(int updatingStudentIndex, String name, String mark) {
        if (currentSheet != null) {
            MarkInfo updatingMarkInfo =  currentSheet.getMarks().get(updatingStudentIndex);
            updatingMarkInfo.setStudent(name);
            updatingMarkInfo.setMark(mark);
        }
    }

    public void loadSheet(String filePath) {
        try {
            List<String[]> rawData = csvProvider.readCSV(filePath);

            //Сначала идёт информация о ведомости: группа, предмет, учитель
            String group = rawData.get(0)[1];
            String subject = rawData.get(1)[1];
            String teacher = rawData.get(2)[1];

            int columnsAmount = headers.length;

            currentSheet = new GradeSheet(group, subject, teacher);

            //Заполнение пробелов в строках null-ами
            for (int i = 4; i < rawData.size(); i++) {
                String[] currentLine = rawData.get(i);
                if (currentLine.length < columnsAmount) {
                    String[] filledLine = new String[columnsAmount];
                    for (int j = 0; j < filledLine.length; j++) {
                        if (j < currentLine.length) filledLine[j] = currentLine[j];
                        else filledLine[j] = null;
                    }

                    rawData.set(i, filledLine);
                }

                currentSheet.addMark(new MarkInfo(rawData.get(i)[0], rawData.get(i)[1]));
            }

            view.update();
        } catch (Exception e) {
            e.printStackTrace();

            view.showError("Возникла ошибка при чтении файла.");
        }
    }

    public void exportToSvg(String fileName) {
        try {
            System.out.println("Trying to save gradesheet to " + fileName);
            if (!fileName.substring(fileName.length() - 4).equals(".csv")) {
                fileName += ".csv";
            }
            //получение всех оценок
            ArrayList<MarkInfo> marks = currentSheet.getMarks();

            String[][] data = new String[marks.size()+4][headers.length];

            data[0] = new String[] { "Группа", currentSheet.getGroup() };
            data[1] = new String[] { "Дисциплина", currentSheet.getSubject() };
            data[2] = new String[] { "Преподаватель", currentSheet.getTeacher() };

            data[3] = headers;

            for (int i = 0; i < marks.size(); i++) {
                data[i+4] = new String[] {marks.get(i).getStudent(), marks.get(i).getMark()};
            }

            csvProvider.saveToCSV(fileName, data);

        } catch (Exception e) {
            e.printStackTrace();

            view.showError("Ошибка сохранения данных в файл.");
        }
    }
    //добавление студента без выбранной строки
    public void addStudent() {
        if (currentSheet != null) {
            currentSheet.addMark(new MarkInfo(null, null));
            view.update();
        }
    }

    //Перегрузка функции addStudent, которая добавляет нового студента после текущей выбранной строки
    //(чтобы можно было добавлять студентов посередине таблицы, а не только в конец и т.п.
    public void addStudent(int additionIndex) {
        if (currentSheet != null) {
            currentSheet.getMarks().add(additionIndex+1, new MarkInfo(null, null));
            view.update();
        }
    }

    public void removeStudent(int removableStudentIndex) {
        if (currentSheet != null) {
            currentSheet.removeMark(removableStudentIndex);

            view.update();
        }
    }

    public GradeSheet getCurrentSheet() {
        return currentSheet;
    }
}
