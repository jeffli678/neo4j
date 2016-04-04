/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
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
package org.neo4j.unsafe.impl.batchimport.cache;

import java.nio.ByteBuffer;

import static java.lang.Math.toIntExact;

public class HeapByteArray extends HeapNumberArray<ByteArray> implements ByteArray
{
    private final int length;
    private final byte[] array;
    private final ByteBuffer buffer;
    private final byte[] defaultValue;

    public HeapByteArray( int length, byte[] defaultValue, int base )
    {
        super( defaultValue.length, base );
        this.length = length;
        this.defaultValue = defaultValue;
        this.array = new byte[itemSize * length];
        this.buffer = ByteBuffer.wrap( array );
        clear();
    }

    @Override
    public long length()
    {
        return length;
    }

    @Override
    public void swap( long fromIndex, long toIndex, int numberOfEntries )
    {
        byte[] intermediary = new byte[numberOfEntries * itemSize];
        System.arraycopy( array, index( toIndex, 0 ), intermediary, 0, intermediary.length );
        System.arraycopy( array, index( fromIndex, 0 ), array, index( toIndex, 0 ), intermediary.length );
        System.arraycopy( intermediary, 0, array, index( fromIndex, 0 ), intermediary.length );
    }

    @Override
    public void clear()
    {
        for ( int i = 0; i < length; i++ )
        {
            System.arraycopy( defaultValue, 0, array, i * itemSize, itemSize );
        }
    }

    @Override
    public void get( long index, byte[] into )
    {
        System.arraycopy( array, index( index, 0 ), into, 0, itemSize );
    }

    @Override
    public byte getByte( long index, int offset )
    {
        return buffer.get( index( index, offset ) );
    }

    @Override
    public short getShort( long index, int offset )
    {
        return buffer.getShort( index( index, offset ) );
    }

    @Override
    public int getInt( long index, int offset )
    {
        return buffer.getInt( index( index, offset ) );
    }

    @Override
    public long get6ByteLong( long index, int offset )
    {
        return get6BLongFromByteBuffer( buffer, index( index, offset ) );
    }

    protected static long get6BLongFromByteBuffer( ByteBuffer buffer, int startOffset )
    {
        long low4b = buffer.getInt( startOffset ) & 0xFFFFFFFFL;
        long high2b = buffer.getShort( startOffset + Integer.BYTES );
        return low4b | (high2b << 32);
    }

    @Override
    public long getLong( long index, int offset )
    {
        return buffer.getLong( index( index, offset ) );
    }

    @Override
    public void set( long index, byte[] value )
    {
        System.arraycopy( value, 0, array, index( index, 0 ), itemSize );
    }

    @Override
    public void setByte( long index, int offset, byte value )
    {
        buffer.put( index( index, offset ), value );
    }

    @Override
    public void setShort( long index, int offset, short value )
    {
        buffer.putShort( index( index, offset ), value );
    }

    @Override
    public void setInt( long index, int offset, int value )
    {
        buffer.putInt( index( index, offset ), value );
    }

    @Override
    public void set6ByteLong( long index, int offset, long value )
    {
        int absIndex = index( index, offset );
        buffer.putInt( absIndex, (int) value );
        buffer.putShort( absIndex + Integer.BYTES, (short) (value >>> 32) );
    }

    @Override
    public void setLong( long index, int offset, long value )
    {
        buffer.putLong( index( index, offset ), value );
    }

    private int index( long index, int offset )
    {
        return toIntExact( (rebase( index ) * itemSize) + offset );
    }
}
