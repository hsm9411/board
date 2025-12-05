import React from 'react';
import styles from './BasicTable.module.css';

function BasicTable({ headers, items, onRowClick }) {

    const handleRowClick = (item) => {
        if (onRowClick) {
            onRowClick(item);
        }
    };
    return (
        <table className={styles.basicTable}>
            <thead>
                <tr>
                    {headers.map((header) => (
                        <th key={header.key} style={{ width: header.width }}>
                            {header.text}
                        </th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {items.length > 0 ? (
                    items.map((item, index) => (
                        <tr key={index} onClick={() => handleRowClick(item)}>
                            {headers.map((header) => (
                                <td key={header.key}>
                                    {item[header.key]}
                                </td>
                            ))}
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={headers.length}>
                            표시할 데이터가 없습니다.
                        </td>
                    </tr>
                )}
            </tbody>
        </table>
    );
}

export default BasicTable;