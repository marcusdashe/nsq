/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DataValidation.ErrorStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.payload.OptionPayload;
import org.cstemp.nsq.repos.OptionRepository;
import org.cstemp.nsq.util.NinasUtil;
import org.cstemp.nsq.util.OptionType;
import org.cstemp.nsq.util.XCellDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author chibuezeharry
 */
@Service
@Slf4j
public class TemplateService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private OptionService optionService;

    public XSSFWorkbook generateSpreadSheetTemplate(Class aClass) {

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet(aClass.getSimpleName());

        Field[] fields = aClass.getDeclaredFields();

        sheet = mapColumnValidations(fields, sheet);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.THICK);
        headerStyle.setBottomBorderColor(IndexedColors.DARK_RED.getIndex());
        headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.DARK_RED.getIndex());
        font.setBold(true);
        headerStyle.setFont(font);

        XSSFFont requiredFont = workbook.createFont();
        requiredFont.setFontName("Calibri");
        requiredFont.setFontHeightInPoints((short) 16);
        requiredFont.setColor(IndexedColors.ORANGE.getIndex());
        requiredFont.setBold(true);
        headerStyle.setFont(requiredFont);

        Cell headerCell;

        XSSFRichTextString headerName;

        XCellDetail xCellDetail;

        for (int i = 0; i < fields.length; i++) {

            headerCell = header.createCell(i);

            headerCell.setCellStyle(headerStyle);

            headerName = new XSSFRichTextString();

            if ((xCellDetail = fields[i].getAnnotation(XCellDetail.class)) != null) {

                headerName.append((xCellDetail.label().isBlank() ? NinasUtil.camelToTitleCase(fields[i].getName()) : xCellDetail.label()), font);

                if (xCellDetail.indexed()) {

                    headerName.append(" *", requiredFont);
                }

            } else {

                headerName.append(NinasUtil.camelToTitleCase(fields[i].getName()), font);

            }

            headerCell.setCellValue(headerName);

            sheet.autoSizeColumn(i);

        }

        return workbook;
    }

    private XSSFSheet mapColumnValidations(Field[] fields, XSSFSheet sheet) {

        DataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);

        for (int i = 0; i < fields.length; i++) {

            CellRangeAddressList range = new CellRangeAddressList(1, 200000, i, i);

            OptionType optionType;

            if (Number.class.equals(fields[i].getType().getSuperclass())) {

                DataValidationConstraint constraint = dvHelper.createIntegerConstraint(OperatorType.BETWEEN, "0", Long.MAX_VALUE + "");

                DataValidation validation = dvHelper.createValidation(constraint, range);

                validation.setErrorStyle(ErrorStyle.STOP);
                validation.createErrorBox("Invalid Input!", "Only numeric values are allowed");
                validation.setShowErrorBox(true);

                sheet.addValidationData(validation);

            } else if ((optionType = fields[i].getAnnotation(OptionType.class)) != null) {

                String optionName = optionType.value();

                List<OptionPayload.OptionResponse> options = optionService.getOptionsByName(optionName);

                String[] optionsArray = new String[options.size()];

                for (int pos = 0; pos < optionsArray.length; pos++) {
                    optionsArray[pos] = options.get(pos).getValue();
                }

                DataValidationConstraint constraint = dvHelper.createExplicitListConstraint(optionsArray);

                DataValidation validation = dvHelper.createValidation(constraint, range);

                validation.setErrorStyle(ErrorStyle.STOP);
                validation.createErrorBox("Invalid Input!", "Select an option from the list");
                validation.setShowErrorBox(true);

                sheet.addValidationData(validation);

//                }
            }
        }

        return sheet;

    }

    public <T> List<T> importSpreadSheet(Class<T> aClass, XSSFSheet sheet) throws Exception {

        List<T> list = new ArrayList<>();

        Iterator<Row> rows = sheet.rowIterator();

        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
        }

        Row headerRow = rows.next();

        validateHeaderRow(fields, headerRow);

        for (int pos = 1; pos < sheet.getPhysicalNumberOfRows() && rows.hasNext(); pos++) {

            Object obj = aClass.getConstructors()[0].newInstance();

            DataFormatter fmt = new DataFormatter();

            T t = aClass.cast(obj);

            Row row = rows.next();

            Cell cell;
            String label;
            XCellDetail xCellDetail;
//
            for (int i = 0; i < fields.length; i++) {

                cell = row.getCell(i);

                if ((xCellDetail = fields[i].getAnnotation(XCellDetail.class)) != null) {

                    label = xCellDetail.label().isBlank() ? NinasUtil.camelToTitleCase(fields[i].getName()) : xCellDetail.label();

                    if (xCellDetail.indexed() && fmt.formatCellValue(cell).trim().isEmpty()) {

                        throw new BaseException("The " + label + " cell on row " + (pos) + " is empty", HttpStatus.BAD_REQUEST);
                    }
                }

                Object cellValue;

                if (Date.class.equals(fields[i].getType())) {

                    cellValue = cell.getDateCellValue();

                } else if (String.class.equals(fields[i].getType())) {

                    cellValue = fmt.formatCellValue(cell);

                } else if (Number.class.equals(fields[i].getType().getSuperclass())) {

                    cellValue = cell.getNumericCellValue();

                } else if (Boolean.class.equals(fields[i].getType())) {

                    cellValue = cell.getBooleanCellValue();

                } else {

                    cellValue = cell.getStringCellValue();

                }

                fields[i].set(t, cellValue);

            }

            list.add(t);

        }

        return list;
    }

    private <T> void validateHeaderRow(Field[] fields, Row headerRow) {

        DataFormatter fmt = new DataFormatter();

        Cell cell;

        String label;
        XCellDetail xCellDetail;

        for (int i = 0; i < fields.length; i++) {

            cell = headerRow.getCell(i);

            if ((xCellDetail = fields[i].getAnnotation(XCellDetail.class)) != null) {

                label = xCellDetail.label().isBlank() ? NinasUtil.camelToTitleCase(fields[i].getName()) : xCellDetail.label();

                if (xCellDetail.indexed()) {

                    label += " *";
                }

            } else {

                label = NinasUtil.camelToTitleCase(fields[i].getName());

            }

            if (!fmt.formatCellValue(cell).contains(label)) {

                throw new BaseException("The header cell on column " + (i + 1) + " should be the labelled: " + (label), HttpStatus.BAD_REQUEST);
            }
        }

    }

}
