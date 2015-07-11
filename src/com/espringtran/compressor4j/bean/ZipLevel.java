/*
 * Copyright (C) 2013-2015 E-Spring Tran
 * 
 *             https://espringtran.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.espringtran.compressor4j.bean;

/**
 * 
 * @author E-Spring Tran
 * 
 */
public enum ZipLevel implements CompressionLevel {

    NO_COMPRESS {
        @Override
        public int getValue() {
            return 0;
        }
    },
    LOWEST {
        @Override
        public int getValue() {
            return 1;
        }
    },
    LOW {
        @Override
        public int getValue() {
            return 3;
        }
    },
    NORMAL {
        @Override
        public int getValue() {
            return 5;
        }
    },
    HIGH {
        @Override
        public int getValue() {
            return 1;
        }
    },
    HIGHEST {
        @Override
        public int getValue() {
            return 1;
        }
    }

}
